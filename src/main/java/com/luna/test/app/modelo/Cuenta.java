package com.luna.test.app.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.luna.test.app.exceptions.DineroInsuficienteException;


@Entity
@Table(name="cuentas")
public class Cuenta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5940697473772976084L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String persona;
	
	private BigDecimal saldo;

	public Cuenta() {
	}

	public Cuenta(Long id, String persona, BigDecimal saldo) {
		this.id = id;
		this.persona = persona;
		this.saldo = saldo;
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
	 * @return the persona
	 */
	public String getPersona() {
		return persona;
	}

	/**
	 * @param persona the persona to set
	 */
	public void setPersona(String persona) {
		this.persona = persona;
	}

	/**
	 * @return the saldo
	 */
	public BigDecimal getSaldo() {
		return saldo;
	}

	/**
	 * @param saldo the saldo to set
	 */
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
	
		if(this == o) return true;
		
		if(o==null ||  getClass()!=o.getClass()) return false;
		
		Cuenta cuenta= (Cuenta) o;
		
		return Objects.equals(id, cuenta.id) && Objects.equals(persona, cuenta.persona);
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id,persona,saldo);
	}
	
	public void debito(BigDecimal monto) {
		BigDecimal nuevoSaldo=this.saldo.subtract(monto);
		
		if(nuevoSaldo.compareTo(BigDecimal.ZERO)<0) {
			throw new DineroInsuficienteException("Dinero insuficiente");
		}
		
		this.saldo=nuevoSaldo;
		
	}
	
	public void credito(BigDecimal monto) {
		this.saldo= this.saldo.add(monto);
		
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cuenta [id=");
		builder.append(id);
		builder.append(", persona=");
		builder.append(persona);
		builder.append(", saldo=");
		builder.append(saldo);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	
	

}
