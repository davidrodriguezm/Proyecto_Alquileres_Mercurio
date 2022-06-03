package com.davidrm.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class UsuarioDTO implements Serializable {
	
	private Long id;
	
	@NotNull(message="El campo DNI no puede ser nulo")
	@NotEmpty(message="El campo DNI no puede estar vacío")
	@Pattern(regexp="[0-9]{8}[A-Z]{1}",message="El campo DNI 8 digitos y 1 letra mayuscula")
	private String dni;
	
	@NotNull(message="El campo email no puede ser nulo")
	@NotEmpty(message="El campo email no puede estar vacío")
	@Email(message="No es un formato email")
	private String email;
	
	@NotNull(message="El campo nombre no puede ser nulo")
	@NotEmpty(message="El campo nombre no puede estar vacío")
	private String nombre;
	
	private String role;
	
	@NotNull(message="El campo telefono no puede ser nulo")
	@NotEmpty(message="El campo telefono no puede estar vacío")
    private String telefono;
	
	@NotNull(message="El campo contraseña no puede ser nulo")
	@NotEmpty(message="El campo contraseña no puede estar vacío")
	@Size(min = 5, message = "Minimo debe tener entre 5 caracteres")
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
