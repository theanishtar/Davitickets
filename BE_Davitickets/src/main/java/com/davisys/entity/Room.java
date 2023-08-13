package com.davisys.entity;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "room")
@NoArgsConstructor
@AllArgsConstructor
public class Room {
	@Id
    Integer room_id;
	
	String room_name;
	
	@JsonIgnore
	@OneToMany(mappedBy = "room")
	List<Showtime> showtime;
	
	@JsonIgnore
	@OneToMany(mappedBy = "room")
	List<Seat> seat;
}
