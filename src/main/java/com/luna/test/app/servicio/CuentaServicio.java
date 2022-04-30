package com.luna.test.app.servicio;

import java.math.BigDecimal;
import java.util.List;

import com.luna.test.app.modelo.Cuenta;

public interface CuentaServicio {
	
	List<Cuenta> findAll();
		
	Cuenta findById(Long id);
	
	Cuenta save(Cuenta cuenta);
	
	void deleteById(Long id);
	
	
	int revisarTotalTransferencias(Long bancoId);
	
	BigDecimal revisarSaldo(Long cuentaId);
	
	void transferir(Long cuentaOrigen,Long cuentaDestino,BigDecimal monto,Long bancoId);
	
	

	
	
}
