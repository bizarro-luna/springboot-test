package com.luna.test.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.luna.test.app.modelo.Cuenta;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
	
	//@Query("select c from Cuenta c where c.persona=?1")
	Optional<Cuenta> findByPersona(String persona);	
	

}
