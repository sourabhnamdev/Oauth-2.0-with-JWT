package com.authentication.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authentication.demo.Dto.UserDto;

@Repository
public interface UserRepository extends JpaRepository<UserDto, Integer> {

	UserDto findByUsername(String userName);
	UserDto findByUsernameOrEmail(String userName,String email);
	UserDto findByEmail(String email);
	UserDto findByRefreshToken(String refreshToken);

}
