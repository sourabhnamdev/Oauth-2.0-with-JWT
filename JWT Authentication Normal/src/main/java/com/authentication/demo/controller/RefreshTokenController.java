package com.authentication.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.demo.Dto.RefreshTokenInfo;
import com.authentication.demo.config.JwtTokenUtil;
import com.authentication.demo.repository.UserRepository;
import com.authentication.demo.request.JwtRefreshRequest;
import com.authentication.demo.response.JwtResponse;
import com.authentication.demo.serviceImpl.JwtUserDetailsServiceImpl;
import com.authentication.demo.serviceImpl.RefreshTokenService;

@RestController
@CrossOrigin(origins = "*")
public class RefreshTokenController {
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private JwtUserDetailsServiceImpl userDetailsService;

	@Autowired
	private UserRepository userRepository;

	@Value("${jwt.secret}")
	private String secret;

	@RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
	public ResponseEntity<?> refreshAccessToken(@RequestBody JwtRefreshRequest refreshTokenRequest) {
		String refreshToken = refreshTokenRequest.getRefreshToken();
		UserDetails userDetails = loadUserFromRefreshToken(refreshToken);

		if (userDetails != null) {
			final String token = jwtTokenUtil.generateToken(userDetails);
			final String newRefreshToken = jwtTokenUtil.generateRefreshToken();
			final Long expiresIn = jwtTokenUtil.getExpirationInSecond(); // Implement your logic to get expiration time
			refreshTokenService.saveRefreshToken(userDetails.getUsername(), newRefreshToken,
					calculateRefreshTokenExpiration());

			return ResponseEntity.ok(new JwtResponse(token, newRefreshToken, expiresIn));

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
		}
	}

	private UserDetails loadUserFromRefreshToken(String refreshToken) {
		RefreshTokenInfo refreshTokenInfo = refreshTokenService.getRefreshTokenInfo(refreshToken);

		if (refreshTokenInfo != null && refreshTokenInfo.getRefreshToken().equals(refreshToken)
				&& !jwtTokenUtil.isTokenExpired(refreshToken)) {
			return userDetailsService.loadUserByUsername(refreshTokenInfo.getUsername());
		}
		return null;
	}

	private Date calculateRefreshTokenExpiration() {
		return new Date(System.currentTimeMillis() + jwtTokenUtil.REFRESH_TOKEN_VALIDITY * 1000);
	}

}
