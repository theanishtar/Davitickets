package com.davisys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Users;

public interface UserDAO extends JpaRepository<Users, Integer> {

	@Query(value = "SELECT * FROM users WHERE email=:email OR phone=:email", nativeQuery = true)
	public Users findEmaiAndPhonelUser(String email);
	
	@Query(value = "SELECT * FROM users WHERE email=:email ", nativeQuery = true)
	public Users findEmailUser(String email);


	@Query(value = "SELECT * FROM users WHERE email=:email OR phone=:phone", nativeQuery = true)
	public Users findPhoneAndEmailUser(String email, String phone);

	@Query(value = "SELECT CONVERT( VARCHAR ,COUNT(userid)) FROM users WHERE MONTH(user_dayjoin) =:month", nativeQuery = true)
	public String userStatisticsMonth(int month);
	
	@Query(value = "SELECT * FROM Users WHERE userid=:id", nativeQuery = true)
	public Users findIdUsers(int id);
	

	@Query(value = "SELECT user_role.role_id FROM user_role WHERE userid =:id", nativeQuery = true)
	public String findRoleUser(int id);
	
	@Query(value = "SELECT movie.title_movie, room.room_name, movie.duration, showtime.showdate, showtime.start_time, showtime.end_time, SUM(payment.total_amount) AS TOLAL FROM\r\n"
			+ "booking INNER JOIN showtime ON showtime.showtime_id = booking.showtime_id\r\n"
			+ "INNER JOIN movie ON movie.movie_id = showtime.movie_id\r\n"
			+ "INNER JOIN room ON room.room_id = showtime.room_id\r\n"
			+ "INNER JOIN payment ON payment.booking_id = booking.booking_id WHERE booking.userid = :id\r\n"
			+ "GROUP BY movie.title_movie, room.room_name, movie.duration, showtime.showdate, showtime.start_time, showtime.end_time", nativeQuery = true)
	public List<Object[]> loadDataHistory(int id);
}
