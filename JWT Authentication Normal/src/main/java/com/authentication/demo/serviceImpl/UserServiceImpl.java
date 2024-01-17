package com.authentication.demo.serviceImpl;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.authentication.demo.Dto.UserDto;
import com.authentication.demo.constant.Const;
import com.authentication.demo.repository.UserRepository;
import com.authentication.demo.response.UserDeleteResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl {

	@Autowired
	private UserRepository userRepository;

	@Value("${jwt.secret}")
	private String secret;

	public UserDto save(UserDto userDto) throws MalformedURLException {

		if (StringUtils.isEmpty(userDto.getIdentity())) {
			// user comes from manually with username and password

			String encryptPassword = encryptPassword(userDto.getPassword());// calling encrypted method
			userDto.setIdentity(Const.MANUAL.getDisplayName());
			userDto.setPassword(encryptPassword);
			UserDto user = userRepository.findByUsername(userDto.getUsername());

			if (ObjectUtils.isEmpty(user)) {
				// new users manually
				userRepository.save(userDto);
			} else if ((!ObjectUtils.isEmpty(user)) && StringUtils.isEmpty(user.getPassword())) {
				// existing user with updating few details
				user.setPassword(userDto.getPassword());
				user.setFirstName(userDto.getFirstName());
				user.setLastName(userDto.getLastName());
				userRepository.save(user);
			}
		}

		else {
			UserDto user = userRepository.findByEmail(userDto.getEmail());
			if (ObjectUtils.isEmpty(user)) {
				userRepository.save(userDto);
			} // If u want to update user from google to do here.....
		}

		return userDto;

	}

	public List<UserDto> getUsers() {
		return userRepository.findAll();

	}

	public UserDeleteResponse deleteUser(int id) {
		Optional<UserDto> existingUser = userRepository.findById(id);
		if (existingUser.isPresent()) {
			UserDeleteResponse message = new UserDeleteResponse();
			userRepository.deleteById(id);
			message.setMessage("User Deleted");
			return message;
		} else {
			throw new RuntimeException("user with this id " + id + " not found");
		}

	}

	private String encryptPassword(String password) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(secret);
		return encryptor.encrypt(password);
	}

}
