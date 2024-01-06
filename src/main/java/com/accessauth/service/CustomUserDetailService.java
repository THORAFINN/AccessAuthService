package com.accessauth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.accessauth.model.db.Login;
import com.accessauth.model.db.User;
import com.accessauth.modeldto.CustomUserDetail;
import com.accessauth.repository.LoginRepository;
import com.accessauth.repository.UserRepository;

@Component
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LoginRepository loginRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CustomUserDetail customUserDetail = null;
		Optional<Login> optionalUsername = this.loginRepo.findByUsername(username);
		if (optionalUsername.isPresent()) {
			Login login = optionalUsername.get();
			Optional<User> optionalUser = this.userRepository.findByLogin_id(login.getId());
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				customUserDetail = new CustomUserDetail();
				customUserDetail.setUser(user);
				customUserDetail.setLogin();
			} else {
				throw new UsernameNotFoundException("Invalid user name no enry found in user "+username);
			}
		} else {
			throw new UsernameNotFoundException("Invalid user name "+username);
		}			
		return customUserDetail;
	}

}
