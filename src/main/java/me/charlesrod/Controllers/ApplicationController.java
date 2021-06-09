package me.charlesrod.Controllers;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me.charlesrod.DataAccess.AccountAccess;
import me.charlesrod.DataAccess.ApplicationAccess;
import me.charlesrod.DataAccess.UserAccess;
import me.charlesrod.Models.Account;
import me.charlesrod.Models.Application;
import me.charlesrod.Models.User;

@Path("/application")
public class ApplicationController {
	private static final Logger logr = LogManager.getLogger(ApplicationController.class);
	EntityManagerFactory eff = Persistence.createEntityManagerFactory("mainhub");
	EntityManager ef = eff.createEntityManager();
	Jws<Claims> jws;
	ObjectMapper mapper = new ObjectMapper();
	Hibernate5Module hbm = new Hibernate5Module();
	ObjectNode response = mapper.createObjectNode();
	@Context
	ServletContext context;
	long acceptableTimeScew = 3 * 60;

	public boolean authorized(String auth, int id) {
		String signer = context.getInitParameter("me.charlesrod.secret");
		SecretKey key = Keys.hmacShaKeyFor(signer.getBytes(StandardCharsets.UTF_8));
		UserAccess ua = new UserAccess(ef);
		User u;
		try {
			jws = Jwts.parserBuilder().setAllowedClockSkewSeconds(acceptableTimeScew).setSigningKey(key).build()
					.parseClaimsJws(auth);
			int claimedID = jws.getBody().get("user_id", Integer.class);
			Optional<User> testu = ua.findById(claimedID);
			if (testu.isEmpty()) {
				System.out.println("empty");
				return false;
			}
			u = testu.get();
			String role = jws.getBody().get("role", String.class);
			if (role.equalsIgnoreCase("Customer")) {
				AccountAccess acca = new AccountAccess(ef);
				// Account acc = acca.findById(id).get();
				Set<User> authedUsers = acca.findById(id).get().getCustomers();
				System.out.println(authedUsers);
				return authedUsers.stream().filter(w -> w.getId() == u.getId()).findAny().isPresent();
			}
			if (role.equalsIgnoreCase("employee")) {
				return true;
			}
		} finally {

		}
		return false;
	}

	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{id}")
	public Response getApplication(@PathParam("id") int id, @Context HttpServletRequest req,
			@HeaderParam("Bearer") String auth) {
		hbm.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
		mapper.registerModule(hbm);
		if (authorized(auth, id)) {
			ApplicationAccess appa = new ApplicationAccess(ef);
			Application a = appa.getApplicationById(id).get();
			try {
				String response = mapper.writeValueAsString(a);
				return Response.ok().entity(response).build();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		response.put("status", 401);
		response.put("message", "Not Authorized");
		return Response.status(Response.Status.OK).entity(response).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/approve/{id}")
	public Response approveApplication(@PathParam("id") int id, @HeaderParam("Bearer") String auth) {
		if (authorized(auth, id)) {
			ApplicationAccess appa = new ApplicationAccess(ef);
			// Application a = appa.getApplicationById(id).get();
			appa.updateApplication(id, 'a');
			return Response.ok().build();
		}
		response.put("status", 401);
		response.put("message", "Not Authorized");
		return Response.status(Response.Status.OK).entity(response).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/deny/{id}")
	public Response denyApplication(@PathParam("id") int id, @HeaderParam("Bearer") String auth) {
		if (authorized(auth, id)) {
			ApplicationAccess appa = new ApplicationAccess(ef);
			// Application a = appa.getApplicationById(id).get();
			appa.updateApplication(id, 'd');
			return Response.ok().build();
		}
		response.put("status", 401);
		response.put("message", "Not Authorized");
		return Response.status(Response.Status.OK).entity(response).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/new/{id}")
	public Response newApplication(@PathParam("id") int id, @HeaderParam("Bearer") String auth) {
		if (authorized(auth, id)) {
			String signer = context.getInitParameter("me.charlesrod.secret");
			SecretKey key = Keys.hmacShaKeyFor(signer.getBytes(StandardCharsets.UTF_8));
			UserAccess ua = new UserAccess(ef);
			User u;
			try {
				jws = Jwts.parserBuilder().setAllowedClockSkewSeconds(acceptableTimeScew).setSigningKey(key).build()
						.parseClaimsJws(auth);
				int claimedID = jws.getBody().get("user_id", Integer.class);
				Optional<User> testu = ua.findById(claimedID);
				if (testu.isEmpty()) {
					System.out.println("empty");
					return Response.noContent().build();
				}
				u = testu.get();
				String role = jws.getBody().get("role", String.class);
				if (role.equalsIgnoreCase("Customer")) {
					ApplicationAccess appa = new ApplicationAccess(ef);
					Application app = new Application(0, 'p', u);
					appa.createApplication(app);
					return Response.ok().build();
				}
				if (role.equalsIgnoreCase("employee")) {
					response.put("status", 401);
					response.put("message", "Not Authorized");
					return Response.status(Response.Status.OK).entity(response).build();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		response.put("status", 401);
		response.put("message", "Not Authorized");
		return Response.status(Response.Status.OK).entity(response).build();
	}
}
