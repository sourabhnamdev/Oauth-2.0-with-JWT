
package com.authentication.demo.serviceImpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.authentication.demo.Dto.UserDto;
import com.authentication.demo.repository.UserRepository;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		UserDto user = userRepository.findByUsername(username);
//		if (ObjectUtils.isEmpty(user)) {
//			throw new UsernameNotFoundException("User not found with username: " + username);
//		}
//		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
//				new ArrayList<>());
//	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		UserDto user;
		if (usernameOrEmail.contains("@")) {

			user = userRepository.findByEmail(usernameOrEmail);
			if (ObjectUtils.isEmpty(user)) {
				throw new UsernameNotFoundException("User not found with this email : " + usernameOrEmail);
			}
			return new org.springframework.security.core.userdetails.User(user.getEmail(), "", new ArrayList<>());
		} else {

			user = userRepository.findByUsername(usernameOrEmail);
			if (ObjectUtils.isEmpty(user)) {
				throw new UsernameNotFoundException("User not found with this username : " + usernameOrEmail);
			}
			return new org.springframework.security.core.userdetails.User(user.getUsername(),
					(StringUtils.isEmpty(user.getPassword()) ? "" : user.getPassword()), new ArrayList<>());
		}

	}
}
