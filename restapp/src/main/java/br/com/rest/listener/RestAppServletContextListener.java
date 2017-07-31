package br.com.rest.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.com.rest.dao.GenericDAO;

@WebListener
public class RestAppServletContextListener implements ServletContextListener {

	/**
	 * Inicializar a entityMangener do DAO no momento de start da aplicação.
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		GenericDAO.inicializarEntityManager();
	}
	
}
