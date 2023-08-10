package com.davisys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusLogin {
	Users user;
	String sesionId;
	boolean role;
//	String status;
}
