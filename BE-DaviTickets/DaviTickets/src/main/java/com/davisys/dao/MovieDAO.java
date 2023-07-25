package com.davisys.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.davisys.entity.Movie;

public interface MovieDAO extends JpaRepository<Movie, Integer>{

}
