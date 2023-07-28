package com.davisys.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.davisys.entity.Showtime;

public interface ShowtimeDAO extends JpaRepository<Showtime, Integer>{

}
