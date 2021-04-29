package com.projeto.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.backend.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Long countByEmailAndAtivo(String email, boolean ativo);
	Long countByEmailAndAtivoAndIdNot(String email, boolean ativo, Long id);
	List<Usuario> findByAtivo(boolean ativo);
}
