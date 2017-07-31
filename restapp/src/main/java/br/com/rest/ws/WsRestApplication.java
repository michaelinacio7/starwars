package br.com.rest.ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.com.rest.filter.JWTTokenNeededFilter;

/**
 * Classe para carregar os serviços REST;
 * 
 * @author michael.inacio
 *
 */
@ApplicationPath("/ws")
public class WsRestApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public WsRestApplication() {
		singletons.add(new JWTTokenNeededFilter());
		singletons.add(new WsRest());
		singletons.add(new LoginWs());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
