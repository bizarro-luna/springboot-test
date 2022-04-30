package com.luna.test.app.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransaccionDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9080952279990423247L;

	
	private Long cuentaOrigenId;
	
	private Long cuentaDestinoId;
	
	private BigDecimal monto;
	
	private Long bancoId;

	/**
	 * @return the cuentaOrigenId
	 */
	public Long getCuentaOrigenId() {
		return cuentaOrigenId;
	}

	/**
	 * @param cuentaOrigenId the cuentaOrigenId to set
	 */
	public void setCuentaOrigenId(Long cuentaOrigenId) {
		this.cuentaOrigenId = cuentaOrigenId;
	}

	/**
	 * @return the cuentaDestinoId
	 */
	public Long getCuentaDestinoId() {
		return cuentaDestinoId;
	}

	/**
	 * @param cuentaDestinoId the cuentaDestinoId to set
	 */
	public void setCuentaDestinoId(Long cuentaDestinoId) {
		this.cuentaDestinoId = cuentaDestinoId;
	}

	/**
	 * @return the monto
	 */
	public BigDecimal getMonto() {
		return monto;
	}

	/**
	 * @param monto the monto to set
	 */
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	
	

	/**
	 * @return the bancoId
	 */
	public Long getBancoId() {
		return bancoId;
	}

	/**
	 * @param bancoId the bancoId to set
	 */
	public void setBancoId(Long bancoId) {
		this.bancoId = bancoId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransaccionDto [cuentaOrigenId=");
		builder.append(cuentaOrigenId);
		builder.append(", cuentaDestinoId=");
		builder.append(cuentaDestinoId);
		builder.append(", monto=");
		builder.append(monto);
		builder.append(", bancoId=");
		builder.append(bancoId);
		builder.append("]");
		return builder.toString();
	}

	
	
	
	
	
	
	
	
}
