package com.davisys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    int userid;
	
	String full_name;
	String gender;
	String user_password;
	String phone;
	String email;
	String profile_picture;
	boolean account_status;
	Boolean processed_by;
	@Temporal(TemporalType.DATE)
	Date user_birtday =new Date();
	@Temporal(TemporalType.DATE)
	Date user_dayjoin =new Date();
	String gg_id;
	Boolean user_role;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "users")
	List<Booking> booking;
	
	@JsonIgnore
	@OneToMany(mappedBy = "users")
	List<UserRoles> userRole;
	
}
