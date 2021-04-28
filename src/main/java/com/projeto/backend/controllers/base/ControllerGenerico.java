package com.projeto.backend.controllers.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.projeto.backend.exceptions.CustomException;

public abstract class ControllerGenerico<MODEL, REQUEST, RESPONSE, ID extends Serializable> {

	@PutMapping("/{id}")
	public abstract RESPONSE atualizar(REQUEST body);

	@PostMapping
	public abstract RESPONSE cadastrar(REQUEST body);

	@GetMapping("/{id}")
	public abstract RESPONSE consultar(ID id);

	@DeleteMapping("/{id}")
	public abstract void deletar(ID id);

	@GetMapping
	public abstract List<RESPONSE> listar();
	
	
	// Utilizado para conversão do input em model e do model em response
	public MODEL converterRequestParaModel(REQUEST request) throws CustomException { return null; }
	
	public RESPONSE converterModelParaResponse(MODEL model) throws CustomException { return null; }	

	public List<MODEL> converterListaRequestsParaListaModels(List<REQUEST> listaRequests) throws CustomException { return null; }

	public List<RESPONSE> converterListaModelsParaListaResponses(List<MODEL> listaModels) throws CustomException { return null; }
	// --
	
}
