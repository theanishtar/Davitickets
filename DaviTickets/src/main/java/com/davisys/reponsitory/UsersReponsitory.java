package com.davisys.reponsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.davisys.entity.Users;

@Repository
public interface UsersReponsitory extends JpaRepository<Users, Long>{

	Optional<Users>findByEmail(String email);
	//Optional<Users>findByIdOptional(Long id);
	//Optional<Users>findByUser_name(String username);
}
