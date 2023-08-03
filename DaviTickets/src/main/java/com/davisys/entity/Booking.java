package com.davisys.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
