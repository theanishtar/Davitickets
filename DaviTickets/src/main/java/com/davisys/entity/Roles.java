package com.davisys.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Roles implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer role_id;

	String name;
	String role_des;

//	@JsonIgnore
//	@OneToMany(mappedBy = "roles")
//	List<UserRoles> userRole;
	
//	@JsonManagedReference
	@ManyToMany( mappedBy = "roles",targetEntity = Users.class)
	List<Users>user ;
//	@Fetch(value = FetchMode.SELECT)
//	Set<Users> user = new HashSet<>();

	public Roles(String name) {
		this.name = name;
	}
}
