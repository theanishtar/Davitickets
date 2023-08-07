package com.davisys.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataBookingDetail {
	int userid;
	Movie movie;
	Showtime showtime;
	List<Booking> bookings ;
	List<Seat> seats;
}
