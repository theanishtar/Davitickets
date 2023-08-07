package com.davisys.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "seat")
@NoArgsConstructor
@AllArgsConstructor
public class Seat implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer seat_id;
	
	Boolean vip;
	String row_symbol;
	Integer seat_num;
	Boolean available;
	
	@JsonIgnore
	@OneToMany(mappedBy = "seat")
	List<Booking> booking;
}
