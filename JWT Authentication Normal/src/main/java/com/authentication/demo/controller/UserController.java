package com.authentication.demo.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.demo.Dto.UserDto;
import com.authentication.demo.config.JwtTokenUtil;
import com.authentication.demo.constant.Const;
import com.authentication.demo.repository.UserRepository;
import com.authentication.demo.response.JwtResponse;
import com.authentication.demo.response.UserDeleteResponse;
import com.authentication.demo.serviceImpl.JwtUserDetailsServiceImpl;
import com.authentication.demo.serviceImpl.RefreshTokenService;
import com.authentication.demo.serviceImpl.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
public class UserController {

	/****
	 * @author sourabh namdev
	 * @category for user
	 */

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private JwtUserDetailsServiceImpl userDetailsService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	UserServiceImpl userServiceImpl;

	@GetMapping("/hello")
	public ResponseEntity<String> firstPage() {
		return ResponseEntity.ok("Hello World");
	}

	@PostMapping("/user/save")
	public UserDto save(@RequestBody UserDto userDto) throws MalformedURLException {
		userServiceImpl.save(userDto);
		return userDto;
	}

	@GetMapping("/getAll/users")
	public List<UserDto> getUsers() {
		List<UserDto> users = userServiceImpl.getUsers();
		return users;
	}

	@GetMapping("/user/delete/{id}")
	public UserDeleteResponse deleteUser(@PathVariable Integer id) {
		return userServiceImpl.deleteUser(id);
	}

	@GetMapping("/user")
	public ResponseEntity<?> getUser(@AuthenticationPrincipal OAuth2User user) throws MalformedURLException {
		Map<String, Object> attributes = user.getAttributes();

		UserDto userDetails = createUserDTOFromGoogleSide(attributes);// Create a UserDto with the
																		// extracted information
		userServiceImpl.save(userDetails); // Calling Save Method to Saving User Details
		
		return generateTokenInternalMethod(userDetails); // generating Token and refresh token

	}
	
	private UserDto createUserDTOFromGoogleSide(Map<String, Object> attributes) {
		UserDto userDto = new UserDto();
		userDto.setSubject((String) attributes.get("sub"));
		userDto.setEmail((String) attributes.get("email"));
		userDto.setGivenName((String) attributes.get("givenName"));
		userDto.setFamilyName((String) attributes.get("familyName"));
		// ... set more fields as needed

//		// Set additional fields from the provided JSON
		userDto.setAccessTokenHash((String) attributes.get("at_hash"));
		userDto.setIsEmailVerified((Boolean) attributes.get("email_verified"));
		userDto.setIssuer((URL) attributes.get("iss"));
		userDto.setLocale((String) attributes.get("locale"));
		userDto.setNonce((String) attributes.get("nonce"));
		userDto.setPicture((String) attributes.get("picture"));
		userDto.setAud((ArrayList<String>) attributes.get("aud"));
		userDto.setClientId((String) attributes.get("azp"));
		userDto.setUsername((String) attributes.get("name"));
	//	userDto.setTokenExpirationDate((Instant) attributes.get("exp"));
		userDto.setFamilyName((String) attributes.get("family_name"));
		userDto.setIssuedAt((Instant) attributes.get("iat"));
		userDto.setRefreshToken((String) attributes.get("refresh_token"));
		userDto.setTokenExpirationDate((Instant) attributes.get("expiration_date"));
		userDto.setIdentity(Const.GOOGLE.getDisplayName());
		return userDto;
	}

	private ResponseEntity<?> generateTokenInternalMethod(UserDto userDetails) {
		final UserDetails newUserDetails = userDetailsService.loadUserByUsername(userDetails.getEmail());
		final String token = jwtTokenUtil.generateToken(newUserDetails);
		final String refreshToken = jwtTokenUtil.generateRefreshToken();
		final Long expiresIn = jwtTokenUtil.getExpirationInSecond();

		
		// Saving Refresh Token In DB
		saveRefreshTokenDetails(userDetails.getEmail(), refreshToken, calculateRefreshTokenExpiration());

		return ResponseEntity.ok(new JwtResponse(token, refreshToken, expiresIn));

	}

	private void saveRefreshTokenDetails(String username, String refreshToken, Date calculateRefreshTokenExpiration) {
		refreshTokenService.saveRefreshToken(username, refreshToken, calculateRefreshTokenExpiration());
	}

	private Date calculateRefreshTokenExpiration() {
		return new Date(System.currentTimeMillis() + jwtTokenUtil.REFRESH_TOKEN_VALIDITY * 1000);
	}
}
