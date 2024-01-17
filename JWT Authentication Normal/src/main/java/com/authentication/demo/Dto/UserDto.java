package com.authentication.demo.Dto;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class UserDto {
	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_name")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@JsonIgnore
	@Column(name = "refresh_token")
	private String refreshToken;

	@JsonIgnore
	@Column(name = "refresh_token_expiration_date")
	private Date refreshTokenExpirationDate;

	// New fields from the provided JSON
	@Column(name = "access_token_hash ")
	private String accessTokenHash;

	@Column(name = "subject")
	private String subject;

	@Column(name = "is_email_verified")
	private Boolean isEmailVerified;

	@Column(name = "issuer")
	private URL issuer;

	@Column(name = "given_name")
	private String givenName;

	@Column(name = "locale")
	private String locale;

	@Column(name = "nonce")
	private String nonce;

	@Column(name = "picture")
	private String picture;

	@Column(name = "aud")
	private ArrayList<String> aud;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "token_expiration_Date")
	private Instant tokenExpirationDate;

	@Column(name = "family_name")
	private String familyName;

	@Column(name = "issued_at")
	private Instant IssuedAt;

	@Column(name = "identity")
	private String identity;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getRefreshTokenExpirationDate() {
		return refreshTokenExpirationDate;
	}

	public void setRefreshTokenExpirationDate(Date refreshTokenExpirationDate) {
		this.refreshTokenExpirationDate = refreshTokenExpirationDate;
	}

	public String getAccessTokenHash() {
		return accessTokenHash;
	}

	public void setAccessTokenHash(String accessTokenHash) {
		this.accessTokenHash = accessTokenHash;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Boolean getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(Boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public URL getIssuer() {
		return issuer;
	}

	public void setIssuer(URL issuer) {
		this.issuer = issuer;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public ArrayList<String> getAud() {
		return aud;
	}

	public void setAud(ArrayList<String> aud) {
		this.aud = aud;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Instant getTokenExpirationDate() {
		return tokenExpirationDate;
	}

	public void setTokenExpirationDate(Instant tokenExpirationDate) {
		this.tokenExpirationDate = tokenExpirationDate;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public Instant getIssuedAt() {
		return IssuedAt;
	}

	public void setIssuedAt(Instant issuedAt) {
		IssuedAt = issuedAt;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", refreshToken=" + refreshToken
				+ ", refreshTokenExpirationDate=" + refreshTokenExpirationDate + ", accessTokenHash=" + accessTokenHash
				+ ", subject=" + subject + ", isEmailVerified=" + isEmailVerified + ", issuer=" + issuer
				+ ", givenName=" + givenName + ", locale=" + locale + ", nonce=" + nonce + ", picture=" + picture
				+ ", aud=" + aud + ", clientId=" + clientId + ", tokenExpirationDate=" + tokenExpirationDate
				+ ", familyName=" + familyName + ", IssuedAt=" + IssuedAt + ", identity=" + identity + "]";
	}

}
