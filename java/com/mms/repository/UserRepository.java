package com.mms.repository;

import com.mms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findBySeqNo(Long seqNo);

    User findBySecure(String secure);

    @Query("select u.thumbnail from user u where u.seqNo = :id")
    String getThumbnail(@Param("id") Long id);
}
