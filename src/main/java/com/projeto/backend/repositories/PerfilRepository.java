package com.projeto.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.backend.enums.PerfilEnum;
import com.projeto.backend.models.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
	
	Optional<Perfil> findDistinctByNomeAndAtivo(PerfilEnum perfilEnum, boolean ativo);

}
