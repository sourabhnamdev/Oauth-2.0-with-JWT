package com.authentication.demo.controller;

import java.util.Date;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.demo.Dto.UserDto;
import com.authentication.demo.config.JwtTokenUtil;
import com.authentication.demo.exception.InvalidCredentialsException;
import com.authentication.demo.repository.UserRepository;
import com.authentication.demo.request.JwtRequest;
import com.authentication.demo.request.RequestByEmail;
import com.authentication.demo.response.JwtResponse;
import com.authentication.demo.serviceImpl.JwtUserDetailsServiceImpl;
import com.authentication.demo.serviceImpl.RefreshTokenService;

@RestController
@CrossOrigin(origins = "*")
public class JwtAuthenticationController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private JwtUserDetailsServiceImpl userDetailsService;

	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Value("${jwt.secret}")
	private String secret;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final String refreshToken = jwtTokenUtil.generateRefreshToken();
		final Long expiresIn = jwtTokenUtil.getExpirationInSecond(); // Implement your logic to get expiration time
		refreshTokenService.saveRefreshToken(authenticationRequest.getUsername(), refreshToken,
				calculateRefreshTokenExpiration());
		return ResponseEntity.ok(new JwtResponse(token, refreshToken, expiresIn));
	}

	@RequestMapping(value = "/authenticate-by-email", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody RequestByEmail authenticationRequest)
			throws Exception {

		authenticateByEmail(authenticationRequest.getEmail());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final String refreshToken = jwtTokenUtil.generateRefreshToken();
		final Long expiresIn = jwtTokenUtil.getExpirationInSecond(); // Implement your logic to get expiration time
		refreshTokenService.saveRefreshToken(authenticationRequest.getEmail(), refreshToken,
				calculateRefreshTokenExpiration());
		return ResponseEntity.ok(new JwtResponse(token, refreshToken, expiresIn));
	}

	private void authenticate(String username, String password) throws Exception {

		try {
			UserDto user = userRepository.findByUsername(username);
			if (ObjectUtils.isEmpty(user)) {
				throw new UsernameNotFoundException("User not found with username: " + username);
			}

			else {
				StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
				encryptor.setPassword(secret);
				StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
				decryptor.setPassword(secret);
				String decryptedText = decryptor.decrypt(user.getPassword());
			
				if (username.equals(user.getUsername()) && password.equals(decryptedText)) {
				} else {
					throw new InvalidCredentialsException("INVALID_CREDENTIALS");
				}

			}

		} catch (DisabledException e) {
			throw new InvalidCredentialsException("INVALID_CREDENTIALS");
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException("INVALID_CREDENTIALS");
		}
	}

	private void authenticateByEmail(String email) throws InvalidCredentialsException {
		try {
			UserDto user = userRepository.findByEmail(email);
			if (ObjectUtils.isEmpty(user)) {
				throw new UsernameNotFoundException("User not found with Email: " + email);
			}

			else {
				StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
				encryptor.setPassword(secret);
				StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
				decryptor.setPassword(secret);
				// String decryptedText = decryptor.decrypt(user.getPassword());
				if (email.equals(user.getEmail())) {
				} else {
					throw new InvalidCredentialsException("INVALID_CREDENTIALS");
				}

			}

		} catch (DisabledException e) {
			throw new InvalidCredentialsException("INVALID_CREDENTIALS");
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException("INVALID_CREDENTIALS");
		}

	}

	private Date calculateRefreshTokenExpiration() {
		return new Date(System.currentTimeMillis() + jwtTokenUtil.REFRESH_TOKEN_VALIDITY * 1000);
	}

}
