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
		
		//Obtengo el usuario
		Usuario user = usuarioSer.findUsuarioByEmail(username);
		
		
		//Si el usuario no existe debo devolver una excepción
		if (user == null) {
			throw new UsernameNotFoundException("Not found:"+ username);
		}
		
		//Adapto la información del usuario al UserDetails que es lo que debe devolver el método
		JPAUserDetails userDetails = new JPAUserDetails(user);
		return userDetails;
		
	}


}
