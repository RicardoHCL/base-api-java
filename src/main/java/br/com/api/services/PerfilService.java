package br.com.api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.models.Perfil;
import br.com.api.repositories.PerfilRepository;

@Service
public class PerfilService {

	@Autowired
	private PerfilRepository repository;

	public Perfil consultarOuCadastrarPerfilPeloNome(Perfil perfil) {
		
		Optional<Perfil> perfilBanco = repository.findDistinctByNomeAndAtivo(perfil.getNome(), true);
		
		if(perfilBanco.isPresent()) {
			return perfilBanco.get();
		}		
		
		return this.repository.save(perfil);

	}

}
