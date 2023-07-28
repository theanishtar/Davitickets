package com.davisys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGoogleCloud {
	private String iss;
	private String nbf;
	private String aud;
	private String sub;
	private String email;
	private String emailVerified;
	private String apz;
	private String name;
	private String picture;
	private String givenName;
	private String familyName;
	private String iat;
	private String exp;
}