package com.luna.test.app.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="bancos")
public class Banco implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2477680151405755924L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	@Column(name="total_transferencias")
	private int totaltransferencias;

	public Banco() {
	}

	public Banco(Long id, String nombre, int totaltransferencias) {
		this.id = id;
		this.nombre = nombre;
		this.totaltransferencias = totaltransferencias;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the totaltransferencias
	 */
	public int getTotaltransferencias() {
		return totaltransferencias;
	}

	/**
	 * @param totaltransferencias the totaltransferencias to set
	 */
	public void setTotaltransferencias(int totaltransferencias) {
		this.totaltransferencias = totaltransferencias;
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Banco [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", totaltransferencias=");
		builder.append(totaltransferencias);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	

}
