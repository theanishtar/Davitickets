package com.davisys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Movie;
import com.davisys.entity.Users;

public interface MovieDAO extends JpaRepository<Movie, Integer>{
	@Query(value = "SELECT * FROM Movie WHERE movie_id=:id", nativeQuery = true)
	public Movie findIdMovie(int id);
	
	@Query(value = "SELECT * FROM Movie WHERE title_movie=:title", nativeQuery = true)
	public Movie findIdMovieByTitle(String title);
	
	
}
