package com.mms.controller;

import com.mms.model.*;
import com.mms.repository.WordImageRepository;
import com.mms.service.WordService;
import com.mms.util.FunctionUtil;
import com.mms.util.PageWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/word")
public class WordController extends FunctionUtil {

    private Map<String,Object> map;
    private SessionUser sessionUser;
    private StringBuffer url;
    private final String NAVI = "word";
    private static final String repository = "/repository";

    private PageWrapper<Word> wordPage;
    private Word word;
    private WordImage wordImage;
    private List<WordImage> wordImageList;

    private List<String> typeList = new ArrayList<String>(){
        {
            add("주일");
            add("수요");
            add("새벽");
            add("잠언");
            add("성구");
            add("부서");
            add("지도자교육");
            add("수련회");
            add("수료식");
            add("해외말씀");
            add("교회순회");
            add("사진자료");
            add("기본신앙");
            add("8강말씀");
            add("기타");
        }
    };

    @Autowired
    private WordService wordService;

    @Autowired
    private WordImageRepository wordImageRepository;


    @RequestMapping("/list")
    public String list(
            @PageableDefault(sort = { "serviceDate" }, direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @RequestParam(value = "word", defaultValue = "") String word,
            @RequestParam(value = "type", defaultValue = "") String type,
            @RequestParam(value = "start", defaultValue = "") String start,
            @RequestParam(value = "end", defaultValue = "") String end,
            Model model,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        url = new StringBuffer();

        url.append("/word/list?1=1");

        if(!word.equals("")){
            url.append("&word=");
            url.append(word);
        }
        if(!type.equals("")){
            url.append("&type=");
            url.append(type);
        }
        if(!start.equals("")){
            url.append("&start=");
            url.append(start);
        }
        if(!end.equals("")){
            url.append("&end=");
            url.append(end);
        }


        wordPage = new PageWrapper<Word>(wordService.getList(pageable, word, type, start, end), url.toString());

        // word 색깔입히기
        if(ObjectUtils.isNotEmpty(word)){
            wordPage.getContent().forEach( w -> {
                w.setTitle(addClassRed(w.getTitle(), word));
            });

        }

        url.append("&page=");
        url.append(pageable.getPageNumber());

        session.setAttribute("word", url.toString());

        model.addAttribute("page", wordPage);
        model.addAttribute("word",word);
        model.addAttribute("typeList",typeList);
        model.addAttribute("navi",NAVI);
        model.addAttribute("start",start);
        model.addAttribute("end",end);
        model.addAttribute("type",type);

        return "/word/list";
    }

    @GetMapping("/form")
    public String user(
            Model model,
            Word word,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        word.setType("주일");


        model.addAttribute("navi", NAVI);
        model.addAttribute("word", word);
        model.addAttribute("typeList",typeList);
        model.addAttribute("action","/word/regist");


        return "/word/form";
    }

    @PostMapping("/regist")
    public String regist(
            WordDTO.General dto,
            HttpSession session,
            HttpServletRequest req
    ) throws Exception {

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        String path = "";

        if(req.getRequestURL().toString().contains("localhost:8080")){
            path = "/Users/tommykim/Documents/MorningStarManagement/src/main/resources/static/repository";
        }else{
            path = "/tom0718/tomcat/webapps/ROOT/WEB-INF/classes/static/repository";
        }

        wordService.saveWord(dto, path, sessionUser.getId());

        return "redirect:/word/list";
    }

    @GetMapping("/view/{seqNo}")
    public String view(
            @PathVariable Long seqNo,
            @RequestParam(value = "word", required = false) String searchWord,
            Model model,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        word = new Word();
        word = wordService.getWord(seqNo);

        // word 색깔입히기
        if(ObjectUtils.isNotEmpty(searchWord)){

            word.setTitle(addClassRed(word.getTitle(), searchWord));

            if(ObjectUtils.isNotEmpty(word.getSubTitle())){
                word.setSubTitle(addClassRed(word.getSubTitle(), searchWord));
            }

            if(ObjectUtils.isNotEmpty(word.getBible())){
                word.setBible(addClassRed(word.getBible(), searchWord));
            }

            if(ObjectUtils.isNotEmpty(word.getContents())){
                word.setContents(addClassRed(word.getContents(), searchWord));
            }
            if(ObjectUtils.isNotEmpty(word.getKeyword())){
                word.setKeyword(addClassRed(word.getKeyword(), searchWord));
            }
        }

        if(word.getType().equals("사진자료")){
            wordImageList = new ArrayList<>();
            wordImageList = wordImageRepository.findByWordSeqNo(word.getSeqNo());

            model.addAttribute("imageList", wordImageList);
        }


        model.addAttribute("navi", NAVI);
        model.addAttribute("typeList",typeList);
        model.addAttribute("word", word);

        return "/word/view";
    }

    @GetMapping("/modify/{seqNo}")
    public String modify(
            @PathVariable Long seqNo,
            Model model,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        word = new Word();
        word = wordService.getWord(seqNo);

        if(word.getType().equals("사진자료")){
            wordImageList = new ArrayList<>();
            wordImageList = wordImageRepository.findByWordSeqNo(word.getSeqNo());

            model.addAttribute("imageList", wordImageList);
        }


        model.addAttribute("navi", NAVI);
        model.addAttribute("typeList",typeList);
        model.addAttribute("word", word);
        model.addAttribute("action","/word/regist");

        return "/word/form";
    }

    @GetMapping("/downloadFile")
    public ModelAndView downLoadFile(HttpServletRequest req, Long seqNo) {

        String path = "";

        if(req.getRequestURL().toString().contains("localhost:8080")){
            path = "/Users/tommykim/Documents/MorningStarManagement/src/main/resources/static/repository";
        }else{
            path = req.getServletContext().getRealPath(repository);
        }

        wordImage = new WordImage();
        wordImage = wordImageRepository.findBySeqNo(seqNo);


        ModelAndView mav = new ModelAndView();
        mav.addObject("path", path);
        mav.addObject("file", wordImage.getImage());
        mav.addObject("fileName", wordImage.getFileName());
        mav.setViewName("downloadFile");

        return mav;
    }



    public String addClassRed(String arg, String word){

        StringBuffer sb = new StringBuffer();
        sb.append("<span class=\"red\">");
        sb.append(word);
        sb.append("</span>");

        return arg.replaceAll(word, sb.toString());
    }

    @PostMapping("/deleteWord")
    @ResponseBody
    public Map<String, Object> deleteWord(
            @RequestParam(value = "seqNo", required = true) Long seqNo,
            HttpSession session
    ){

        map = new HashMap<>();

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        map = wordService.deleteWord(seqNo);

        return map;
    }
}
