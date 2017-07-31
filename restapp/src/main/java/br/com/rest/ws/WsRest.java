package br.com.rest.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.rest.annotation.JWTTokenNeeded;
import br.com.rest.dao.PeopleDAO;
import br.com.rest.dao.StarshipDAO;
import br.com.rest.domain.People;
import br.com.rest.domain.Starship;
import br.com.rest.dto.EspaconaveDTO;
import br.com.rest.dto.TripulanteDTO;

/**
 * WebService para realização do crud
 * 
 * @author michael.inacio
 *
 */
@Path("rest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WsRest {

	private PeopleDAO peopleDAO;
	private StarshipDAO starshipDAO;
	
	public WsRest() {
		peopleDAO = new PeopleDAO();
		starshipDAO = new StarshipDAO();
	}
	
	@POST
	@Path("cadastrarFrota")
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded
	public void cadastrarTripulantes(EspaconaveDTO espacoNaveDto) {

		Starship starship = new Starship();
		People pessoaFiltro = new People();

		starship = espacoNaveDto.getId() != null ? starshipDAO.findById(espacoNaveDto.getId()) : null;

		if (starship != null) {
			pessoaFiltro.setStarship(starship);
			List<People> listaPeople = peopleDAO.findAllByExample(pessoaFiltro);
			for (People item : listaPeople) {
				peopleDAO.delete(item);
			}
		} else {
			starship = new Starship();
		}

		starship.setIdStarship(espacoNaveDto.getIdEspaconave());
		starship.setName(espacoNaveDto.getNome());
		starship.setIdUser(espacoNaveDto.getIdUser());
		starship = starshipDAO.insert(starship);

		for (int i = 0; i < espacoNaveDto.getTripulantes().size(); i++) {
			People pessoa = new People();

			pessoa.setIdPeople(espacoNaveDto.getTripulantes().get(i).getId());
			pessoa.setName(espacoNaveDto.getTripulantes().get(i).getName());
			pessoa.setIdUser(espacoNaveDto.getIdUser());
			pessoa.setStarship(starship);
			peopleDAO.insert(pessoa);
		}
	}

	@POST
	@Path("listarEspaconaves")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Starship> listaEspaconaves(Integer userId) {

		Starship starship = new Starship();
		starship.setIdUser(userId);

		return starshipDAO.findAllByExample(starship);
	}
	
	@POST
	@Path("editarFrota")
	@Produces(MediaType.APPLICATION_JSON)
	public EspaconaveDTO editarFrota(Integer id) {
		Starship starship = new Starship();
		starship.setId(id);
		starship = starshipDAO.findById(id);
		People people = new People();
		people.setStarship(starship);
		List<People> listaPeople = peopleDAO.findAllByExample(people); 
		EspaconaveDTO espacoNave = new EspaconaveDTO();
		espacoNave.setId(starship.getId());
		espacoNave.setIdEspaconave(starship.getIdStarship());
		espacoNave.setIdUser(starship.getIdUser());
		espacoNave.setNome(starship.getName());
		espacoNave.setTripulantes(new ArrayList<>());
		for (People item : listaPeople) {
			TripulanteDTO tripulante = new TripulanteDTO();
			tripulante.setId(item.getIdPeople());
			tripulante.setName(item.getName());
			espacoNave.getTripulantes().add(tripulante);
		}
		return espacoNave;
	}

}
