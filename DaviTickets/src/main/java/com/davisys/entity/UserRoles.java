package com.davisys.entity;


import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "userRoles")
@NoArgsConstructor
@AllArgsConstructor
public class UserRoles implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	Users users;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	Roles roles;
	
	
}
