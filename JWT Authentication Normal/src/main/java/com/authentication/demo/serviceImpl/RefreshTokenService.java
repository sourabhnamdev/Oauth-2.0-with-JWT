package com.authentication.demo.serviceImpl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authentication.demo.Dto.RefreshTokenInfo;
import com.authentication.demo.Dto.UserDto;
import com.authentication.demo.repository.UserRepository;

@Service
public class RefreshTokenService {
	@Autowired
    private UserRepository userRepository;

	public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
	
	// Save the refresh token in the database
    public void saveRefreshToken(String usernameOrEmail, String refreshToken, Date expirationDate) {
        UserDto user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpirationDate(expirationDate);
        userRepository.save(user);
    }
    
    // Retrieve the refresh token and expiration time from the database
    public RefreshTokenInfo getRefreshTokenInfo(String refreshToken) {
        UserDto user = userRepository.findByRefreshToken(refreshToken);
        return new RefreshTokenInfo(user.getRefreshToken(), user.getRefreshTokenExpirationDate(),user.getUsername());
    }
}
