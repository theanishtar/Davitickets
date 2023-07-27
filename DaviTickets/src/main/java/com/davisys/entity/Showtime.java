package com.davisys.entity;


import java.io.Serializable;
import java.sql.Time;
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
	
	@Temporal(TemporalType.DATE)
	Date showdate = new Date();
	
	@Temporal(TemporalType.TIME)
	Time start_time = new Time(0L);
	
	@Temporal(TemporalType.TIME)
	Time end_time = new Time(0L);
	
	Integer price;
	
	@JsonIgnore
	@OneToMany(mappedBy = "showtime")
	List<Booking> booking;
}
