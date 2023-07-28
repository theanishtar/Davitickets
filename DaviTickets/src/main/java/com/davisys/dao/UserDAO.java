package com.davisys.dao;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.davisys.entity.Users;

import jakarta.transaction.Transactional;

public interface UserDAO extends JpaRepository<Users, Integer> {

	@Query(value = "SELECT * FROM users WHERE email=:email OR phone=:email", nativeQuery = true)
	public Users findEmaiAndPhonelUser(String email);
	
	@Query(value = "SELECT * FROM users WHERE email=:email ", nativeQuery = true)
	public Users findEmailUser(String email);


	@Query(value = "SELECT * FROM users WHERE email=:email OR phone=:phone", nativeQuery = true)
	public Users findPhoneAndEmailUser(String email, String phone);


}
