package com.projeto.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.backend.models.Perfil;
import com.projeto.backend.repositories.PerfilRepository;

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
