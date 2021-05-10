package br.com.api.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import br.com.api.dtos.AlteracaoPerfilsDTO;
import br.com.api.dtos.PerfilDTO;
import br.com.api.exceptions.ValidationException;
import br.com.api.models.Perfil;
import br.com.api.repositories.PerfilRepository;

@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerfilServiceTest {

	@Autowired
	private PerfilService service;

	@MockBean
	private PerfilRepository repository;

	@MockBean
	private UsuarioService usuarioService;

	@BeforeEach
	public void init() {
		Perfil perfilUsuario = new Perfil(1l, Perfil.PERFIL_USUARIO);
		Perfil perfilAdmin = new Perfil(2l, Perfil.PERFIL_ADMIN);

		Mockito.when(this.repository.findDistinctByNomeAndAtivo(Perfil.PERFIL_USUARIO, true))
				.thenReturn(Optional.of(perfilUsuario));

		Mockito.when(this.repository.findDistinctByNomeAndAtivo(Perfil.PERFIL_ADMIN, true))
				.thenReturn(Optional.of(new Perfil()));

		Mockito.when(this.repository.save(Mockito.any(Perfil.class))).thenReturn(perfilAdmin);
	}

	@Test
	public void consultarPerfilPeloNome() {
		Perfil perfil = this.service.consultarOuCadastrarPerfilPeloNome(new Perfil(Perfil.PERFIL_USUARIO));

		assertThat(perfil.getId()).isEqualTo(1l);
		assertThat(perfil.getNome()).isEqualTo("USUARIO");
	}

	@Test
	public void cadastrarPerfilPeloNome() {
		Perfil perfil = this.service.consultarOuCadastrarPerfilPeloNome(new Perfil(Perfil.PERFIL_ADMIN));

		assertThat(perfil.getId()).isEqualTo(2l);
		assertThat(perfil.getNome()).isEqualTo("ADMINISTRADOR");
	}

	@Test
	public void erroAoCadastrarPerfilPeloNome() {
		try {
			this.service.consultarOuCadastrarPerfilPeloNome(new Perfil("MODERADOR"));
		} catch (Exception e) {
			assertThat(ValidationException.class).isEqualTo(e.getClass());
			assertThat("Perfil MODERADOR inválido").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void listarPerfisAtivos() {
		// Construindo mocks
		Perfil perfilUsuario = new Perfil(1l, Perfil.PERFIL_USUARIO);
		Perfil perfilAdmin = new Perfil(2l, Perfil.PERFIL_ADMIN);
		List<Perfil> perfis = Arrays.asList(perfilUsuario, perfilAdmin);
		Mockito.when(this.repository.findByAtivo(true)).thenReturn(perfis);

		PerfilDTO dto = this.service.listarPerfisAtivos();

		assertThat(dto.getIdUsuario()).isNull();
		assertThat(dto.getLogin()).isNull();
		assertThat(dto.getPerfis()).isNotEmpty();
		assertThat(dto.getPerfis().size()).isEqualTo(2);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_USUARIO);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_ADMIN);
	}

	@Test
	public void adicionarPerfilAoUsuario() {
		// Construindo mocks
		Perfil perfilUsuario = new Perfil(1l, Perfil.PERFIL_USUARIO);
		Perfil perfilAdmin = new Perfil(2l, Perfil.PERFIL_ADMIN);
		AlteracaoPerfilsDTO altPerfilDTO = new AlteracaoPerfilsDTO(3l, "ricardo",
				Arrays.asList(perfilUsuario, perfilAdmin), false);
		Mockito.when(this.usuarioService.alterarPerfisUsuario(Mockito.any(AlteracaoPerfilsDTO.class)))
				.thenReturn(altPerfilDTO);

		PerfilDTO dto = new PerfilDTO(Arrays.asList(Perfil.PERFIL_ADMIN, Perfil.PERFIL_ADMIN));
		dto.setLogin("ricardo");

		dto = this.service.alterarPerfil(dto, false);

		assertThat(dto.getIdUsuario()).isEqualTo(3l);
		assertThat(dto.getLogin()).isEqualTo("ricardo");
		assertThat(dto.getPerfis()).isNotEmpty();
		assertThat(dto.getPerfis().size()).isEqualTo(2);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_USUARIO);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_ADMIN);
	}

	@Test
	public void erroAoAdicionarPerfilAoUsuarioT01() {
		try {
			PerfilDTO dto = new PerfilDTO(Arrays.asList(Perfil.PERFIL_ADMIN, Perfil.PERFIL_ADMIN));
			this.service.alterarPerfil(dto, false);

		} catch (Exception e) {
			assertThat(ValidationException.class).isEqualTo(e.getClass());
			assertThat("Login ou id do usuário não informado").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void erroAoAdicionarPerfilAoUsuarioT02() {
		try {
			PerfilDTO dto = new PerfilDTO(Arrays.asList(Perfil.PERFIL_ADMIN, "MODERADOR"));
			dto.setLogin("ricardo");
			this.service.alterarPerfil(dto, false);

		} catch (Exception e) {
			assertThat(ValidationException.class).isEqualTo(e.getClass());
			assertThat("Perfil MODERADOR inválido").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void removerPerfilAoUsuario() {
		// Construindo mocks
		Perfil perfilUsuario = new Perfil(1l, Perfil.PERFIL_USUARIO);
		AlteracaoPerfilsDTO altPerfilDTO = new AlteracaoPerfilsDTO(3l, "ricardo", Arrays.asList(perfilUsuario), true);
		Mockito.when(this.usuarioService.alterarPerfisUsuario(Mockito.any(AlteracaoPerfilsDTO.class)))
				.thenReturn(altPerfilDTO);

		PerfilDTO dto = new PerfilDTO(Arrays.asList(Perfil.PERFIL_ADMIN));
		dto.setIdUsuario(3l);

		dto = this.service.alterarPerfil(dto, true);

		assertThat(dto.getIdUsuario()).isEqualTo(3l);
		assertThat(dto.getLogin()).isEqualTo("ricardo");
		assertThat(dto.getPerfis()).isNotEmpty();
		assertThat(dto.getPerfis().size()).isEqualTo(1);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_USUARIO);
	}

}
