package com.davisys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
	
	Integer toltal_amount;
	
	@Temporal(TemporalType.DATE)
	Date payment_date = new Date();
	
}
