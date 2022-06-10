package com.davidrm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="Usuarios")
public class Usuario implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true,length=9,nullable=true)
	private String dni;
	
	@Column(name="email",unique=true,nullable=false)
	private String email;
	
	@Column(name="nombre",nullable=false)
	private String nombre;
	
	@Column(name="role",nullable=false)
	private String role;
	
	@Column(name = "telefono", nullable = false)
    private String telefono;
	
	@Column(name="password",nullable=false)
	private String password;
	
	@Column(nullable=false,columnDefinition="BOOLEAN")	
	private boolean activo;
	
	@OneToMany(mappedBy="cliente",cascade=CascadeType.ALL,orphanRemoval = true)
	private Set<Alquiler> alquileres = new HashSet<>();

	public Usuario() {}

	public Usuario(Long id, String dni, String email, String nombre, String role, String telefono, String password,
			boolean activo) {
		this.id = id;
		this.dni = dni;
		this.email = email;
		this.nombre = nombre;
		this.role = role;
		this.telefono = telefono;
		this.password = password;
		this.activo = activo;
	}

	public Usuario(String dni, String email, String nombre, String role, String telefono, String password,
			boolean activo) {
		this.dni = dni;
		this.email = email;
		this.nombre = nombre;
		this.role = role;
		this.telefono = telefono;
		this.password = password;
		this.activo = activo;
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
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Set<Alquiler> getAlquileres() {
		return alquileres;
	}

	public void setAlquileres(Set<Alquiler> alquileres) {
		this.alquileres = alquileres;
	}
	
	public void addAlquiler(Alquiler alquiler) {
		this.alquileres.add(alquiler);
		alquiler.setCliente(this);
	}
	
	public void removeAlquiler(Alquiler alquiler) {
		this.alquileres.remove(alquiler);
		alquiler.setCliente(null);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dni, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(dni, other.dni) || Objects.equals(id, other.id);
	}
	
}
