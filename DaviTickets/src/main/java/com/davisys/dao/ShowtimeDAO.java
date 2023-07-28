package com.davisys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Showtime;

public interface ShowtimeDAO extends JpaRepository<Showtime, Integer>{

	@Query(value = "SELECT *FROM showtime where showdate=:date",nativeQuery = true)
	public List<Showtime>loadTimeMovie(String date);
	
	@Query(value = "SELECT *FROM showtime where showdate=:date AND movie_id=:id",nativeQuery = true)
	public List<Showtime>loadTimeMovieId(String date,int id);
	
	@Query(value = "SELECT * FROM Showtime WHERE showtime_id=:id", nativeQuery = true)
	public Showtime findIdShowtime(int id);
	
	@Query(value = "SELECT TOP 3 DATEPART(HOUR, CONVERT(DATETIME, start_time, 8)), COUNT(*) AS TIMES FROM showtime GROUP BY showtime.start_time ORDER BY TIMES DESC	", nativeQuery = true)
	public List<Object[]> getTop3GoldenHour();
}
