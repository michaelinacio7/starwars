package br.com.rest.ws;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.rest.annotation.JWTTokenNeeded;
import br.com.rest.dao.UserDAO;
import br.com.rest.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * WebService para realização do login e do cadastro	
 * 
 * @author michael.inacio
 *
 */
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginWs {

	private UserDAO userDAO;
	private User findUsers;
	
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@FormParam("login") String login, @FormParam("passwd") String passwd) {
		User user = new User();
		UserDAO userDAO = new UserDAO();
		User findUser = new User();
		user.setUser(login);
		user.setPassword(passwd);
		
		findUser = userDAO.findByExemple(user);
		try {
			
			if (findUser != null) {
				String token = issueToken(login);
				return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer" + token).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Path("estalogado")
	@JWTTokenNeeded
	public void estaLogado() {
	}

	private String issueToken(String login) {
		try {
			String jwtToken = Jwts.builder().setSubject(login).setIssuer("teste").setIssuedAt(new Date())
					.setExpiration(
							Date.from(LocalDateTime.now().plusMinutes(15l).atZone(ZoneId.systemDefault()).toInstant()))
					.signWith(SignatureAlgorithm.HS512, "secret").compact();
			return jwtToken;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@POST
	@Path("cadastro")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response cadastro(@FormParam("name") String name, @FormParam("login") String login, @FormParam("passwd") String passwd) {

		User user = new User();
		UserDAO userDAO = new UserDAO();
		User findUser = new User();
		user.setUser(login);
		user.setName(name);
		user.setPassword(passwd);
		
		findUser = userDAO.findByExemple(user);
		
		try {
			if (findUser == null) {
				userDAO.insert(user);
				String token = issueToken(login);
				return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer" + token).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
	}
	
	@POST
	@Path("findUser")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Long getUser(@FormParam("login") String login) {

		User user = new User();
		UserDAO userDAO = new UserDAO();
		findUsers = new User();
		user.setUser(login);
		findUsers = userDAO.findByExemple(user);
		return findUsers.getId();
	}

}
