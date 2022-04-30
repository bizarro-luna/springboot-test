package com.luna.test.app.servicio.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luna.test.app.modelo.Banco;
import com.luna.test.app.modelo.Cuenta;
import com.luna.test.app.repository.BancoRepository;
import com.luna.test.app.repository.CuentaRepository;
import com.luna.test.app.servicio.CuentaServicio;



@Service
public class CuentaServicioImpl implements CuentaServicio {

	
	private CuentaRepository cuentaRepository;
	
	private BancoRepository bancoRepository;
	
	
	
	
	public CuentaServicioImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
		this.cuentaRepository = cuentaRepository;
		this.bancoRepository = bancoRepository;
	}

	@Override
	@Transactional(readOnly=true)
	public Cuenta findById(Long id) {
		return cuentaRepository.findById(id).get();
	}

	@Override
	@Transactional(readOnly=true)
	public int revisarTotalTransferencias(Long bancoId) {
		Optional<Banco> banco = bancoRepository.findById(bancoId);
		return banco.get().getTotaltransferencias();
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal revisarSaldo(Long cuentaId) {
		Optional<Cuenta> cuenta = cuentaRepository.findById(cuentaId);
		return cuenta.get().getSaldo();
	}

	@Override
	@Transactional
	public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto,Long bancoId) {
		
		Optional<Cuenta> cuentaOrigen= cuentaRepository.findById(numCuentaOrigen);
		cuentaOrigen.get().debito(monto);
		cuentaRepository.save(cuentaOrigen.get());
		
		Optional<Cuenta> cuentaDestino= cuentaRepository.findById(numCuentaDestino);
		cuentaDestino.get().credito(monto);
		cuentaRepository.save(cuentaDestino.get());
		
		Optional<Banco> banco= bancoRepository.findById(bancoId);
		int totalTransferencias=banco.get().getTotaltransferencias();
		banco.get().setTotaltransferencias(++totalTransferencias);
		bancoRepository.save(banco.get());
		
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<Cuenta> findAll() {
		return cuentaRepository.findAll();
	}

	@Override
	@Transactional
	public Cuenta save(Cuenta cuenta) {
		return cuentaRepository.save(cuenta);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		cuentaRepository.deleteById(id);	
	}

}























