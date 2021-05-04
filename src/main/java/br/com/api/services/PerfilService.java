package br.com.api.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.constants.ExceptionsConstantes;
import br.com.api.dtos.AlteracaoPerfilsDTO;
import br.com.api.dtos.PerfilDTO;
import br.com.api.exceptions.CustomException;
import br.com.api.exceptions.ValidationException;
import br.com.api.models.Perfil;
import br.com.api.repositories.PerfilRepository;
import br.com.api.services.base.ServiceGenerico;
import static br.com.api.utils.ValidacaoUtils.isIdValido;
import static br.com.api.utils.ValidacaoUtils.isCampoStringValido;
import static br.com.api.utils.ValidacaoUtils.isPerfilValido;

@Service
public class PerfilService extends ServiceGenerico<Perfil, PerfilDTO, Long, PerfilRepository> {

	@Autowired
	private PerfilRepository repository;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public PerfilRepository getRepositorio() {
		return repository;
	}

	public PerfilDTO listarPerfisAtivos() {
		List<Perfil> perfis = this.repository.findByAtivo(true);
		return converterListaPerfisParaPerfilDTO(perfis);
	}

	public Perfil consultarOuCadastrarPerfilPeloNome(Perfil perfil) {
		Optional<Perfil> perfilBanco = repository.findDistinctByNomeAndAtivo(perfil.getNome(), true);

		if (perfilBanco.isPresent()) {
			return perfilBanco.get();
		}
		return this.salvar(perfil, null);
	}

	public PerfilDTO alterarPerfil(PerfilDTO perfilDTO, boolean isRemocao) {
		List<Perfil> perfis = new ArrayList<>();

		this.validarInformacoesUsuario(perfilDTO);

		for (String nomePerfil : perfilDTO.getPerfis()) {
			if (!isPerfilValido(nomePerfil)) {
				throw new ValidationException(MessageFormat.format(ExceptionsConstantes.PERFIL_INVALIDO, nomePerfil));
			}

			Perfil perfil = this.consultarOuCadastrarPerfilPeloNome(new Perfil(nomePerfil));
			perfis.add(perfil);
		}
		AlteracaoPerfilsDTO altPerfilDTO = new AlteracaoPerfilsDTO(perfilDTO.getIdUsuario(), perfilDTO.getLogin(),
				perfis, isRemocao);

		altPerfilDTO = this.usuarioService.alterarPerfisUsuario(altPerfilDTO);

		return this.montarPerfilDTO(altPerfilDTO);
	}

	private PerfilDTO montarPerfilDTO(AlteracaoPerfilsDTO altPerfilDTO) {
		PerfilDTO perfilDTO = new PerfilDTO(altPerfilDTO.getIdUsuario(), altPerfilDTO.getLogin());
		List<String> nomePerfis = new ArrayList<>();

		for (Perfil perfil : altPerfilDTO.getPerfis()) {
			nomePerfis.add(perfil.getNome());
		}

		perfilDTO.setPerfis(nomePerfis);

		return perfilDTO;
	}

	private void validarInformacoesUsuario(PerfilDTO perfilDTO) {
		if (!(isIdValido(perfilDTO.getIdUsuario()) || isCampoStringValido(perfilDTO.getLogin()))) {
			throw new ValidationException(ExceptionsConstantes.LOGIN_OU_ID_NAO_INFORMADO);
		}
	}

	private PerfilDTO converterListaPerfisParaPerfilDTO(List<Perfil> perfis) throws CustomException {
		List<String> listaNomesPerfis = new ArrayList<>();
		
		for (Perfil perfil : perfis) {
			listaNomesPerfis.add(perfil.getNome());
		}
		
		return new PerfilDTO(listaNomesPerfis);
	}

	@Override
	public Perfil converterDTOParaEntidade(PerfilDTO entidadeDTO) {
		return null;
	}

	@Override
	public PerfilDTO converterEntidadeParaDTO(Perfil entidade) {
		return null;
	}

}
