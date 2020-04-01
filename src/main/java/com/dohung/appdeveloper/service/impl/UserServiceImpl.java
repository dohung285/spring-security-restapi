package com.dohung.appdeveloper.service.impl;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dohung.appdeveloper.exceptions.UserServiceException;
import com.dohung.appdeveloper.io.entity.UserEntity;
import com.dohung.appdeveloper.io.repositories.UserRepository;
import com.dohung.appdeveloper.service.UserService;
import com.dohung.appdeveloper.ui.model.response.ErrorMessages;
import com.dohung.appdeveloper.ui.model.response.UserRest;
import com.dohung.appdeveloper.ui.shared.dto.UserDto;
import com.dohung.appdeveloper.ui.shared.util.Utils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {

		UserEntity storedEmailExist = userRepository.findByEmail(user.getEmail());
		if (storedEmailExist != null) {
			throw new RuntimeException("Email is exist!");
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		// set user id
		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		// bcrypt for password
		userEntity.setEncrytedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		UserEntity storedUserDetail = userRepository.save(userEntity);

		UserDto returnVaule = new UserDto();
		BeanUtils.copyProperties(storedUserDetail, returnVaule);

		return returnVaule;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}

		return new User(userEntity.getEmail(), userEntity.getEncrytedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {

		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String id) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(id);
		if (userEntity == null) {
			throw new UserServiceException(
					"User with ID : " + ErrorMessages.NO_RECORD_FOUND.getErrorMessage() + " not found");
		}
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDto updateUser(String id, UserDto user) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(id);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());

		UserEntity updateUserDetail = userRepository.save(userEntity);
		BeanUtils.copyProperties(updateUserDetail, returnValue);

		return returnValue;
	}

	@Override
	public void deleteUser(String id) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(id);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		userRepository.delete(userEntity);

	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnVales = new ArrayList<UserDto>();

		if (page > 0) {
			page = page - 1;
		}
		org.springframework.data.domain.Pageable pageableRequest = PageRequest.of(page, limit);
		Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();

		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnVales.add(userDto);
		}

		return returnVales;
	}

}
