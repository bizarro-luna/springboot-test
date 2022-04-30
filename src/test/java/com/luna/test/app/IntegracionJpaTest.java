package com.luna.test.app;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.luna.test.app.modelo.Cuenta;
import com.luna.test.app.repository.CuentaRepository;



@DataJpaTest
public class IntegracionJpaTest {

	
	
	
	@Autowired
    CuentaRepository cuentaRepository;
	
	
	@Test
	void testFindById() {
		Optional<Cuenta> optional=  cuentaRepository.findById(1L);
		assertTrue(optional.isPresent());
		assertEquals("Roberto",optional.get().getPersona());
	}
	
	@Test
	void testFindByPersona() {
		Optional<Cuenta> optional=  cuentaRepository.findByPersona("Roberto");
		assertTrue(optional.isPresent());
		assertEquals("Roberto",optional.get().getPersona());
		assertEquals("1000.00",optional.get().getSaldo().toPlainString());
	}

	
	
	/*@Test
	void testFindByPersonaThrowException() {
		Optional<Cuenta> optional=  cuentaRepository.findByPersona("Rod");
		assertThrows(NoSuchElementException.class, ()->{
			
		});
		assertTrue(optional.isPresent());
		assertEquals("Roberto",optional.get().getPersona());
		assertEquals("1000.00",optional.get().getSaldo().toPlainString());
	}*/
	
	
	@Test
	void testFindAll() {
		List<Cuenta> cuenta = cuentaRepository.findAll();
		assertFalse(cuenta.isEmpty());
		assertEquals(2,cuenta.size());
	}
	
	@Test
	void testSave() {
		
		Cuenta cuenta= new Cuenta(null,"Pepe",new BigDecimal("3000"));
		cuentaRepository.save(cuenta);
		
		Cuenta cuenta2=cuentaRepository.findByPersona("Pepe").get();
		
		assertEquals("Pepe",cuenta2.getPersona());
		assertEquals("3000",cuenta.getSaldo().toPlainString());
		assertEquals(3,cuenta.getId());
		
		
		
	}
	
	@Test
	void testUpdate() {
		
		Cuenta cuenta= new Cuenta(null,"Pepe",new BigDecimal("3000"));
		cuentaRepository.save(cuenta);
		
		Cuenta cuenta2=cuentaRepository.findByPersona("Pepe").get();
		
		assertEquals("Pepe",cuenta2.getPersona());
		assertEquals("3000",cuenta2.getSaldo().toPlainString());
		assertEquals(4,cuenta2.getId());
		
		cuenta2.setSaldo(new BigDecimal("3800"));
		
		Cuenta actualizada=cuentaRepository.save(cuenta2);
		assertEquals("Pepe",actualizada.getPersona());
		assertEquals("3800",actualizada.getSaldo().toPlainString());
		
		
	}
	
	
	@Test
	void testDelete() {
		
		Cuenta cuenta= cuentaRepository.findById(2L).get();
		assertEquals("Erick",cuenta.getPersona());
		
		cuentaRepository.delete(cuenta);
		/*
		assertThrows(NoSuchElementException.class,()->{
			cuentaRepository.findById(2L);
		});*/
		
	}
	
}



























