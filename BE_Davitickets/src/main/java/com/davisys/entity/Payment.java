package com.davisys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer payment_id;
	
	@ManyToOne
	@JoinColumn(name = "booking_id")
	Booking booking;
	
	Integer total_amount;
	
	@Temporal(TemporalType.DATE)
	Date payment_date = new Date();
	
}
