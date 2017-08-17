package br.com.rest.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.rest.domain.Starship;
 
/**
 * Classe DAO para Espaçonaves.
 * 
 * @author michael.inacio
 *  
 */
 
public class StarshipDAO extends GenericDAO<Starship> {
	
	public Starship findByIdStarship(Integer idStarship, Integer idUser) {
		getEntityManager().clear();
		Query query = getEntityManager().createQuery("select u from Starship u where u.idStarship = :id_starship and u.idUser = :idUser");
		query.setParameter("id_starship", idStarship);
		query.setParameter("idUser", idUser);
		
		try {
			return (Starship) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
}
