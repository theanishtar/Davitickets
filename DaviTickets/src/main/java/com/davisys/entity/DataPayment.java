package com.davisys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPayment {
	Showtime showtime;
	String full_name;
	String email;
	byte[] qrCodeGeneratorService;
}
