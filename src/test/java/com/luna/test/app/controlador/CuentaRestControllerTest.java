package com.luna.test.app.controlador;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luna.test.app.modelo.Cuenta;
import com.luna.test.app.modelo.TransaccionDto;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag("integracion_rt")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment= RANDOM_PORT )
public class CuentaRestControllerTest {
	
	
	@Autowired
	private TestRestTemplate client;
	
	
	private ObjectMapper mapper;

	@BeforeEach
	void setUp() {
		
		mapper= new ObjectMapper();
		
	}
	
	@Test
	@Order(1)
	void testTrasferir() throws JsonMappingException, JsonProcessingException {
		
		TransaccionDto dto = new TransaccionDto();
		dto.setMonto(new BigDecimal("100"));
		dto.setCuentaDestinoId(2L);
		dto.setCuentaOrigenId(1L);
		dto.setBancoId(1L);
		
		ResponseEntity<String> response=  client.postForEntity("/api/cuentas/transferir", dto, String.class);
		
		String json = response.getBody();
		
		
		//1
		assertEquals(HttpStatus.OK,response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON,response.getHeaders().getContentType());
		assertNotNull(json);
		assertTrue(json.contains("Transferencia realizada con exito!"));
		
		//2
		JsonNode jsonNode=  mapper.readTree(json);
		assertEquals("Transferencia realizada con exito!",jsonNode.get("mensaje").asText());
		assertEquals(LocalDate.now().toString(),jsonNode.get("date").asText());
		assertEquals("100",jsonNode.path("transaccion").path("monto").asText());
		assertEquals(1L,jsonNode.path("transaccion").path("cuentaOrigenId").asLong());
		
		//3
		Map<String,Object> responseMap= new HashMap<>();
		responseMap.put("date", LocalDate.now().toString());
		responseMap.put("status","OK");
		responseMap.put("mensaje", "Transferencia realizada con exito!");
		responseMap.put("transaccion", dto);
		
		assertEquals(mapper.writeValueAsString(responseMap),json);
		
	}
	
	
	@Test
	@Order(2)
	void testDetalle() {
		
		ResponseEntity<Cuenta> response= client.getForEntity("/api/cuentas/1", Cuenta.class);
		assertEquals(HttpStatus.OK,response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON,response.getHeaders().getContentType());
		Cuenta cuenta = response.getBody();
		assertNotNull(cuenta);
		assertEquals(1L,cuenta.getId());
		assertEquals("Roberto",cuenta.getPersona());
		assertEquals("900.00",cuenta.getSaldo().toPlainString());
		assertEquals(new Cuenta(1L, "Roberto", new BigDecimal("900.00")),cuenta);
		
		
		
	}
	
	@Test
	@Order(3)
	void testListar() throws JsonMappingException, JsonProcessingException {
		
	    ResponseEntity<Cuenta[]> respuesta=	 client.getForEntity("/api/cuentas", Cuenta[].class);
		List<Cuenta> cuentas=Arrays.asList(respuesta.getBody());
		assertEquals(HttpStatus.OK,respuesta.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON,respuesta.getHeaders().getContentType());
		//1
		assertEquals(2,cuentas.size());
		assertEquals(1L,cuentas.get(0).getId());
		assertEquals("Roberto",cuentas.get(0).getPersona());
		assertEquals("900.00",cuentas.get(0).getSaldo().toPlainString());
		
		assertEquals(2L,cuentas.get(1).getId());
		assertEquals("Erick",cuentas.get(1).getPersona());
		assertEquals("2100.00",cuentas.get(1).getSaldo().toPlainString());
		
		//2
		JsonNode json= mapper.readTree(mapper.writeValueAsString(cuentas));
		assertEquals(1L,json.get(0).path("id").asLong());
		assertEquals("Roberto",json.get(0).path("persona").asText());
		assertEquals("900.0",json.get(0).path("saldo").asText());
		
		assertEquals(2L,json.get(1).path("id").asLong());
		assertEquals("Erick",json.get(1).path("persona").asText());
		assertEquals("2100.0",json.get(1).path("saldo").asText());
	}
	
	
	
	@Test
	@Order(4)
	void testGuardar() {
		
		Cuenta cuenta = new Cuenta(null, "Melissa", new BigDecimal("3000"));
		
		ResponseEntity<Cuenta> respuesta= client.postForEntity("/api/cuentas", cuenta, Cuenta.class);
		assertEquals(HttpStatus.CREATED,respuesta.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON,respuesta.getHeaders().getContentType());
		Cuenta cuentaCreada= respuesta.getBody();
		assertNotNull(cuentaCreada);
		assertEquals(3L,cuentaCreada.getId());
		assertEquals("Melissa",cuentaCreada.getPersona());
		assertEquals("3000",cuentaCreada.getSaldo().toPlainString());
	}
	
	
	@Test
	@Order(5)
	void testEliminar() {
		
		ResponseEntity<Cuenta[]> respuesta=	 client.getForEntity("/api/cuentas", Cuenta[].class);
		List<Cuenta> cuentas=Arrays.asList(respuesta.getBody());
		assertEquals(3,cuentas.size());
		
		//ResponseEntity<Void> exchange= client.exchange("/api/cuentas/3", HttpMethod.DELETE,null,Void.class);
		Map<String,Long> variables =new HashMap<>();
		variables.put("id", 3L);
		ResponseEntity<Void> exchange= client.exchange("/api/cuentas/{id}", HttpMethod.DELETE,null,Void.class,variables);
		assertEquals(HttpStatus.NO_CONTENT,exchange.getStatusCode());
		assertFalse(exchange.hasBody());
		//client.delete("/api/cuentas/3");
		
		
		ResponseEntity<Cuenta> response= client.getForEntity("/api/cuentas/3", Cuenta.class);
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
		assertFalse(response.hasBody());

		ResponseEntity<Cuenta[]> respuesta2=	 client.getForEntity("/api/cuentas", Cuenta[].class);
		List<Cuenta> cuentas2=Arrays.asList(respuesta2.getBody());
		assertEquals(2,cuentas2.size());
		
		
		
	}
	
	

}
