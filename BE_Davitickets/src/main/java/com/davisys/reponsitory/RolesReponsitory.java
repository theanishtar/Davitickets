package com.davisys.reponsitory;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davisys.entity.Roles;

@Repository
public interface RolesReponsitory extends JpaRepository<Roles, Long> {
	Roles findByName(String name);
	//Roles findByrole_name(String role_name);
}
