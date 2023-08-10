package com.davisys.dao;

import java.awt.print.Book;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Booking;

public interface BookingDAO extends JpaRepository<Booking, Integer> {

	@Query(value = "SELECT *FROM booking WHERE showtime_id=:idShowtime", nativeQuery = true)
	public List<Booking> findBookingShowtime(int idShowtime);
}
