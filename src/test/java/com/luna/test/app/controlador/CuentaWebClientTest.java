package com.luna.test.app.controlador;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luna.test.app.modelo.Cuenta;
import com.luna.test.app.modelo.TransaccionDto;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("integracion_wc")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
public class CuentaWebClientTest {
	
	
	@Autowired
	private WebTestClient client;
	
	ObjectMapper mapper;
	
	
	@BeforeEach
	void setUp() {
		
		mapper= new ObjectMapper();
		
	}
	
	@Test
	@Order(1)
	void testTransferir() throws JsonProcessingException {
		//given
		TransaccionDto dto = new TransaccionDto();
		dto.setCuentaOrigenId(1L); 
		
		dto.setCuentaDestinoId(2L);
		dto.setBancoId(1L);
		dto.setMonto(new BigDecimal("100"));
		
		Map<String,Object> response= new HashMap<>();
		response.put("date", LocalDate.now().toString());
		response.put("status","OK");
		response.put("mensaje", "Transferencia realizada con exito!");
		response.put("transaccion", dto);
		
		//when
		client.post().uri("/api/cuentas/transferir")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(dto)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.consumeWith(respuesta->{
			
			try {
				//readTree acepta bytes[] y String
				JsonNode json = mapper.readTree(respuesta.getResponseBody());
				
				
				assertEquals("Transferencia realizada con exito!",json.path("mensaje").asText());
				assertEquals(1L,json.path("transaccion").path("cuentaOrigenId").asLong());
				assertEquals(LocalDate.now().toString(),json.path("date").asText());
				assertEquals("100",json.path("transaccion").path("monto").asText());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block 
				e.printStackTrace();
			}
		})
		
		.jsonPath("$.mensaje").isNotEmpty()
		.jsonPath("$.mensaje").value(is("Transferencia realizada con exito!"))
		.jsonPath("$.mensaje").value(valor->{
			 assertEquals("Transferencia realizada con exito!",valor);
		})
		.jsonPath("$.mensaje").isEqualTo("Transferencia realizada con exito!")
		.jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
		.jsonPath("$.date").isEqualTo(LocalDate.now().toString())
		.json(mapper.writeValueAsString(response));
	}
	
	
	@Test
	@Order(2)
	void testDetalle() throws JsonProcessingException {
		
		
		Cuenta cuenta= new Cuenta(1L, "Roberto", new BigDecimal("900"));
		
		
		client.get().uri("/api/cuentas/1").exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.persona").isEqualTo("Roberto")
		.jsonPath("$.saldo").isEqualTo(900)
		.json(mapper.writeValueAsString(cuenta));
	}
	
	@Test
	@Order(3)
	void testDetalle2() {
		client.get().uri("/api/cuentas/2").exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(Cuenta.class)
		.consumeWith(response->{
			Cuenta cuenta= response.getResponseBody();
			assertEquals("Erick",cuenta.getPersona());
			assertEquals("2100.00",cuenta.getSaldo().toPlainString());
		});
	}
	
	
	@Test
	@Order(4)
	void testListar(){
		
		client.get().uri("/api/cuentas").exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$[0].persona").isEqualTo("Roberto")
		.jsonPath("$[0].id").isEqualTo(1)
		.jsonPath("$[0].saldo").isEqualTo(900)
		.jsonPath("$[1].persona").isEqualTo("Erick")
		.jsonPath("$[1].id").isEqualTo(2)
		.jsonPath("$[1].saldo").isEqualTo(2100)
		.jsonPath("$").isArray()
		.jsonPath("$").value(hasSize(2));
		
		
	}
	
	@Test
	@Order(5)
	void testListar2(){
		
		client.get().uri("/api/cuentas").exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Cuenta.class)
		.consumeWith(response->{
			List<Cuenta> cuentas= response.getResponseBody();
			assertNotNull(cuentas);
			assertEquals(2,cuentas.size());
			assertEquals("Roberto",cuentas.get(0).getPersona());
			assertEquals(1L,cuentas.get(0).getId());
			assertEquals(900,cuentas.get(0).getSaldo().intValue());
			assertEquals("Erick",cuentas.get(1).getPersona());
			assertEquals(2L,cuentas.get(1).getId());
			assertEquals("2100.0",cuentas.get(1).getSaldo().toPlainString());
		})
		.hasSize(2)
		;
	}
	
	
	@Test
	@Order(6)
	void testGuardar() {
		//given
		Cuenta cuenta = new Cuenta(null, "Melissa", new BigDecimal("3000"));
		
		//when
		client.post().uri("/api/cuentas")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(cuenta)
		.exchange()
		
		//then
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.persona").isEqualTo("Melissa")
		.jsonPath("$.persona").value(is("Melissa"))
		.jsonPath("$.id").isEqualTo(3)
		.jsonPath("$.saldo").isEqualTo(3000);
		
		
	}
	
	
	@Test
	@Order(7)
	void testGuardar2() {
		//given
		Cuenta cuenta = new Cuenta(null, "Yoselin", new BigDecimal("3500"));
		
		//when
		client.post().uri("/api/cuentas")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(cuenta)
		.exchange()
		
		//then
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(Cuenta.class)
		.consumeWith(response->{
			Cuenta c= response.getResponseBody();
			assertNotNull(c);
			assertEquals(4L,c.getId());
			assertEquals("Yoselin",c.getPersona());
			assertEquals(3500,c.getSaldo().intValue());
			
		});	
	}
	
	
	@Test
	@Order(8)
	void testEliminar() {
		
		client.get().uri("/api/cuentas").exchange()
		.expectStatus().isOk()
		.expectBodyList(Cuenta.class)
		.hasSize(4);
		
		
		client.delete().uri("/api/cuentas/3")
		.exchange()
		.expectStatus().isNoContent()
		.expectBody().isEmpty();
		
		client.get().uri("/api/cuentas").exchange()
		.expectStatus().isOk()
		.expectBodyList(Cuenta.class)
		.hasSize(3);
		
		
		client.get().uri("/api/cuentas/3").exchange()
		//.expectStatus().is5xxServerError();
		.expectStatus().isNotFound()
		.expectBody().isEmpty();
		
	}
	
	

}











