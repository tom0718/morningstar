package com.mms.repository;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mms.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account>{
	
	
	@Transactional
	@Modifying
	@Query("update account a set a.fail = :fail where a.id = :id")
	void updateFail(@Param("id") String id, @Param("fail") Integer fail);

	@Transactional
	@Modifying
	@Query("update account a set a.status = :status, a.fail = :fail where a.id = :id")
	void updateStatus(@Param("id") String id, @Param("status") Integer status, @Param("fail") Integer fail);
	
	@Query("select a from account a where a.id =:id and a.status = :status")
	Optional<Account> findByIdAndStatus(@Param("id") String id, @Param("status") Integer status);
	
	Optional<Account> findByIdAndDeleted(String id, boolean delete);

	@Transactional
	@Modifying
	@Query("update account c set c.deleted = true where c.id = :id")
	Integer updateDeleteFlag(@Param("id") String id);


	Boolean existsByCode(String code);

	@Transactional
	@Modifying
	@Query("update account c set c.last = :last, c.fail = 0 where c.id = :id")
	void updateLoginDate(@Param("id") String id, @Param("last") Date last);

	Optional<Account> findByIdAndStatusAndFailLessThan(String id, int status, int fail);

	Account findByDeletedAndAuthAndCode(boolean deleted, String auth, String code);

	Account findByEmailAndDeleted(String email, boolean deleted);

	Account findByDeletedAndId(boolean deleted, String id);

    Boolean existsByEmail(String email);

	Account findByAuth(String auth);

	Boolean existsByEmailAndIdNot(String email, String id);
}
