package com.luna.test.app.controlador;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.luna.test.app.Datos.*;
import com.luna.test.app.modelo.Cuenta;
import com.luna.test.app.modelo.TransaccionDto;
import com.luna.test.app.repository.CuentaRepository;
import com.luna.test.app.servicio.CuentaServicio;
import static org.hamcrest.Matchers.*;

/**
 * Test para {@link CuentaControlador}
 * @author Hector
 *
 */
@WebMvcTest(CuentaControlador.class)
public class CuentaControladorTest {
	
	
	
	/**
	 * Para probar los restcontroladores
	 */
	@Autowired
	private MockMvc mvc;
	
	ObjectMapper mapper;
	
	
	@BeforeEach
	void setUp() {
		
		mapper= new ObjectMapper();
		
	}
	
	
	@MockBean
	private CuentaServicio cuentaServicio;
	
	
	@Test
	void testDetalle() throws Exception {
		
		when(cuentaServicio.findById(1L)).thenReturn(crearCuenta001().get());
		
		
		mvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
		//Then
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.persona").value("Roberto"))
		.andExpect(jsonPath("$.saldo").value("1000"))
		;
		
		verify(cuentaServicio).findById(1L);
		
	}
	
	
	@Test
	void testTransferir() throws JsonProcessingException, Exception {
		
		TransaccionDto dto = new TransaccionDto();
		dto.setCuentaOrigenId(1L);
		dto.setCuentaDestinoId(2L);
		dto.setMonto(new BigDecimal("100"));
		dto.setBancoId(1L);
		System.out.println(mapper.writeValueAsString(dto));
		
		Map<String,Object> response= new HashMap<>();
		response.put("date", LocalDate.now().toString());
		response.put("status","OK");
		response.put("mensaje", "Transferencia realizada con exito!");
		response.put("transaccion", dto);
		System.out.println(mapper.writeValueAsString(response));
		
		
		mvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(dto)))
		
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
		.andExpect(jsonPath("$.mensaje").value("Transferencia realizada con exito!"))
		.andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(1L))
		.andExpect(content().json(mapper.writeValueAsString(response)));
	}
	
	
	@Test
	void testListar() throws JsonProcessingException, Exception {
		//Given
		List<Cuenta> cuentas = Arrays.asList(crearCuenta001().get(),crearCuenta002().get());
		
		when(cuentaServicio.findAll()).thenReturn(cuentas);
		
		//When
		mvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
		//Then
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.[0].persona").value("Roberto"))
		.andExpect(jsonPath("$.[0].saldo").value("1000"))
		.andExpect(jsonPath("$.[1].persona").value("Erick"))
		.andExpect(jsonPath("$.[1].saldo").value("2000"))
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(content().json(mapper.writeValueAsString(cuentas)));
		
		verify(cuentaServicio).findAll();
		
	}
	
	
	@Test
	void testGuardar() throws JsonProcessingException, Exception {
		//Given
		Cuenta cuenta= new Cuenta(null, "Juan",new BigDecimal("1500"));
		when(cuentaServicio.save(any())).then(invocation->{
			Cuenta c= invocation.getArgument(0);
			c.setId(3L);
			return c;
		});
		
		
		//when
		mvc.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(cuenta)))
		//Then
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id",is(3)))
		.andExpect(jsonPath("$.persona",is("Juan")))
		.andExpect(jsonPath("$.saldo",is(1500)));
		
		verify(cuentaServicio).save(any());
	}
	
	
	
	
	
	
	

}

























