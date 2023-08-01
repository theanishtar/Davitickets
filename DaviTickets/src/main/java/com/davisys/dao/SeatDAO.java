package com.davisys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Seat;
import com.davisys.entity.Users;

public interface SeatDAO extends JpaRepository<Seat, Integer>{
	@Query(value = "SELECT * FROM Seat WHERE seat_id=:id", nativeQuery = true)
	public Seat findIdSeats(int id);
	
}
