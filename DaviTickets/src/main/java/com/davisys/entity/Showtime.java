package com.davisys.entity;


import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "showtime")
@NoArgsConstructor
@AllArgsConstructor
public class Showtime implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer showtime_id;
	
	@ManyToOne
	@JoinColumn(name = "movie_id")
	Movie movie;
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	Room room;
	
	@Temporal(TemporalType.DATE)
	Date showdate = new Date();
	
	/*
	@Temporal(TemporalType.TIME)
	Time start_time = new Time(0L);
	
	@Temporal(TemporalType.TIME)
	Time end_time = new Time(0L);
	*/
	Date start_time = new Date();
	Date end_time = new Date();
	Integer price;
	
	@JsonIgnore
	@OneToMany(mappedBy = "showtime")
	List<Booking> booking;
}
