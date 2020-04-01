package com.dohung.appdeveloper.io.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dohung.appdeveloper.io.entity.UserEntity;
import com.dohung.appdeveloper.io.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId("1");
		userEntity.setFirstName("Do");
		userEntity.setLastName("Hung");
		userEntity.setEmailVerificationStatus(true);
		userEntity.setEmail("dohung12369874@gmail.com");
		userEntity.setEncrytedPassword("xxxyyyzzz");

		userRepository.save(userEntity);
	}

	@Test
	void testGetVerifiedUser() {
		
		org.springframework.data.domain.Pageable pageableRequest = PageRequest.of(0, 2);
		Page<UserEntity> pages = userRepository.findAllUserWithConfrimedEmailAddress(pageableRequest);
		assertNotNull(pages);

		List<UserEntity> list = pages.getContent();
		assertNotNull(list);
		assertTrue(list.size() == 1);

	}

}
