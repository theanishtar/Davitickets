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

	@Query(value = "SELECT CONVERT( VARCHAR ,COUNT(userid)) FROM users WHERE MONTH(user_dayjoin) =:month", nativeQuery = true)
	public String userStatisticsMonth(int month);
	
	@Query(value = "SELECT * FROM Users WHERE userid=:id", nativeQuery = true)
	public Users findIdUsers(int id);
	

	@Query(value = "SELECT user_role.role_id FROM user_role WHERE userid =:id", nativeQuery = true)
	public String findRoleUser(int id);
}
