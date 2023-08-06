package com.davisys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataBooking {
	private String userEmail;
	private int idMovie;
	private int idShowTime;
//	private String cookie;
}
