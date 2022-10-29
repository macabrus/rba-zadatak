package com.rba.bernard.zadatak;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZadatakApplicationTests {

	@Value("${folder}")
	private String folder;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@BeforeAll
	void setUp() throws IOException {
		FileUtils.deleteDirectory(Path.of(folder).toFile());
		mockMvc = webAppContextSetup(wac).addFilter(((request, response, chain) -> {
			response.setCharacterEncoding("UTF-8");
			chain.doFilter(request, response);
		})).build();
	}

	@Test
	@Order(1)
	void Dodavanje_Osobe() throws Exception {
		var osobe = List.of(
			new OsobaDto("19316631100", "Bernard", "Crnković"),
			new OsobaDto("01234567891", "Bruno", "Pries")
		).listIterator();
		var expectOsobe = List.of(
			new Osoba("19316631100", "Bernard", "Crnković", Status.NIJE_IZDANO),
			new Osoba("01234567891", "Bruno", "Pries", Status.NIJE_IZDANO)
		);
		while (osobe.hasNext()) {
			var i = osobe.nextIndex();
			var o = osobe.next();
			mockMvc.perform(
				post("/osoba")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(o))
				).andExpect(status().isOk())
				.andExpect(content().json(
					mapper.writeValueAsString(expectOsobe.get(i)))
				);
		}
	}

	@Test
	@Order(2)
	void Pretraživanje_Skupa_Osoba() throws Exception {
		mockMvc.perform(get("/osoba").queryParam("oib", "1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.*", isA(ArrayList.class)))
			.andExpect(jsonPath("$.*", hasSize(2)));
	}

	@Test
	@Order(3)
	void Pretraživanje_Nepostojeće_Osobe() throws Exception {
		mockMvc.perform(get("/osoba").queryParam("oib", "321"))
			.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	@Order(4)
	void Izdavanje_Kartice() throws Exception {
		mockMvc.perform(post("/osoba/%s/kartica".formatted("19316631100")))
			.andExpect(status().is(HttpStatus.CREATED.value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
			.andExpect(content().string("""
            Ime: Bernard
            Prezime: Crnković
            OIB: 19316631100
            Status: IZDANO
            """));
	}

	@Test
	@Order(5)
	void Dohvaćanje_Postojeće_Kartice() throws Exception {
		mockMvc.perform(get("/osoba/%s/kartica".formatted("19316631100")))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
			.andExpect(content().string("""
            Ime: Bernard
            Prezime: Crnković
            OIB: 19316631100
            Status: IZDANO
            """));
	}

	@Test
	@Order(6)
	void Dohvaćanje_Nepostojeće_Kartice() throws Exception {
		mockMvc.perform(get("/kartica").queryParam("oib", "19316631100"))
			.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	@Order(7)
	void Brisanje_Osobe() throws Exception {
		mockMvc.perform(delete("/osoba/19316631100"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.oib", equalTo("19316631100")));
		mockMvc.perform(get("/osoba"))
			.andExpect(jsonPath("$.*", isA(ArrayList.class)))
			.andExpect(jsonPath("$.*", hasSize(1)));
	}

}
