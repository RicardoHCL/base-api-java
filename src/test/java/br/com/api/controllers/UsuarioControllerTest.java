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
import br.com.api.dtos.UsuarioDTO;
import br.com.api.models.Usuario;
import br.com.api.repositories.UsuarioRepository;
import br.com.api.utils.Utils;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Testes de ingração do usuário")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioControllerTest {

	private MockMvc mockMvc;

	private Usuario usuario;

	@Autowired
	private UsuarioController controller;
	
	@Autowired
	private UsuarioRepository repository;

	@BeforeAll
	@DisplayName("Preparando para iniciar os testes com um usuário salvo no banco")
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		String json = "{ \"nome\": \"Teste\"," + "  \"login\": \"teste\"," + " \"email\": \"teste@gmail.com\","
				+ " \"senha\": \"123456\"," + " \"confirmacaoSenha\": \"123456\" }";

		MvcResult resposta = this.mockMvc.perform(MockMvcRequestBuilders.post(UrlConstantes.USUARIOS)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String conteudo = resposta.getResponse().getContentAsString();
		UsuarioDTO dto = converterJsonEmUsuarioDTO(conteudo);

		this.usuario = this.repository.findById(dto.getId()).get();
	}

	@Test
	@Order(1)
	@DisplayName("Criando usuário com sucesso")
	public void criarUsuario() throws Exception {
		String json = "{ \"nome\": \"Ricardo Lima\"," + "  \"login\": \"ricardo\","
				+ " \"email\": \"ricardo@gmail.com\"," + " \"senha\": \"123456\","
				+ " \"confirmacaoSenha\": \"123456\" }";

		MvcResult resposta = this.mockMvc.perform(MockMvcRequestBuilders.post(UrlConstantes.USUARIOS)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String conteudo = resposta.getResponse().getContentAsString();
		UsuarioDTO dto = converterJsonEmUsuarioDTO(conteudo);

		assertThat(dto.getId()).isNotNull();
		assertThat(dto.getNome()).isEqualTo("Ricardo Lima");
		assertThat(dto.getLogin()).isEqualTo("ricardo");
		assertThat(dto.getEmail()).isEqualTo("ricardo@gmail.com");
	}

	@Test
	@Order(2)
	@DisplayName("Consultando usuário existente no banco")
	public void consultar() throws Exception {
		MvcResult resposta = this.mockMvc
				.perform(MockMvcRequestBuilders.get(UrlConstantes.USUARIOS + "/" + this.usuario.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		String conteudo = resposta.getResponse().getContentAsString();
		UsuarioDTO dto = converterJsonEmUsuarioDTO(conteudo);

		assertThat(dto.getId()).isEqualTo(this.usuario.getId());
		assertThat(dto.getNome()).isEqualTo("Teste");
		assertThat(dto.getLogin()).isEqualTo("teste");
		assertThat(dto.getEmail()).isEqualTo("teste@gmail.com");
	}
	
	@Test
	@Order(3)
	@DisplayName("Atualizando o nome do usuário")
	public void atualizarNome() throws Exception {
		String json = "{ \"nome\": \"Teste Atualizado\" }";
		
		MvcResult resposta = this.mockMvc.perform(MockMvcRequestBuilders.put(UrlConstantes.USUARIOS + "/" + this.usuario.getId())
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		String conteudo = resposta.getResponse().getContentAsString();
		UsuarioDTO dto = converterJsonEmUsuarioDTO(conteudo);

		assertThat(dto.getId()).isEqualTo(this.usuario.getId());
		assertThat(dto.getNome()).isEqualTo("Teste Atualizado");
		assertThat(dto.getLogin()).isEqualTo("teste");
		assertThat(dto.getEmail()).isEqualTo("teste@gmail.com");
	}
	
	@Test
	@Order(4)
	@DisplayName("Atualizando a senha do usuário")
	public void atualizarSenha() throws Exception {
		String json = "{ \"senha\": \"123456\", \"confirmacaoSenha\": \"12345678\",  \"novaSenha\": \"12345678\" }";
		
		MvcResult resposta = this.mockMvc.perform(MockMvcRequestBuilders.put(UrlConstantes.USUARIOS + "/" + this.usuario.getId())
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		String conteudo = resposta.getResponse().getContentAsString();
		UsuarioDTO dto = converterJsonEmUsuarioDTO(conteudo);
		
		Usuario usuario = this.repository.findById(dto.getId()).get();
		
		boolean isSenhaAlterada = Utils.isSenhasIdenticas("12345678", usuario.getSenha());

		assertThat(dto.getId()).isEqualTo(this.usuario.getId());
		assertThat(isSenhaAlterada).isTrue();
	}
	
	@Test
	@Order(5)
	@DisplayName("listando os usuários ativos")
	public void listarUsuariosAtivos() throws Exception {
		MvcResult resposta = this.mockMvc
				.perform(MockMvcRequestBuilders.get(UrlConstantes.USUARIOS))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		
		String conteudo = resposta.getResponse().getContentAsString();
		UsuarioDTO[] usuarios = converterJsonEmListaUsuarioDTO(conteudo);
		
		assertThat(usuarios.length).isEqualTo(2);
	}
	
	@Test
	@Order(6)
	@DisplayName("Fazer a exclusão lógica do usuário")
	public void excluirUsuario() throws Exception {
		 this.mockMvc
			.perform(MockMvcRequestBuilders.delete(UrlConstantes.USUARIOS + "/" + this.usuario.getId()))
			.andExpect(MockMvcResultMatchers.status().isOk());
		 
		Long qdtUsuariosAtivos = this.repository.countByAtivo(true);
		
		assertThat(qdtUsuariosAtivos).isEqualTo(1);
	}

	public static UsuarioDTO converterJsonEmUsuarioDTO(String json) {
		UsuarioDTO usuario = new UsuarioDTO();
		ObjectMapper mapper = new ObjectMapper();

		try {
			usuario = mapper.readValue(json, UsuarioDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return usuario;
	}
	
	private UsuarioDTO[] converterJsonEmListaUsuarioDTO(String json) {
		UsuarioDTO[] usuarios = null ;
		ObjectMapper mapper = new ObjectMapper();

		try {
			usuarios = mapper.readValue(json, UsuarioDTO[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return usuarios;
	}

}
