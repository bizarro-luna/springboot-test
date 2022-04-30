package com.luna.test.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luna.test.app.modelo.Banco;

public interface BancoRepository extends JpaRepository<Banco, Long> {

	

}
