package com.authentication.demo.Dto;

import java.util.Date;

public class RefreshTokenInfo {

	private final String refreshToken;
	private final Date expirationDate;
	private final String username;

	public RefreshTokenInfo(String refreshToken, Date expirationDate, String username) {
		this.refreshToken = refreshToken;
		this.expirationDate = expirationDate;
		this.username = username;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public String getUsername() {
		return username;
	}
}
