package com.davisys.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.davisys.entity.Roles;

public interface RoleDAO extends JpaRepository<Roles, Integer>{

}
