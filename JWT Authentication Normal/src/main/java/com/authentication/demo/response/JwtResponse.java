package com.authentication.demo.response;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final String refreshToken;
	private final Long expiresIn;

	public String getJwttoken() {
		return jwttoken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public JwtResponse(String jwttoken, String refreshToken, Long expiresIn) {
		super();
		this.jwttoken = jwttoken;
		this.refreshToken = refreshToken;
		this.expiresIn = expiresIn;
	}

}