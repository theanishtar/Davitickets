package com.davisys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "movie")
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    int movie_id;
	String title_movie;
	
	@Temporal(TemporalType.DATE)
	Date release_date = new Date();
	
	int duration;
	String genre;
	String poster;
	

	float rating;
	
	String film_director;
	String description_movie;
	
	@JsonIgnore
	@OneToMany(mappedBy = "movie")
	List<Showtime> showtime;
}
