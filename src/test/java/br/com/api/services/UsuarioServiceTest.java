package br.com.api.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import br.com.api.dtos.UsuarioDTO;
import br.com.api.exceptions.CustomException;
import br.com.api.exceptions.ValidationException;
import br.com.api.models.Perfil;
import br.com.api.models.Usuario;
import br.com.api.repositories.UsuarioRepository;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioServiceTest {

	@Autowired
	private UsuarioService service;

	@MockBean
	private UsuarioRepository repository;

	@MockBean
	private PerfilService perfilService;

	@Test
	public void criarUsuario() {
		// Construindo mocks
		Usuario usuario = new Usuario(1l, "Ricardo Lima", "ricardo", "ricardo@gmail.com",
				"$2a$10$A3BtshmFkCkcmWkDLfzA6OoS0xIEVPvc/rh2lbITuzoNqSFHjuizC");
		Mockito.when(this.repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		Mockito.when(this.perfilService.consultarOuCadastrarPerfilPeloNome(Mockito.any(Perfil.class)))
				.thenReturn(new Perfil(1l, Perfil.PERFIL_USUARIO));
		Mockito.when(this.repository.countByEmailAndIdNot(Mockito.anyString(), Mockito.anyLong())).thenReturn(0l);
		Mockito.when(this.repository.countByLoginAndIdNot(Mockito.anyString(), Mockito.anyLong())).thenReturn(0l);

		UsuarioDTO dto = new UsuarioDTO("Ricardo Lima", "ricardo", "ricardo@gmail.com", "123456", "123456");
		dto = this.service.criarUsuario(dto);

		assertThat(dto.getId()).isNotNull();
		assertThat(dto.getNome()).isEqualTo("Ricardo Lima");
		assertThat(dto.getLogin()).isEqualTo("ricardo");
		assertThat(dto.getEmail()).isEqualTo("ricardo@gmail.com");
	}

	@Test
	public void erroAoCriarUsuarioT01() {
		Mockito.when(this.repository.countByEmailAndIdNot(Mockito.anyString(), Mockito.anyLong())).thenReturn(1l);

		try {
			UsuarioDTO dto = new UsuarioDTO("Ricardo Lima", "ricardo", "ricardo@gmail.com", "123456", "123456");
			dto = this.service.criarUsuario(dto);
		} catch (Exception e) {
			assertThat(CustomException.class).isEqualTo(e.getClass());
			assertThat("Email informado já cadastrado").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void erroAoCriarUsuarioT02() {
		Mockito.when(this.repository.countByLoginAndIdNot(Mockito.anyString(), Mockito.anyLong())).thenReturn(1l);

		try {
			UsuarioDTO dto = new UsuarioDTO("Ricardo Lima", "ricardo", "ricardo@gmail.com", "123456", "123456");
			dto = this.service.criarUsuario(dto);
		} catch (Exception e) {
			assertThat(CustomException.class).isEqualTo(e.getClass());
			assertThat("Login informado já cadastrado").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void erroAoCriarUsuarioT03() {
		try {
			UsuarioDTO dto = new UsuarioDTO("Ricardo Lima", "ricardo", "ricardo@gmail.com", "123456", "1234567");
			dto = this.service.criarUsuario(dto);
		} catch (Exception e) {
			assertThat(ValidationException.class).isEqualTo(e.getClass());
			assertThat("Confirmação de senha diferente da senha").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void erroAoCriarUsuarioT04() {
		try {
			UsuarioDTO dto = new UsuarioDTO("", "ricardo", "ricardo@gmail.com", "123456", "1234567");
			dto = this.service.criarUsuario(dto);
		} catch (Exception e) {
			assertThat(ValidationException.class).isEqualTo(e.getClass());
			assertThat("O campo nome deve ter entre 3 e 20 caracteres").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void erroAoCriarUsuarioT05() {
		try {
			UsuarioDTO dto = new UsuarioDTO("Ricardo Lima", null, "ricardo@gmail.com", "123456", "1234567");
			dto = this.service.criarUsuario(dto);
		} catch (Exception e) {
			assertThat(ValidationException.class).isEqualTo(e.getClass());
			assertThat("O campo login deve ter entre 5 e 10 caracteres").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void erroAoCriarUsuarioT06() {
		try {
			UsuarioDTO dto = new UsuarioDTO("Ricardo Lima", "ricardo", "ri", "123456", "1234567");
			dto = this.service.criarUsuario(dto);
		} catch (Exception e) {
			assertThat(ValidationException.class).isEqualTo(e.getClass());
			assertThat("Email inválido").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void listarTodosUsuariosAtivos() {
		Usuario usuario1 = new Usuario(1l, "teste", "teste", "teste@gmail.com",
				"$2a$10$A3BtshmFkCkcmWkDLfzA6OoS0xIEVPvc/rh2lbITuzoNqSFHjuizC");
		Usuario usuario2 = new Usuario(2l, "Ricardo Lima", "ricardo", "ricardo@gmail.com",
				"$2a$10$A3BtshmFkCkcmWkDLfzA6OoS0xIEVPvc/rh2lbITuzoNqSFHjuizC");
		Mockito.when(this.repository.findByAtivo(true)).thenReturn(Arrays.asList(usuario1, usuario2));

		List<UsuarioDTO> usuarios = this.service.consultarTodos();

		assertThat(usuarios).isNotEmpty();
		assertThat(usuarios.size()).isEqualTo(2);
	}

	@Test
	public void consultarPorId() {
		Usuario usuario = new Usuario(2l, "Ricardo Lima", "ricardo", "ricardo@gmail.com",
				"$2a$10$A3BtshmFkCkcmWkDLfzA6OoS0xIEVPvc/rh2lbITuzoNqSFHjuizC");
		Mockito.when(this.repository.findDistinctByIdAndAtivo(Mockito.anyLong(), Mockito.anyBoolean())).thenReturn(Optional.of(usuario));

		UsuarioDTO dto = this.service.consultar(2l);

		assertThat(dto.getId()).isEqualTo(2l);
		assertThat(dto.getNome()).isEqualTo("Ricardo Lima");
		assertThat(dto.getLogin()).isEqualTo("ricardo");
		assertThat(dto.getEmail()).isEqualTo("ricardo@gmail.com");
	}

	@Test
	public void erroAoConsultarPorId() {
		Mockito.when(this.repository.findDistinctByIdAndAtivo(Mockito.anyLong(), Mockito.anyBoolean())).thenReturn(Optional.of(new Usuario()));

		try {
			this.service.consultar(5l);
		} catch (Exception e) {
			assertThat(EntityNotFoundException.class).isEqualTo(e.getClass());
			assertThat("Usuário não encontrado").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void consultarPorLogin() {
		Usuario usuario = new Usuario(2l, "Ricardo Lima", "ricardo", "ricardo@gmail.com",
				"$2a$10$A3BtshmFkCkcmWkDLfzA6OoS0xIEVPvc/rh2lbITuzoNqSFHjuizC");
		Mockito.when(this.repository.findDistinctByLoginAndAtivo(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(Optional.of(usuario));

		usuario = this.service.consultarPorLogin("ricardo");

		assertThat(usuario.getId()).isEqualTo(2l);
		assertThat(usuario.getNome()).isEqualTo("Ricardo Lima");
		assertThat(usuario.getLogin()).isEqualTo("ricardo");
		assertThat(usuario.getEmail()).isEqualTo("ricardo@gmail.com");
	}

	@Test
	public void erroAoConsultarPorLogin() {
		Mockito.when(this.repository.findDistinctByLoginAndAtivo(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(Optional.of(new Usuario()));

		try {
			this.service.consultarPorLogin("ricardo");
		} catch (Exception e) {
			assertThat(EntityNotFoundException.class).isEqualTo(e.getClass());
			assertThat("Login inválido").isEqualTo(e.getMessage());
		}
	}
	
	//public void al
	

}
