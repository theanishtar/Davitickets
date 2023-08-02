package com.davisys.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.context.annotation.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Users implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int userid;
	String full_name;
	String user_name;
	String gender;
	String user_password;
	String phone;
	String email;
	String profile_picture;
	boolean account_status;
	Boolean processed_by;
	@Temporal(TemporalType.DATE)
	Date user_birtday = new Date();
	@Temporal(TemporalType.DATE)
	Date user_dayjoin = new Date();
	String gg_id;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "users")
	List<Booking> booking;

//	@JsonManagedReference
	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Roles.class)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userid", referencedColumnName = "userid"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
//	List<Roles> roles = new ArrayList<Roles>();
	Set<Roles> roles = new HashSet<>();

//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Users {userid=").append(userid).append(", full_name=").append(full_name).append(", user_name=")
//				.append(user_name).append(", gender=").append(gender).append(", user_password=").append(user_password)
//				.append(", phone=").append(phone).append(", email=").append(email).append(", profile_picture=")
//				.append(profile_picture).append(", account_status=").append(account_status).append(", processed_by=")
//				.append(processed_by).append(", user_birtday=").append(user_birtday).append(", user_dayjoin=")
//				.append(user_dayjoin).append(", gg_id=").append(gg_id).append(", booking=").append(booking)
//				.append(", roles=");
//		if (roles != null) {
//			sb.append("{");
//			for (Roles r : roles) {
//				sb.append(r.getName()).append(", ");
//			}
//			sb.delete(sb.length() - 2, sb.length());
//			sb.append("}");
//		} else {
//			sb.append("null");
//		}
//		sb.append("}");
//		System.out.println("sb: "+sb);
//		return sb.toString();
//	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return this.getUser_password();
	}

	public String getUsername() {
		// TODO Auto-generated method stub
		return this.getUser_name();
	}

	public String[] getAuth() {
		List<String> roles = new ArrayList<>();
		for (Roles role : this.roles) {
			//System.out.println(role.getName().substring(5)); // "ROLE_" ->
			roles.add(role.getName().substring(5));
		}
		return roles.toArray(new String[0]);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            
            System.out.println("ROLE: " + role.getName());
        }
        return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
