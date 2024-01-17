package com.authentication.demo.response;

import lombok.Data;

@Data
public class UserDeleteResponse {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
