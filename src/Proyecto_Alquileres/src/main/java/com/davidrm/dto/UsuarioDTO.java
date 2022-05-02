package com.davidrm.dto;

import java.io.Serializable;


public class UsuarioDTO implements Serializable {
	private Long id;
	private String dni;
	private String email;
	private String nombre;
	private String role;
    private String telefono;
	private String password;
		
	public UsuarioDTO() {}	

	public UsuarioDTO(Long id) {
		this.id = id;
	}

	public UsuarioDTO(String dni, String email, String nombre, String telefono, String password) {
		this.dni = dni;
		this.email = email;
		this.nombre = nombre;
		this.telefono = telefono;
		this.password = password;
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
