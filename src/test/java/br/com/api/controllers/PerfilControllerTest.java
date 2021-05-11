package br.com.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
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
import br.com.api.dtos.PerfilDTO;
import br.com.api.models.Perfil;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Testes de ingração do perfil")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerfilControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private PerfilController controller;

	@Autowired
	private UsuarioController usuarioController;

	@BeforeAll
	@DisplayName("Cadastrando um usuário para ser adicionado/removido os perfis e iniciando com um perfil salvo no banco")
	public void setUp() throws Exception {

		this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();

		String json = "{ \"nome\": \"Teste\"," + "  \"login\": \"teste\"," + " \"email\": \"teste@gmail.com\","
				+ " \"senha\": \"123456\"," + " \"confirmacaoSenha\": \"123456\" }";

		this.mockMvc.perform(MockMvcRequestBuilders.post(UrlConstantes.USUARIOS).contentType(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(MockMvcResultMatchers.status().isOk());

		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

	}

	@Test
	@Order(2)
	@DisplayName("Listando os perfis ativos")
	public void listarPerfisAtivos() throws Exception {
		MvcResult resposta = this.mockMvc.perform(MockMvcRequestBuilders.get(UrlConstantes.PERFIS))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		String conteudo = resposta.getResponse().getContentAsString();
		PerfilDTO dto = converterJsonEmPerfilDTO(conteudo);

		assertThat(dto.getIdUsuario()).isNull();
		assertThat(dto.getLogin()).isNull();
		assertThat(dto.getPerfis()).isNotEmpty();
		assertThat(dto.getPerfis().size()).isEqualTo(2);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_USUARIO);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_ADMIN);
	}

	@Test
	@Order(1)
	@DisplayName("Adicionando perfil admin a um usuário")
	public void adicionarPerfilAoUsuario() throws Exception {
		String json = "{ \"login\": \"teste\", \"perfis\": [ \"ADMINISTRADOR\"] }";

		MvcResult resposta = this.mockMvc
				.perform(MockMvcRequestBuilders.post(UrlConstantes.PERFIS + "/adicionar")
						.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		String conteudo = resposta.getResponse().getContentAsString();
		PerfilDTO dto = converterJsonEmPerfilDTO(conteudo);
		
		assertThat(dto.getIdUsuario()).isNotNull();
		assertThat(dto.getLogin()).isEqualTo("teste");
		assertThat(dto.getPerfis()).isNotEmpty();
		assertThat(dto.getPerfis().size()).isEqualTo(2);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_USUARIO);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_ADMIN);
	}

	@Test
	@Order(3)
	@DisplayName("Removendo os perfis admin e usuário")
	public void removerPerfisDoUsuario() throws Exception {
		String json = "{ \"login\": \"teste\", \"perfis\": [\"USUARIO\", \"ADMINISTRADOR\"] }";

		MvcResult resposta = this.mockMvc
				.perform(MockMvcRequestBuilders.post(UrlConstantes.PERFIS + "/remover")
						.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		String conteudo = resposta.getResponse().getContentAsString();
		PerfilDTO dto = converterJsonEmPerfilDTO(conteudo);
		
		assertThat(dto.getIdUsuario()).isNotNull();
		assertThat(dto.getLogin()).isEqualTo("teste");
		assertThat(dto.getPerfis()).isEmpty();
	}
	
	
	private PerfilDTO converterJsonEmPerfilDTO(String json) {
		PerfilDTO perfil = new PerfilDTO();
		ObjectMapper mapper = new ObjectMapper();

		try {
			perfil = mapper.readValue(json, PerfilDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return perfil;
	}
}
