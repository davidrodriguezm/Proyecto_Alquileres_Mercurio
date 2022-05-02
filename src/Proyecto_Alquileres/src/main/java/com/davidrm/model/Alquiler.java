package com.davidrm.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="alquileres")
public class Alquiler implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Usuario cliente;	

	@ManyToOne
	@JoinColumn(name="id_vehiculo")
	private Vehiculo vehiculo;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="fecha_inicio",nullable=false)
	private LocalDate fecha_inicio;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="fecha_fin",nullable=false)
	private LocalDate fecha_fin;
	
	@Column(name = "pago")
    private Double pago;
	
	@Column(name="comentario")
	private String comentario;
	
	@Column(name="estado",nullable=false)
	private String estado;

	public Alquiler() {}
	
	public Alquiler(Usuario cliente, Vehiculo vehiculo, LocalDate fecha_inicio, LocalDate fecha_fin, 
			Double pago, String comentario, String estado) {
		this.cliente = cliente;
		this.vehiculo = vehiculo;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.pago = pago;
		this.comentario = comentario;
		this.estado = estado;
	}
	
	public Alquiler(Usuario cliente, Vehiculo vehiculo, LocalDate fecha_inicio, LocalDate fecha_fin, 
			String estado) {
		this.cliente = cliente;
		this.vehiculo = vehiculo;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.estado = estado;
	}

	public Alquiler(Long id, Usuario cliente, Vehiculo vehiculo, LocalDate fecha_inicio, LocalDate fecha_fin,
			Double pago, String comentario, String estado) {
		this.id = id;
		this.cliente = cliente;
		this.vehiculo = vehiculo;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.pago = pago;
		this.comentario = comentario;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getCliente() {
		return cliente;
	}


	public void setCliente(Usuario cliente) {
		this.cliente = cliente;
	}


	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public LocalDate getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(LocalDate fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	
	public LocalDate getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(LocalDate fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	
	public Double getPago() {
		return pago;
	}

	public void setPago(Double pago) {
		this.pago = pago;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getInicioFormato() {
		return fecha_inicio.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public String getFinFormato() {
		return fecha_fin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
			
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alquiler other = (Alquiler) obj;
		return Objects.equals(id, other.id);
	}
}
