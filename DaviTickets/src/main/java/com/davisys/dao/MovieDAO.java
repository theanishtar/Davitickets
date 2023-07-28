package com.davisys.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Movie;

public interface MovieDAO extends JpaRepository<Movie, Integer>{

	@Query(value = "SELECT a.movie_id,a.title_movie,a.duration,a.poster,b.showtime_id,b.start_time,b.showdate FROM movie a,showtime b WHERE a.movie_id =b.movie_id AND b.showdate =:date", nativeQuery = true)
	public List<Object[]>loadAllMovie(String date);
	
	@Query(value = "SELECT a.showdate, a.start_time FROM showtime a WHERE a.movie_id =:id AND a.showdate =:date",nativeQuery = true)
	public List<Object[]>findDate(int id,String date);
	
	@Query(value = "SELECT * FROM Movie WHERE movie_id=:id", nativeQuery = true)
	public Movie findIdMovie(int id);
	
	@Query(value = "SELECT * FROM Movie WHERE title_movie=:title", nativeQuery = true)
	public Movie findIdMovieByTitle(String title);
}
