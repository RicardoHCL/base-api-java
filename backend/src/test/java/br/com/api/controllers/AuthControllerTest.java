package br.com.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.api.constants.UrlConstantes;
import br.com.api.controllers.base.AuthController;
import br.com.api.models.RespostaLoginDTO;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Teste de ingração do login")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private AuthController controller;

	@Autowired
	private UsuarioController usuarioController;

	@BeforeAll
	@DisplayName("Criando usuário para ser usado no login")
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();

		String json = "{ \"nome\": \"Teste\"," + "  \"login\": \"teste\"," + " \"email\": \"teste@gmail.com\","
				+ " \"senha\": \"123456\"," + " \"confirmacaoSenha\": \"123456\" }";

		this.mockMvc.perform(MockMvcRequestBuilders.post(UrlConstantes.USUARIOS).contentType(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(MockMvcResultMatchers.status().isOk());

		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	@DisplayName("Fazendo login")
	public void login() throws Exception {
		String json = "{ \"login\": \"teste\", \"senha\": \"123456\" }";

		MvcResult resposta = this.mockMvc.perform(MockMvcRequestBuilders.post(UrlConstantes.ACESSO_LIBERADO + "/login")
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		String conteudo = resposta.getResponse().getContentAsString();
		RespostaLoginDTO respostaDTO = converterJsonEmRespostaLoginDTO(conteudo);
		
		assertThat(respostaDTO.getLogin()).isEqualTo("teste");
		assertThat(respostaDTO.getToken()).isNotNull();
		assertThat(respostaDTO.getToken()).isNotBlank();
	}
	

	public static RespostaLoginDTO converterJsonEmRespostaLoginDTO(String json) {
		RespostaLoginDTO respostaDTO = new RespostaLoginDTO();
		ObjectMapper mapper = new ObjectMapper();

		try {
			respostaDTO = mapper.readValue(json, RespostaLoginDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return respostaDTO;
	}

}
