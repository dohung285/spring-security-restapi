package com.dohung.appdeveloper.io.repositories;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.dohung.appdeveloper.io.entity.UserEntity;
import com.dohung.appdeveloper.ui.shared.dto.UserDto;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);

	UserEntity findByUserId(String id);
	
	@Query(value = "SELECT * FROM  Users u WHERE u.EMAIL_VERIFICATION_STATUS='true' ",
			countQuery = "SELECT COUNT(*) FROM  Users u WHERE u.EMAIL_VERIFICATION_STATUS='true'",
			nativeQuery =true )
	Page<UserEntity> findAllUserWithConfrimedEmailAddress(
			org.springframework.data.domain.Pageable pageable
			);

}
