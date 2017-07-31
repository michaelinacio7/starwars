package br.com.rest.dto;

import java.io.Serializable;
import java.util.List;

public class EspaconaveDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer idEspaconave;
	
	private String nome;

	private Integer idUser;
	
	private List<TripulanteDTO> tripulantes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<TripulanteDTO> getTripulantes() {
		return tripulantes;
	}

	public void setTripulantes(List<TripulanteDTO> tripulantes) {
		this.tripulantes = tripulantes;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public Integer getIdEspaconave() {
		return idEspaconave;
	}

	public void setIdEspaconave(Integer idEspaconave) {
		this.idEspaconave = idEspaconave;
	}

}
