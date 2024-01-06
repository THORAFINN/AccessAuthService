package com.accessauth.modeldto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.accessauth.model.db.Login;
import com.accessauth.model.db.User;


public class CustomUserDetail implements UserDetails {
	
	
	private User user;
	
	private Login login;	
	
	public Login getLogin() {
		return login;
	}

	public void setLogin() {
		this.login = this.user.getLogin();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ROLE_"+user.getRid()));
		return roles;
	}

	@Override
	public String getPassword() {
		return login.getPassword();
	}

	@Override
	public String getUsername() {
		return login.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
