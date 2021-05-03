package br.com.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.constants.ExceptionsConstantes;
import br.com.api.constants.ValidacaoConstantes;
import br.com.api.converter.DozerConverter;
import br.com.api.dtos.UsuarioDTO;
import br.com.api.enums.PerfilEnum;
import br.com.api.exceptions.CustomException;
import br.com.api.exceptions.ValidationException;
import br.com.api.models.Perfil;
import br.com.api.models.Usuario;
import br.com.api.repositories.UsuarioRepository;
import br.com.api.services.base.ServiceGenerico;
import br.com.api.utils.Utils;
import br.com.api.utils.ValidacaoUtils;

@Service
public class UsuarioService extends ServiceGenerico<Usuario, UsuarioDTO, Long, UsuarioRepository> {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private PerfilService perfilService;

	@Override
	public UsuarioRepository getRepositorio() {
		return repository;
	}

	public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
		Usuario usuario = converterDTOParaEntidade(usuarioDTO);
		usuario = this.salvar(usuario, null);
		return converterEntidadeParaDTO(usuario);
	}

	public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO) {
		Usuario usuario = converterDTOParaEntidade(usuarioDTO);
		usuario = this.validarCamposAlterados(usuario);
		usuario = this.salvar(usuario, Utils.getUsuarioLogado());
		return converterEntidadeParaDTO(usuario);
	}

	@Override
	public List<UsuarioDTO> consultarTodos() {
		return this.converterListaEntidadeParaListaDTO(this.repository.findByAtivo(true));
	}

	public UsuarioDTO consultar(Long id) {
		Optional<Usuario> usuario = this.repository.findDistinctByIdAndAtivo(id, true);

		if (usuario.isPresent()) {
			return converterEntidadeParaDTO(usuario.get());
		}

		throw new EntityNotFoundException(ExceptionsConstantes.USUARIO_NAO_ENCONTRADO);
	}

	public Usuario consultarPorLogin(String login) {
		Optional<Usuario> usuario = repository.findDistinctByLoginAndAtivo(login, true);
		if (usuario.isPresent()) {
			return usuario.get();
		}

		throw new EntityNotFoundException(ExceptionsConstantes.EMAIL_INVALIDO);
	}

	@Override
	protected void resolverPreDependencias(Usuario entidade) throws CustomException {
		this.vincularPerfisAoUsuario(entidade);
	}

	private void vincularPerfisAoUsuario(Usuario usuario) {
		// Em caso de cadastro de usuario, o mesmo inicia com perfil de usuario
		if (usuario.getListaPerfis() == null || usuario.getListaPerfis().isEmpty()) {
			List<Perfil> perfis = new ArrayList<>();
			Perfil perfil = new Perfil(PerfilEnum.USUARIO);
			perfis.add(perfil);
			usuario.setListaPerfis(perfis);
		}

		List<Perfil> perfis = new ArrayList<>();

		for (Perfil perfil : usuario.getListaPerfis()) {
			perfis.add(this.perfilService.consultarOuCadastrarPerfilPeloNome(perfil));
		}

		usuario.setListaPerfis(perfis);
	}

	@Override
	protected void validarInclusao(Usuario entidade) throws CustomException {
		this.validarUnicidade(entidade);

		if (!entidade.getSenha().equals(entidade.getConfirmacaoSenha())) {
			throw new ValidationException(ValidacaoConstantes.SENHA_E_CONFIRMACAO_SENHA_DIFERENTES);
		}

		String hashSenha = Utils.gerarHashSenha(entidade.getSenha());
		entidade.setSenha(hashSenha);
	}

	@Override
	protected Usuario validarCamposAlterados(Usuario entidade) {

		Optional<Usuario> usuarioDesatualizado = this.repository.findDistinctByIdAndAtivo(entidade.getId(), true);
		usuarioDesatualizado
				.orElseThrow(() -> new EntityNotFoundException(ExceptionsConstantes.USUARIO_NAO_ENCONTRADO));

		Usuario usuarioAtualizado = usuarioDesatualizado.get();

		if (entidade.getNome() != null) {
			if (!ValidacaoUtils.isCampoStringValido(entidade.getNome())) {
				throw new ValidationException(ValidacaoConstantes.NOME_INVALIDO);
			}

			usuarioAtualizado.setNome(entidade.getNome());
		}

		if (entidade.getEmail() != null) {
			if (entidade.getEmail().isBlank() || !ValidacaoUtils.isEmailValido(entidade.getEmail())) {
				throw new ValidationException(ValidacaoConstantes.EMAIL_INVALIDO);
			}
			usuarioAtualizado.setEmail(entidade.getEmail());
		}

		if (entidade.getNovaSenha() != null) {
			this.validarTrocarSenha(entidade, usuarioAtualizado.getSenha());
			usuarioAtualizado.setSenha(entidade.getSenha());
		}

		return usuarioAtualizado;
	}

	@Override
	protected void validarAlteracao(Usuario entidade) throws CustomException {
		this.validarUnicidade(entidade);
	}

	private void validarTrocarSenha(Usuario usuario, String hashSenhaAtual) {

		if (!Utils.isSenhasIdenticas(usuario.getSenha(), hashSenhaAtual)) {
			throw new ValidationException(ValidacaoConstantes.SENHA_INCORRETA);
		}

		if (!usuario.getNovaSenha().equals(usuario.getConfirmacaoSenha())) {
			throw new ValidationException(ValidacaoConstantes.SENHA_E_CONFIRMACAO_SENHA_DIFERENTES);
		}

		String hashNovaSenha = Utils.gerarHashSenha(usuario.getNovaSenha());

		usuario.setSenha(hashNovaSenha);
	}

	@Override
	protected void validarUnicidade(Usuario entidade) throws CustomException {
		Long idUsuario = entidade.getId() == null ? 0l : entidade.getId();
		Long count = repository.countByEmailAndIdNot(entidade.getEmail(), idUsuario);

		if (count > 0l) {
			throw new CustomException(ExceptionsConstantes.EMAIL_JA_CADASTRADO);
		}
		
		count = repository.countByLoginAndIdNot(entidade.getLogin(), idUsuario);

		if (count > 0l) {
			throw new CustomException(ExceptionsConstantes.LOGIN_JA_CADASTRADO);
		}
		
	}

	@Override
	public Usuario converterDTOParaEntidade(UsuarioDTO entidadeDTO) {
		return DozerConverter.converterObjeto(entidadeDTO, Usuario.class);
	}

	@Override
	public UsuarioDTO converterEntidadeParaDTO(Usuario entidade) {
		return DozerConverter.converterObjeto(entidade, UsuarioDTO.class);
	}

	@Override
	protected List<UsuarioDTO> converterListaEntidadeParaListaDTO(List<Usuario> listaEntidades) throws CustomException {
		return DozerConverter.converterListaObjetos(listaEntidades, UsuarioDTO.class);
	}

}
