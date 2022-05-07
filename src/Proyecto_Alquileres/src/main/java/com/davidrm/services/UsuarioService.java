package com.davidrm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidrm.model.Usuario;
import com.davidrm.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	UsuarioRepository usuarioRepo;
	
	public UsuarioService() {}

	public Usuario findUsuarioById(Long id) {		
		return usuarioRepo.findById(id).orElse(null);
	}
	
	public List<Usuario> getAllUsuarios() {
		return usuarioRepo.findAll(); 	
	}
	
	public Usuario findUsuarioByDni(String dni) {		
		return usuarioRepo.findByDni(dni).orElse(null);
	}
	
	public Usuario findUsuarioByEmail(String email) {		
		return usuarioRepo.findByEmail(email).orElse(null);
	}
	
	public Usuario insertarUsuario(Usuario usuario) {
		if (usuario != null && findUsuarioByDni(usuario.getDni()) == null && findUsuarioByEmail(usuario.getEmail()) == null)
			return usuarioRepo.save(usuario);			
		else
			return null;
	}

	public Usuario actualizarUsuario(Usuario usuario) {
		if (usuario == null || usuario.getId() == null)
			return null;
		else
			return usuarioRepo.save(usuario); 	
	}
	
	public boolean borrarUsuario(Usuario usuario) {
		if (usuario != null && usuario.getId() != null) {
			usuarioRepo.delete(usuario);
			return true;
		} else
			return false;
	}
}
