package br.com.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findDistinctByLoginAndAtivo(String login, boolean ativo);
	Long countByEmailAndIdNot(String email, Long id);
	Long countByLoginAndIdNot(String login, Long id);
	List<Usuario> findByAtivo(boolean ativo);
}
