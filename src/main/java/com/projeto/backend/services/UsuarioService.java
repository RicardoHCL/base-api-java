package com.projeto.backend.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.backend.constants.MenssagensErrosConstantes;
import com.projeto.backend.converter.DozerConverter;
import com.projeto.backend.dtos.UsuarioDTO;
import com.projeto.backend.exceptions.CustomException;
import com.projeto.backend.models.Usuario;
import com.projeto.backend.repositories.UsuarioRepository;
import com.projeto.backend.services.base.ServiceGenerico;

@Service
public class UsuarioService extends ServiceGenerico<Usuario, UsuarioDTO, Long, UsuarioRepository> {

	@Autowired
	private UsuarioRepository repository;	
	
	@Override
	public UsuarioRepository getRepositorio() {	
		return repository;
	}

	@Override
	public Usuario converterDTOParaEntidade(UsuarioDTO entidadeDTO) {
		return DozerConverter.converterObjeto(entidadeDTO, Usuario.class);
	}

	@Override
	public UsuarioDTO converterEntidadeParaDTO(Usuario entidade) {		
		return DozerConverter.converterObjeto(entidade, UsuarioDTO.class);
	}	
	
	public UsuarioDTO cadastro(UsuarioDTO usuarioDTO) {
		Usuario usuario = converterDTOParaEntidade(usuarioDTO);
		usuario = this.salvar(usuario, null);
		return converterEntidadeParaDTO(usuario);
	}
	
		
	@Override
	protected void validarInclusao(Usuario entidade) throws CustomException {
		Long count = repository.countByEmailAndAtivo(entidade.getEmail(), true);
	
		if(count > 0l) {
			throw new  CustomException(MenssagensErrosConstantes.EMAIL_JA_CADASTRADO);
		}		
	}
	
	@Override
	protected void validarAlteracao(Usuario entidade) throws CustomException {
		Long count = repository.countByEmailAndAtivoAndIdNot(entidade.getEmail(), true, entidade.getId());
		
		if(count > 0l) {
			throw new CustomException(MenssagensErrosConstantes.EMAIL_JA_CADASTRADO);
		}
	}		
		
}
