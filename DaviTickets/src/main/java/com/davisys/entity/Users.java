package com.davisys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userid;
	
	String full_name;
	String gender;
	String user_password;
	String phone;
	String email;
	String profile_picture;
	Boolean account_status;
	Boolean processed_by;
	@Temporal(TemporalType.DATE)
	Date user_birtday = new Date();
	@Temporal(TemporalType.DATE)
	Date user_dayjoin = new Date();
	String gg_id;
	Boolean user_role;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "users")
	List<Booking> booking;
}
