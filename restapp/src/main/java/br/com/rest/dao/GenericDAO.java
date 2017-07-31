package br.com.rest.dao;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.rest.domain.Entidade;

/**
 * Classe DAO generica, faz operações CRUD.
 * @author michael.inacio	
 *
 * @param <T>
 */
public class GenericDAO<T extends Entidade> {

	
	private static EntityManager entityManager;

	private Class<T> persisteceClass = null;
	
	public static EntityManager getEntityManager() {
		return entityManager;
	}
	
	public static void inicializarEntityManager() {
		entityManager = Persistence.createEntityManagerFactory("restapi").createEntityManager();
	}
	
	// Método que efetua a inserção no banco de dados
	public T insert(T entidade) {
		getEntityManager().getTransaction().begin(); // Inicia a transação
		entidade = getEntityManager().merge(entidade);
		System.out.println("insert:  " + entidade.toString());
		getEntityManager().getTransaction().commit();// Commit
		return entidade;
	}

	// Método que efetua a atualização dos dados no banco de dados
	public T update(T entidade) {
		getEntityManager().getTransaction().begin();
		entidade = getEntityManager().merge(entidade); // MERGE: Caso não tenha
														// ID é efetuado a
														// inserção do mesmo,
														// caso contrario é
														// atualizado o mesmo
		getEntityManager().getTransaction().commit();
		return entidade;
	}

	// Método que efetua a busca pelo ID no banco de dados
	public T findById(Serializable id) {
		getEntityManager().clear();
		T entidade = getEntityManager().find(getPersistenceClass(), id);
		return entidade;
	}

	// Método que efetua a remoção dos dados do banco de dados
	public void delete(T entidade) {
		getEntityManager().getTransaction().begin();
		if (entidade.getId() != null) {
			getEntityManager().remove(getEntityManager().getReference(entidade.getClass(), entidade.getId()));
		}
		getEntityManager().getTransaction().commit();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T findByExemple(T entidade) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
		Root root = criteriaQuery.from(entidade.getClass());

		List<Predicate> listaPredicate = new ArrayList<Predicate>();
		
		adicionarWhere(entidade, criteriaBuilder, criteriaQuery, root, listaPredicate);
		criteriaQuery.select(root);
		criteriaQuery.where(listaPredicate.toArray(new Predicate[listaPredicate.size()]));
		Query query = getEntityManager().createQuery(criteriaQuery);
		query.setMaxResults(1);

		try {
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAllByExample(T entidade) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
		Root root = criteriaQuery.from(entidade.getClass());

		List<Predicate> listaPredicate = new ArrayList<Predicate>();
		
		adicionarWhere(entidade, criteriaBuilder, criteriaQuery, root, listaPredicate);

		criteriaQuery.select(root);
		criteriaQuery.where(listaPredicate.toArray(new Predicate[listaPredicate.size()]));
		Query query = getEntityManager().createQuery(criteriaQuery);

		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	protected final Class<T> getPersistenceClass() {
		if (persisteceClass == null) {
			this.persisteceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return persisteceClass;
	}


	/**
	 * Metodo utilizado para adicionar a clausula WHERE no select.
	 * 
	 * @param entidade
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param root
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adicionarWhere(T entidade, CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, Root root, List<Predicate> listaPredicate) {
		try {
			for (Field field : entidade.getClass().getDeclaredFields()) {
				if (isCampoTabelado(field) && (entidade.isSomenteId() && isCampoId(field) || !entidade.isSomenteId())) {
					field.setAccessible(true);
					Object campo = field.get(entidade);
					if (campo != null && !campo.toString().trim().isEmpty()) {
						if (campo.getClass().equals(String.class)) {
							listaPredicate.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(field.getName())), "%" + field.get(entidade).toString().toLowerCase() + "%"));
						} else if (field.get(entidade).getClass().getSuperclass().equals(Entidade.class)) {
							Field id = field.get(entidade).getClass().getDeclaredField("id");
							id.setAccessible(true);
							if(id.get(field.get(entidade)) != null ){
								listaPredicate.add(criteriaBuilder.equal(root.get(field.getName()), field.get(entidade)));
							}
							id.setAccessible(false);
						} else {
							listaPredicate.add(criteriaBuilder.equal(root.get(field.getName()), field.get(entidade)));
						}
					}
					field.setAccessible(false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que ignora os campos que possuem anotacao Transiente ou ï¿½ um serialVersionUID
	 */
	private boolean isCampoTabelado(Field campo) {
		if (campo.getAnnotation(Transient.class) != null || campo.getName().equals("serialVersionUID")) {
			return false;
		}
		return true;
	}

	private boolean isCampoId(Field field) {
		for (Annotation annotation : field.getDeclaredAnnotations()) {
			if (annotation.annotationType().equals(Id.class)) {
				return true;
			}
		}
		return false;
	}

}