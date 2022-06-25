package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	//valida actualmente el el usuario existente
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		if (userName.equals("John")) {
			return new User("John", "secret", new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("Usuario no existe");
		}
	}

}
