package br.com.api.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.api.dtos.UsuarioDTO;
import br.com.api.models.Usuario;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Testes unitários do dozer converter")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DozerConverterTest {

	private UsuarioDTO usuarioDTO;

	private Usuario usuario;

	private List<UsuarioDTO> listaDtos;

	private List<Usuario> listaUsuarios;

	@BeforeAll
	@DisplayName("Montando listas e objetos para serem convertidos nos testes")
	public void setUp() {
		this.usuarioDTO = new UsuarioDTO("Ricardo", "ricardo", "ricardo@gmail.com", "123456", "123456");
		UsuarioDTO usuarioDTO2 = new UsuarioDTO("Carlos", "carlos", "carlos@gmail.com", "123465", "123465");

		this.usuario = new Usuario(1l, "Fernando", "nando_", "nando@gmail.com", "$2a$10$A3Btshm");
		Usuario usuario2 = new Usuario(2l, "Ana", "aninha", "aninha@gmail.com", "$2a$10$A3gtshm");

		this.listaDtos = Arrays.asList(this.usuarioDTO, usuarioDTO2);
		this.listaUsuarios = Arrays.asList(this.usuario, usuario2);
	}

	@Test
	@DisplayName("Convertendo um objeto DTO para um Objeto Entidade")
	public void converterUsuarioDTOParaUsuario() {
		Usuario usuario = DozerConverter.converterObjeto(this.usuarioDTO, Usuario.class);

		assertThat(usuario.getId()).isNull();
		assertThat(usuario.getNome()).isEqualTo("Ricardo");
		assertThat(usuario.getLogin()).isEqualTo("ricardo");
		assertThat(usuario.getEmail()).isEqualTo("ricardo@gmail.com");
		assertThat(usuario.getSenha()).isEqualTo("123456");
	}

	@Test
	@DisplayName("Convertendo um objeto Entidade para um Objeto DTO")
	public void converterUsuarioParaUsuarioDTO() {
		UsuarioDTO dto = DozerConverter.converterObjeto(this.usuario, UsuarioDTO.class);

		assertThat(dto.getId()).isEqualTo(1l);
		assertThat(dto.getNome()).isEqualTo("Fernando");
		assertThat(dto.getLogin()).isEqualTo("nando_");
		assertThat(dto.getEmail()).isEqualTo("nando@gmail.com");
		assertThat(dto.getSenha()).isEqualTo("$2a$10$A3Btshm");
	}

	@Test
	@DisplayName("Convertendo uma lista de Entidades para uma lista de DTOs")
	public void converterListaUsuarioParaListaUsuarioDTO() {
		List<UsuarioDTO> dtos = DozerConverter.converterListaObjetos(this.listaUsuarios, UsuarioDTO.class);

		assertThat(dtos).isNotEmpty();
		assertThat(dtos.size()).isEqualTo(2);

		UsuarioDTO dto1 = dtos.get(0);

		assertThat(dto1.getId()).isEqualTo(1l);
		assertThat(dto1.getNome()).isEqualTo("Fernando");
		assertThat(dto1.getLogin()).isEqualTo("nando_");
		assertThat(dto1.getEmail()).isEqualTo("nando@gmail.com");
		assertThat(dto1.getSenha()).isEqualTo("$2a$10$A3Btshm");

		UsuarioDTO dto2 = dtos.get(1);

		assertThat(dto2.getId()).isEqualTo(2l);
		assertThat(dto2.getNome()).isEqualTo("Ana");
		assertThat(dto2.getLogin()).isEqualTo("aninha");
		assertThat(dto2.getEmail()).isEqualTo("aninha@gmail.com");
		assertThat(dto2.getSenha()).isEqualTo("$2a$10$A3gtshm");
	}

	@Test
	@DisplayName("Convertendo uma lista de DTOs para uma lista de Entidades")
	public void converterListaUsuarioDTOParaListaUsuario() {
		List<Usuario> usuarios = DozerConverter.converterListaObjetos(this.listaDtos, Usuario.class);

		assertThat(usuarios).isNotEmpty();
		assertThat(usuarios.size()).isEqualTo(2);

		Usuario usuario1 = usuarios.get(0);

		assertThat(usuario1.getId()).isNull();
		assertThat(usuario1.getNome()).isEqualTo("Ricardo");
		assertThat(usuario1.getLogin()).isEqualTo("ricardo");
		assertThat(usuario1.getEmail()).isEqualTo("ricardo@gmail.com");
		assertThat(usuario1.getSenha()).isEqualTo("123456");

		Usuario usuario2 = usuarios.get(1);

		assertThat(usuario2.getId()).isNull();
		assertThat(usuario2.getNome()).isEqualTo("Carlos");
		assertThat(usuario2.getLogin()).isEqualTo("carlos");
		assertThat(usuario2.getEmail()).isEqualTo("carlos@gmail.com");
		assertThat(usuario2.getSenha()).isEqualTo("123465");
	}
}
