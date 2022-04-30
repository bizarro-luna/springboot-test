package com.luna.test.app.controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.luna.test.app.modelo.Cuenta;
import com.luna.test.app.modelo.TransaccionDto;
import com.luna.test.app.servicio.CuentaServicio;



@RestController
@RequestMapping("/api/cuentas")
public class CuentaControlador  {

	
	@Autowired
	private CuentaServicio cuentaServicio;
	
	
	@GetMapping
	@ResponseStatus(OK)
	public List<Cuenta> listar(){
		return cuentaServicio.findAll();
	}
	
	
	@GetMapping("/{id}")
	private ResponseEntity<?> detalle(@PathVariable Long id) {
		
		Cuenta cuenta=null;
		
		try {
		cuenta= cuentaServicio.findById(id);
		}catch(NoSuchElementException e) {
			return ResponseEntity.notFound().build();
			
		}
		
		return ResponseEntity.ok(cuenta);
	}
	
	
	@PostMapping()
	@ResponseStatus(CREATED)
	public Cuenta guardar(@RequestBody Cuenta cuenta) {
		return cuentaServicio.save(cuenta);
	}
	
	
	@PostMapping("/transferir")
	public ResponseEntity<?> transferir(@RequestBody  TransaccionDto dto  ){
		
		cuentaServicio.transferir(dto.getCuentaOrigenId(), dto.getCuentaDestinoId(), dto.getMonto(), dto.getBancoId());
		
		Map<String,Object> response= new HashMap<>();
		response.put("date", LocalDate.now().toString());
		response.put("status","OK");
		response.put("mensaje", "Transferencia realizada con exito!");
		response.put("transaccion", dto);
		
		
		return ResponseEntity.ok(response);
	}
	
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		
		cuentaServicio.deleteById(id);
		
		return ResponseEntity.noContent().build();
		
	}
	
	
	
}























