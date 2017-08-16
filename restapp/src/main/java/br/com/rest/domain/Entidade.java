package br.com.rest.domain;

import java.io.Serializable;

/**  
 * 
 * Classe de dominio
 * 
 * @author michael.inacio
 *
 */
public abstract class Entidade implements Serializable {

	private static final long serialVersionUID = -568109573379162024L;

	public abstract Serializable getId();

	private boolean somenteId;


	public boolean isSomenteId() {
		return somenteId;
	}

	public void setSomenteId(boolean somenteId) {
		this.somenteId = somenteId;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "; ID = " + getId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entidade other = (Entidade) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}