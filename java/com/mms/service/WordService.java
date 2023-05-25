package com.mms.service;

import com.mms.model.User;
import com.mms.model.Word;
import com.mms.model.WordDTO;
import com.mms.model.WordImage;
import com.mms.repository.WordImageRepository;
import com.mms.repository.WordRepository;
import com.mms.util.FunctionUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class WordService extends FunctionUtil {

    private Map<String, Object> map;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    private Date serviceDate;
    private Date startDate;
    private Date endDate;
    private Word word;
    private WordImage wordImage;
    private Date today;
    private Integer i;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private WordImageRepository wordImageRepository;


    public Page<Word> getList(Pageable pageable, String word, String type, String start, String end) {

        @SuppressWarnings("serial")
        Specification<Word> specification = new Specification<Word>() {

            @Override
            public Predicate toPredicate(Root<Word> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List<Predicate> predicates = new ArrayList<>();

                Boolean flag = false;

                if (!type.equals("")) { //
                    predicates.add(builder.equal(root.get("type"), type));
                    flag = true;
                }

                if(!word.equals("")){
                    predicates.add(builder.or(builder.like(root.get("title"), "%" + word + "%"), builder.like(root.get("subTitle"), "%" + word + "%"), builder.like(root.get("bible"), "%" + word + "%"), builder.like(root.get("contents"), "%" + word + "%"), builder.like(root.get("keyword"), "%" + word + "%")));
                    flag = true;
                }

                if (!start.equals("") && end.equals("")) { //

                    startDate = new Date();
                    try {
                        startDate = sf.parse(start);

                        predicates.add(builder.greaterThanOrEqualTo(root.get("serviceDate"), startDate));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    flag = true;
                }

                if (start.equals("") && !end.equals("")) { //

                    endDate = new Date();
                    try {
                        endDate = sf.parse(end);

                        predicates.add(builder.lessThanOrEqualTo(root.get("serviceDate"), endDate));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    flag = true;
                }

                if (!start.equals("") && !end.equals("")) { //

                    startDate = new Date();
                    endDate = new Date();
                    try {
                        startDate = sf.parse(start);
                        endDate = sf.parse(end);

                        predicates.add(builder.or(builder.greaterThanOrEqualTo(root.get("serviceDate"), startDate),builder.lessThanOrEqualTo(root.get("serviceDate"), endDate)));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    flag = true;
                }

                if(flag){
                    query.where(predicates.toArray(new Predicate[] {}));
                }

                return builder.and(predicates.toArray(new Predicate[] {}));
            }
        };

        return wordRepository.findAll(specification, pageable);

    }

    public Word getWord(Long seqNo) {
        return wordRepository.findBySeqNo(seqNo);
    }

    @Transactional
    public void saveWord(WordDTO.General dto, String path, String user) throws ParseException {

        word = new Word();

        today = Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant());

        if(ObjectUtils.isNotEmpty(dto.getSeqNo())){
            word = wordRepository.findBySeqNo(dto.getSeqNo());

            word.setModDate(today);
            word.setUpdater(user);

        }else{

            word.setRegDate(today);
            word.setRegister(user);


        }

        word.setType(dto.getType());
        word.setTitle(dto.getTitle());
        word.setSubTitle(dto.getSubTitle());
        word.setBible(dto.getBible());
        word.setContents(dto.getContents());
        if(ObjectUtils.isNotEmpty(dto.getServiceDate())){
            word.setServiceDate(sf.parse(dto.getServiceDate()));
        }

        if(ObjectUtils.isNotEmpty(dto.getKeywordList())){
            word.setKeyword(String.join(":",dto.getKeywordList()));
        }else{
            word.setKeyword(null);
        }

        word = wordRepository.save(word);

        wordImageRepository.deleteByWordSeqNo(word.getSeqNo());

        if(dto.getType().equals("사진자료")){

            i = 0;

            dto.getFileList().forEach(f -> {

                wordImage = new WordImage();

                wordImage.setWordSeqNo(word.getSeqNo());

                if(ObjectUtils.isNotEmpty(f.getFile())){
                    wordImage.setImage(uploadImage(f.getFile(), i, path));
                }else{
                    wordImage.setImage(f.getImage());
                }

                wordImage.setFileName(f.getOrigin());

                wordImageRepository.save(wordImage);

                i++;
            });
        }

    }

    public String uploadImage(String image, int i, String dir) {

        String[] strings = image.split(",");
        String extension = "";

        switch (strings[0]) {//check image's extension
            case "data:image/jpeg;base64":
                extension = "jpeg";
                break;
            case "data:image/png;base64":
                extension = "png";
                break;
            case "data:image/gif;base64":
                extension = "gif";
                break;
            default://should write cases for more images types
                extension = "jpg";
                break;
        }

        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);

        StringBuffer path = new StringBuffer();
        StringBuffer name = new StringBuffer();

        path.append(dir);
        path.append(File.separator);
        name.append(setID.apply("W"));
        name.append(i);
        name.append(".");
        name.append(extension);

        File file = new File(path.toString()+name.toString());

        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return name.toString();
    }

    @Transactional
    public Map<String, Object> deleteWord(Long seqNo) {
        map = new HashMap<>();

        wordImageRepository.deleteByWordSeqNo(seqNo);
        wordRepository.deleteBySeqNo(seqNo);

        return map;
    }
}
