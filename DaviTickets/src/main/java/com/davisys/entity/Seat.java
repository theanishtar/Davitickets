package com.davisys.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
	String row_symboll;
	Integer seat_num;
	Boolean available;
	
	@JsonIgnore
	@OneToMany(mappedBy = "seat")
	List<Booking> booking;
}
