package com.mms.repository;

import com.mms.model.SearchUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SearchUserRepository extends JpaRepository<SearchUser, Long>, JpaSpecificationExecutor<SearchUser> {
    SearchUser findBySecure(String secure);
}
