package com.davisys.dao;

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


}
