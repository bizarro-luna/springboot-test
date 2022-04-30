package com.luna.test.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.luna.test.app.exceptions.DineroInsuficienteException;
import com.luna.test.app.modelo.Banco;
import com.luna.test.app.modelo.Cuenta;
import com.luna.test.app.repository.BancoRepository;
import com.luna.test.app.repository.CuentaRepository;
import com.luna.test.app.servicio.CuentaServicio;
import com.luna.test.app.servicio.impl.CuentaServicioImpl;
import static com.luna.test.app.Datos.*;

@SpringBootTest
class SpringbootTestApplicationTests {
	
	//@Mock
	@MockBean
	CuentaRepository cuentaRepository;
	
	//@Mock
	@MockBean
	BancoRepository bancoRepository;
	
	//@InjectMocks
	@Autowired
	CuentaServicio servicio;
	
	
	@BeforeEach
	void setUp() {
//		cuentaRepository= mock(CuentaRepository.class);
//		bancoRepository= mock(BancoRepository.class);
//		servicio= new CuentaServicioImpl(cuentaRepository, bancoRepository);
		
		/*Datos.CUENTA_001.setSaldo(new BigDecimal("1000"));
		Datos.CUENTA_002.setSaldo(new BigDecimal("2000"));
		Datos.BANCO.setTotaltransferencias(0);*/
	}
	

	@Test
	void contextLoads() {
		
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(crearBanco());
		
		BigDecimal saldoOrigen= servicio.revisarSaldo(1L);
		BigDecimal saldoDestino = servicio.revisarSaldo(2L);
		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());
		
		servicio.transferir(1L, 2L, new BigDecimal("100"), 1L);
		
		int totalTransferencias= servicio.revisarTotalTransferencias(1L);
		assertEquals(1,totalTransferencias);
		
		/*
		saldoOrigen= servicio.revisarSaldo(1L);
		saldoDestino = servicio.revisarSaldo(2L);
		assertEquals("900",saldoOrigen.toPlainString());
		assertEquals("2100",saldoDestino.toPlainString());
		*/
		
		verify(cuentaRepository,times(2)).findById(1L);
		verify(cuentaRepository,times(2)).findById(2L);
		verify(cuentaRepository,times(2)).save(any(Cuenta.class));
		
		verify(bancoRepository,times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));
		
		verify(cuentaRepository,times(4)).findById(anyLong());
		verify(cuentaRepository,never()).findAll();
	}
	
	
	@Test
	void contextLoads2() {
		
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(crearBanco());
		
		BigDecimal saldoOrigen= servicio.revisarSaldo(1L);
		BigDecimal saldoDestino = servicio.revisarSaldo(2L);
		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());
		
		assertThrows(DineroInsuficienteException.class, () ->{
			servicio.transferir(1L, 2L, new BigDecimal("1200"), 1L);
		});
		
		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());
		
		int totalTransferencias= servicio.revisarTotalTransferencias(1L);
		assertEquals(0,totalTransferencias);
		
		
		
		verify(cuentaRepository,times(2)).findById(1L);
		verify(cuentaRepository,times(1)).findById(2L);
		verify(cuentaRepository,never()).save(any(Cuenta.class));
		
		verify(bancoRepository,times(1)).findById(1L);
		verify(bancoRepository,never()).save(any(Banco.class));
		
		
		verify(cuentaRepository,times(3)).findById(anyLong());
		verify(cuentaRepository,never()).findAll();
	}
	
	
	@Test
	void contextLoads3() {
		
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		
		Cuenta cuenta1 = servicio.findById(1L);
		Cuenta cuenta2 = servicio.findById(1L);
		
		assertSame(cuenta1,cuenta2);
		assertTrue(cuenta1==cuenta2);
		assertEquals("Roberto",cuenta1.getPersona());
		assertEquals("Roberto",cuenta2.getPersona());
		verify( cuentaRepository,times(2)).findById(1L);
		
	}
	
	@Test
	void testFindAll() {
		//Given
		List<Cuenta> datos= Arrays.asList(crearCuenta001().get(),crearCuenta002().get());
		when(cuentaRepository.findAll()).thenReturn(datos);
		
		//when
		List<Cuenta> cuentas= cuentaRepository.findAll();
		
		//then
		assertFalse(cuentas.isEmpty());
		assertEquals(2,cuentas.size());
		assertTrue(cuentas.contains(crearCuenta002().get()));
		
		verify(cuentaRepository).findAll();
		
	}
	
	
	@Test
	void testSave() {
		//given
		Cuenta cuentaPepe= new Cuenta(null, "Pepe", new BigDecimal("3000"));
		when(cuentaRepository.save(any())).then(invocation->{
			Cuenta c= invocation.getArgument(0);
			c.setId(3L);
			return c;
		});
		
		//when
		
		Cuenta cuenta = servicio.save(cuentaPepe);
		//then
		assertEquals("Pepe",cuenta.getPersona());
		assertEquals(3,cuenta.getId());
		assertEquals("3000",cuenta.getSaldo().toPlainString());
		
		verify(cuentaRepository).save(any());
		
		
	}

}




















