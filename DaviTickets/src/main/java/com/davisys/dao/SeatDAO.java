package com.davisys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Seat;

public interface SeatDAO extends JpaRepository<Seat, Integer>{
	@Query(value = "SELECT * FROM Seat WHERE seat_id=:id", nativeQuery = true)
	public Seat findIdSeats(int id);

	@Query(value = "SELECT *FROM seat WHERE row_symbol=:row_symbol AND seat_num=:seat_num", nativeQuery = true)
	public Seat findIdSeatandsymbol(String row_symbol, int seat_num);
}
