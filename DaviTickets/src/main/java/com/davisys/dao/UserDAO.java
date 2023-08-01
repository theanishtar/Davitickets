package com.davisys.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Users;

public interface UserDAO extends JpaRepository<Users, Integer>{
	@Query(value = "SELECT CONVERT( VARCHAR ,COUNT(userid)) FROM users WHERE MONTH(user_dayjoin) =:month", nativeQuery = true)
	public String userStatisticsMonth(int month);
	
	@Query(value = "SELECT * FROM Users WHERE userid=:id", nativeQuery = true)
	public Users findIdUsers(int id);
	
}
