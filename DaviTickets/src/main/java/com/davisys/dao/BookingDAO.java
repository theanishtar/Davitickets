package com.davisys.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.davisys.entity.Booking;

public interface BookingDAO extends JpaRepository<Booking, Integer>{

}

