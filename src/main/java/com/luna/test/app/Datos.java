package com.luna.test.app;

import java.math.BigDecimal;
import java.util.Optional;

import com.luna.test.app.modelo.Banco;
import com.luna.test.app.modelo.Cuenta;

public class Datos {
	
	/*public static final Cuenta CUENTA_001= new Cuenta(1L, "Roberto", new BigDecimal("1000"));
	public static final Cuenta CUENTA_002= new Cuenta(2L, "Erick", new BigDecimal("2000")); 
	public static final Banco  BANCO= new Banco(1L, "Banco del Bienestar", 0);*/
	
	
	public static Optional<Cuenta> crearCuenta001() {
		return Optional.of(new Cuenta(1L, "Roberto", new BigDecimal("1000")));
	}
	
	public static Optional<Cuenta> crearCuenta002() {
		return Optional.of(new Cuenta(2L, "Erick", new BigDecimal("2000"))); 
	}
	
	public static Optional<Banco> crearBanco() {
		return  Optional.of(new Banco(1L, "Banco del Bienestar", 0));
	}

}
