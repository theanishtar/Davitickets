package com.davisys.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.davisys.entity.Payment;

public interface PaymentDAO extends JpaRepository<Payment, Integer>{

}

