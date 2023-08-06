package com.davisys.config;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {
	private static final long serialVersionUID = 8544329907338151549L;
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000; // 5 Hours
	// public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 1000; // 5 Minutes
	@Value("${jwt.secret}")
	private String secret;

	public String getUsernameFromToken(String token) {
		System.out.println(secret);
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public Boolean validateToken(String token, String usernameFromToken) {
		final String username = getUsernameFromToken(token);
		return (username.equals(usernameFromToken) && !isTokenExpired(token));
	}

}
