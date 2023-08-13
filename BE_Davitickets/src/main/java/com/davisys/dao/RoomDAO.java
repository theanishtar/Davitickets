package com.davisys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Movie;
import com.davisys.entity.Room;

public interface RoomDAO extends JpaRepository<Room, Integer>{
	@Query(value = "SELECT * FROM Room WHERE room_id=:id", nativeQuery = true)
	public Room findIdRoom(int id);
	
	@Query(value = "SELECT seat.room_id FROM Seat WHERE seat_id=:id", nativeQuery = true)
	public int findIdRoomByIdSeat(int id);
}
