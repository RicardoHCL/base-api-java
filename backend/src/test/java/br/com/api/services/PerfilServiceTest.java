package br.com.api.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Testes unitários do perfil")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerfilServiceTest {

	@Autowired
	private PerfilService service;

	@MockBean
	private PerfilRepository repository;

	@MockBean
	private UsuarioService usuarioService;

	//"Usuários salvos"
	private Perfil perfilUsuario = new Perfil(1l, Perfil.PERFIL_USUARIO);
	private Perfil perfilAdmin = new Perfil(2l, Perfil.PERFIL_ADMIN);

	@BeforeEach
	@DisplayName("Preparando para iniciar os testes com algumas informações mocadas")
	public void setUp() {
		Mockito.when(this.repository.findDistinctByNomeAndAtivo(Perfil.PERFIL_USUARIO, true))
				.thenReturn(Optional.of(this.perfilUsuario));

		Mockito.when(this.repository.findDistinctByNomeAndAtivo(Perfil.PERFIL_ADMIN, true))
				.thenReturn(Optional.of(new Perfil()));

		Mockito.when(this.repository.save(Mockito.any(Perfil.class))).thenReturn(this.perfilAdmin);
	}

	@Test
	@Order(1)
	@DisplayName("Consultando perfil pelo nome")
	public void consultarPerfilPeloNome() {
		Perfil perfil = this.service.consultarOuCadastrarPerfilPeloNome(new Perfil(Perfil.PERFIL_USUARIO));

		assertThat(perfil.getId()).isEqualTo(1l);
		assertThat(perfil.getNome()).isEqualTo("USUARIO");
	}

	@Test
	@Order(2)
	@DisplayName("Cadastrando perfil pelo nome")
	public void cadastrarPerfilPeloNome() {
		Perfil perfil = this.service.consultarOuCadastrarPerfilPeloNome(new Perfil(Perfil.PERFIL_ADMIN));

		assertThat(perfil.getId()).isEqualTo(2l);
		assertThat(perfil.getNome()).isEqualTo("ADMINISTRADOR");
	}

	@Test
	@Order(3)
	@DisplayName("Falha ao cadastrar perfil com nome inválido")
	public void erroAoCadastrarPerfilPeloNome() {
		try {
			this.service.consultarOuCadastrarPerfilPeloNome(new Perfil("MODERADOR"));
		} catch (Exception e) {
			assertThat(ValidationException.class).isEqualTo(e.getClass());
			assertThat("Perfil MODERADOR inválido").isEqualTo(e.getMessage());
		}
	}

	@Test
	@Order(4)
	@DisplayName("Listando os perfis ativos")
	public void listarPerfisAtivos() {
		Mockito.when(this.repository.findByAtivo(true)).thenReturn(Arrays.asList(this.perfilUsuario, this.perfilAdmin));

		PerfilDTO dto = this.service.listarPerfisAtivos();

		assertThat(dto.getIdUsuario()).isNull();
		assertThat(dto.getLogin()).isNull();
		assertThat(dto.getPerfis()).isNotEmpty();
		assertThat(dto.getPerfis().size()).isEqualTo(2);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_USUARIO);
		assertThat(dto.getPerfis()).contains(Perfil.PERFIL_ADMIN);
	}

	@Test
	@Order(5)
	@DisplayName("Adicionando perfis em um usuário")
	public void adicionarPerfilAoUsuario() {
		// Construindo mocks
		AlteracaoPerfilsDTO altPerfilDTO = new AlteracaoPerfilsDTO(3l, "ricardo",
				Arrays.asList(this.perfilUsuario, this.perfilAdmin), false);
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
	@Order(6)
	@DisplayName("Falha ao adicionar perfis sem informar o id ou login do usuário")
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
	@Order(7)
	@DisplayName("Falha ao adicionar perfis, informando um perfil inválido")
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
	@Order(8)
	@DisplayName("Removendo perfis de um usuário")
	public void removerPerfilUsuario() {
		// Construindo mocks
		AlteracaoPerfilsDTO altPerfilDTO = new AlteracaoPerfilsDTO(3l, "ricardo", Arrays.asList(this.perfilUsuario), true);
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
