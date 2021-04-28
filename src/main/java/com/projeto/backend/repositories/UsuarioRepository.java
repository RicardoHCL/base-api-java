package com.projeto.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.backend.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmailAndAtivo(String email, boolean ativo);
	List<Usuario> findByAtivo(boolean ativo);
}
