package com.authentication.demo.constant;

public enum Const {
	MANUAL("Manual"), GOOGLE("Google");

	private final String displayName;

	Const(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
