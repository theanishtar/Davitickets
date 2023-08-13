package com.davisys.dao;

import java.awt.print.Book;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Booking;

public interface BookingDAO extends JpaRepository<Booking, Integer> {

	@Query(value = "SELECT *FROM booking WHERE showtime_id=:idShowtime", nativeQuery = true)
	public List<Booking> findBookingShowtime(int idShowtime);

	@Query(value = "SELECT * FROM booking WHERE userid=:userid AND showtime_id =:showtime_id AND booking_date=:booking_date  AND DATEPART(HOUR, CONVERT(DATETIME, booking_time, 8)) =:hour AND DATEPART(MINUTE, CONVERT(DATETIME, booking_time, 8)) =:minute", nativeQuery = true)
	public List<Booking> findBookingUser(int userid, int showtime_id, String booking_date, int hour, int minute);
}
