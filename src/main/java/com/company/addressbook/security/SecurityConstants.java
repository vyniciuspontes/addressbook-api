package com.company.addressbook.security;

import java.security.Key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/signup";

	public static Key getSecretKey() {
		if (secretKey != null)
			return secretKey;

		secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		return secretKey;
	}

	private static Key secretKey;
}