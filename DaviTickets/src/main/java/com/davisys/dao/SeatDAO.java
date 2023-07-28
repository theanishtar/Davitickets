package com.davisys.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.davisys.entity.Seat;

public interface SeatDAO extends JpaRepository<Seat, Integer>{

}
