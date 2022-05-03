package com.davidrm.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.davidrm.model.JPAUserDetails;
import com.davidrm.model.Usuario;

@Service
public class JPAUserDetailsService implements UserDetailsService {

	@Autowired
	UsuarioService usuarioSer;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = usuarioSer.findUsuarioByEmail(username);

		if (user == null) throw new UsernameNotFoundException("Not found:"+ username);
		
		JPAUserDetails userDetails = new JPAUserDetails(user);
		return userDetails;		
	}

}
