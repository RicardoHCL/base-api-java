package br.com.api.converter;

import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DozerConverter {

	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

	/**
	 * Converter uma lista de objetos para uma lista de DTO's e vice versa
	 *
	 * @param listaOrigens
	 * @param destino
	 * @return
	 */
	public static <O, D> List<D> converterListaObjetos(List<O> listaOrigens, Class<D> destino) {

		List<D> listaDestinos = new ArrayList<D>();
		for (O origem : listaOrigens) {
			listaDestinos.add(mapper.map(origem, destino));
		}
		return listaDestinos;
	}

	/**
	 * Converter um unico objeto para o DTO e vice versa
	 *
	 * @param origem
	 * @param destino
	 * @return
	 */
	public static <O, D> D converterObjeto(O origem, Class<D> destino) {

		return mapper.map(origem, destino);
	}
}
