package br.com.rest.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "people", schema = "starwars")
public class People extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@JoinColumn(name = "starship")
	@ManyToOne(fetch = FetchType.LAZY)
	private Starship starship;
	
	@Column(name = "id_people")
	private Integer idPeople;
	
	@Column(name = "id_user")
	private Integer idUser;
	
	@Column(name = "name")
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Starship getStarship() {
		return starship;
	}

	public void setStarship(Starship starship) {
		this.starship = starship;
	}

	public Integer getIdPeople() {
		return idPeople;
	}

	public void setIdPeople(Integer idPeople) {
		this.idPeople = idPeople;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
