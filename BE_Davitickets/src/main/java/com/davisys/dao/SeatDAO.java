package com.davisys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Seat;
import com.davisys.entity.Showtime;

public interface SeatDAO extends JpaRepository<Seat, Integer>{
	@Query(value = "SELECT * FROM Seat WHERE seat_id=:id", nativeQuery = true)
	public Seat findIdSeats(int id);

	@Query(value = "SELECT *FROM seat WHERE row_symbol=:row_symbol AND seat_num=:seat_num  AND room_id=:roomId", nativeQuery = true)
	public Seat findIdSeatandsymbol(String row_symbol, int seat_num,int roomId);
	
	@Query(value = "SELECT * FROM Seat WHERE seat.room_id=:id", nativeQuery = true)
	public List<Seat> getListSeatByRoom(int id);
	
}
