package br.com.api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.dtos.PerfilDTO;
import br.com.api.models.Perfil;
import br.com.api.repositories.PerfilRepository;
import br.com.api.services.base.ServiceGenerico;

@Service
public class PerfilService extends ServiceGenerico<Perfil, PerfilDTO, Long, PerfilRepository> {

	@Autowired
	private PerfilRepository repository;

	public Perfil consultarOuCadastrarPerfilPeloNome(Perfil perfil) {
		Optional<Perfil> perfilBanco = repository.findDistinctByNomeAndAtivo(perfil.getNome(), true);

		if (perfilBanco.isPresent()) {
			return perfilBanco.get();
		}
		return this.salvar(perfil, null);
	}

	@Override
	public PerfilRepository getRepositorio() {
		return repository;
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
