package com.mms.repository;

import com.mms.model.WordImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordImageRepository extends JpaRepository<WordImage, Long> {
    void deleteByWordSeqNo(Long wordSeqNo);

    List<WordImage> findByWordSeqNo(Long wordSeqNo);

    WordImage findBySeqNo(Long seqNo);
}
