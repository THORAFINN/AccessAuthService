package com.accessauth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accessauth.model.db.Login;

public interface LoginRepository extends JpaRepository<Login, Integer> {
	
	/**
	 * 
	 * <p>
	 * @param username
	 * @param password
	 * @return
	 */
	public Optional<Login> findByUsernameAndPassword (String username, String password);
	
	/**
	 * 
	 * <p>
	 * @param username
	 * @return
	 */
	public Optional<Login> findByUsername (String username);

}
