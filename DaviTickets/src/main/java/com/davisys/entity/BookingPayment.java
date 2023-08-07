package com.davisys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingPayment {
	String money;
	String showtimeId;
	String seat;
	String userEmail;
	String qrCode;
}
