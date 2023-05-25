package com.mms.repository;

import com.mms.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WordRepository extends JpaRepository<Word, Long>, JpaSpecificationExecutor<Word> {

    Word findBySeqNo(Long seqNo);

    void deleteBySeqNo(Long seqNo);
}
