package com.mms.repository;

import com.mms.model.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RelationRepository extends JpaRepository<Relation, Long>, JpaSpecificationExecutor<Relation> {
    List<Relation> findByMe(Long me);

    void deleteByMeAndFamily(Long me, Long family);

    void deleteByFamily(Long family);

    void deleteByMe(Long me);
}
