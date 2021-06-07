package me.charlesrod.Controllers.Session;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import me.charlesrod.DataAccess.UserAccess;
import me.charlesrod.Models.Credentials;
import me.charlesrod.Models.User;

@Path("/register")
public class RegisterController extends Application {
	@Context ServletContext context;
	EntityManagerFactory eff = Persistence.createEntityManagerFactory("mainhub");
	EntityManager ef = eff.createEntityManager();
	UserAccess ua = new UserAccess(ef);
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response register(Credentials cred) throws ServletException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode response = mapper.createObjectNode();
		try {
			String username = cred.getUsername();
			String password = cred.getPassword();
			if (tryNew(username, password)) {
				return Response.ok().build();
			}
			response.put("status", 409);
			return Response.status(Response.Status.OK).entity(response).build();
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 400);
			return Response.status(Response.Status.OK).entity(response).build();
		}
	}
	private boolean tryNew(String username, String password) {
		Optional<User> u = ua.findByUsername(username);
		if (u.isEmpty()) {
			User user = new User(username,password);
			 Optional<User> test  = ua.createUser(user);
			 if (test.isPresent()) {
				 return true;
			 }
			 return false;
		}
		return false;
	}
}
