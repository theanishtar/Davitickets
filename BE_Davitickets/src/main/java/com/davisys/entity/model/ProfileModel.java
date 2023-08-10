package com.davisys.entity.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileModel {
	String fullname;
	String email;
	String pic;
	String phone;
	Date birtday = new Date();
	String password;
	String gender;
	
}
