package br.com.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.models.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
	
	Optional<Perfil> findDistinctByNomeAndAtivo(String nome, boolean ativo);

}
