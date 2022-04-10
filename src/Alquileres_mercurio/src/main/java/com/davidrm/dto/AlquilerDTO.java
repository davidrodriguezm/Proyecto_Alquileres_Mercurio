package com.davidrm.dto;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;



public class AlquilerDTO implements Serializable {
	private Long idCliente;
	private Long idVehiculo;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate fecha_inicio;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate fecha_fin;
	
	private Double pago;	
	private String comentario;

	public AlquilerDTO() {}

	public AlquilerDTO(Long idCliente, Long idVehiculo, LocalDate fecha_inicio, LocalDate fecha_fin) {
		this.idCliente = idCliente;
		this.idVehiculo = idVehiculo;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
	}

	public AlquilerDTO(Long idCliente, Long idVehiculo, LocalDate fecha_inicio, LocalDate fecha_fin, Double pago) {
		this.idCliente = idCliente;
		this.idVehiculo = idVehiculo;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.pago = pago;
	}

	public AlquilerDTO(Long idCliente, Long idVehiculo, LocalDate fecha_inicio, LocalDate fecha_fin, Double pago,
			String comentario) {
		this.idCliente = idCliente;
		this.idVehiculo = idVehiculo;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.pago = pago;
		this.comentario = comentario;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdVehiculo() {
		return idVehiculo;
	}

	public void setIdVehiculo(Long idVehiculo) {
		this.idVehiculo = idVehiculo;
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
	
}
