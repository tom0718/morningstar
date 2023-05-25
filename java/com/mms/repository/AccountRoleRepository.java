package com.mms.repository;

import com.mms.model.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    void deleteByAccountId(String accountId);

    List<AccountRole> findByAccountIdOrderByDepartmentIdAsc(String accountId);

    AccountRole findBySeqNo(Long seqNo);
}
