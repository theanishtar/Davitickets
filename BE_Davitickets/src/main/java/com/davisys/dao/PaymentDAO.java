package com.davisys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.davisys.entity.Earnings;
import com.davisys.entity.Movie;
import com.davisys.entity.Payment;

public interface PaymentDAO extends JpaRepository<Payment, Integer>{
	
	@Query(value = "SELECT CONVERT( VARCHAR ,SUM(total_amount)) FROM payment WHERE DAY(payment_date) =:day", nativeQuery = true)
	public String revenueStatisticsDay(int day);
	
	@Query(value = "SELECT CONVERT( VARCHAR ,SUM(total_amount)) FROM payment WHERE MONTH(payment_date) =:month", nativeQuery = true)
	public String revenueStatisticsMonth(int month);
	
	@Query(value = "SELECT CONVERT( VARCHAR ,MAX(total_amount)) FROM payment WHERE MONTH(payment_date) =:month", nativeQuery = true)
	public String revenueStatisticsMaxDay(int month);
	

	@Query(value = "SELECT MONTH(payment_date) AS MONTH, SUM(total_amount) FROM payment GROUP BY MONTH(payment_date) ORDER BY MONTH(payment_date) ASC", nativeQuery = true)
	public List<Object[]> getlistEarnings();
	
	
}

