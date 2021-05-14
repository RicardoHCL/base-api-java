package br.com.api.models.base;

import java.io.Serializable;

public interface IEntidade<ID extends Serializable > extends Serializable {

	public ID getId();

	public void setId(ID id);

}
