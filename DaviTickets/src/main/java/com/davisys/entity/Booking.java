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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "booking")
@NoArgsConstructor
@AllArgsConstructor
public class Booking implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer booking_id;
	
	@ManyToOne
	@JoinColumn(name = "showtime_id")
	Showtime showtime;
	
	@ManyToOne
	@JoinColumn(name = "seat_id")
	Seat seat;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	Users users;
	
	@Temporal(TemporalType.DATE)
	Date booking_date = new Date();
	
	@JsonIgnore
	@OneToMany(mappedBy = "booking")
	List<Payment> payment;
	
}
