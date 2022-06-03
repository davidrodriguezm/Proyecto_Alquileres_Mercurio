package com.davidrm.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordDTO implements Serializable {
	
	@NotNull(message="La contraseña no puede ser nulo")
	@NotEmpty(message="La contraseña no puede estar vacía")
	@Size(min = 5, message = "Minimo debe tener entre 5 caracteres")
	private String password_nueva;
	
	@NotNull(message="La contraseña no puede ser nulo")
	@NotEmpty(message="La contraseña no puede estar vacía")
	@Size(min = 5, message = "Minimo debe tener entre 5 caracteres")
	private String password_repetida;
	
	
	public PasswordDTO() {}

	public PasswordDTO(String password_nueva, String password_repetida) {
		this.password_nueva = password_nueva;
		this.password_repetida = password_repetida;
	}

	public String getPassword_nueva() {
		return password_nueva;
	}

	public void setPassword_nueva(String password_nueva) {
		this.password_nueva = password_nueva;
	}	
	
	public String getPassword_repetida() {
		return password_repetida;
	}

	public void setPassword_repetida(String password_repetida) {
		this.password_repetida = password_repetida;
	}

	public boolean coincide() {
		return this.password_nueva.equals(this.password_repetida) ? true : false;
	}

}
