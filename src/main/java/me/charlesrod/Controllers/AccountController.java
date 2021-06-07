package me.charlesrod.Controllers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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
import me.charlesrod.Models.User;

@Path("/account")
public class AccountController {
	private static final Logger logr = LogManager.getLogger(AccountController.class);
	EntityManagerFactory eff = Persistence.createEntityManagerFactory("mainhub");
	EntityManager ef = eff.createEntityManager();
	Jws<Claims> jws;
	ObjectMapper mapper = new ObjectMapper();
	Hibernate5Module hbm = new Hibernate5Module();
	ObjectNode response = mapper.createObjectNode();
	@Context ServletContext context;
	long acceptableTimeScew = 3 * 60;
	public boolean authorized(String auth, int id) {
		String signer = context.getInitParameter("me.charlesrod.secret");
		SecretKey key = Keys.hmacShaKeyFor(signer.getBytes(StandardCharsets.UTF_8));
		UserAccess ua = new UserAccess(ef);
		User u;
		try {
			jws = Jwts.parserBuilder()
					.setAllowedClockSkewSeconds(acceptableTimeScew)
					.setSigningKey(key)
					.build()
					.parseClaimsJws(auth);
			int claimedID = jws.getBody().get("user_id",Integer.class);
			Optional<User> testu =ua.findById(claimedID);
			if (testu.isEmpty()) {
				System.out.println("empty");
				return false;
			}
			u = testu.get();
			String role = jws.getBody().get("role",String.class);
			if (role.equalsIgnoreCase("Customer")) {
				AccountAccess acca = new AccountAccess(ef);
				//Account acc = acca.findById(id).get();
				Set<User> authedUsers = acca.findById(id).get().getCustomers();	
				System.out.println(authedUsers);
				return authedUsers.stream().filter(w -> w.getId() == u.getId()).findAny().isPresent();
			}
			if (role.equalsIgnoreCase("employee")) {
				return true;
			}
		}finally {

		}
		return false;
	}
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{id}")
	public Response getAccount(@PathParam("id") int id,@Context HttpServletRequest req,@HeaderParam("Bearer") String auth) {
		hbm.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
		mapper.registerModule(hbm);
		if (authorized(auth,id)) {
			AccountAccess acca = new AccountAccess(ef);
			Account a = acca.findById(id).get();
			try {
				String response = mapper.writeValueAsString(a);
				return Response.ok().entity(response).build();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		response.put("status", 401);
		response.put("message","Not Authorized");
		return Response.status(Response.Status.OK).entity(response).build();
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{id}/deposit")
	public Response deposit(@PathParam("id") int id,@HeaderParam("Bearer") String auth,String body) {
		hbm.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
		mapper.registerModule(hbm);
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(body);
			String doubleArg = parent.path("amount").asText();
			System.out.println(parent);
			logr.debug(parent);
			double fromArg = Double.parseDouble(doubleArg);
			if (authorized(auth,id)) {
				System.out.println("Authorized");
				AccountAccess acca = new AccountAccess(ef);
				acca.deposit(id, fromArg);
				System.out.println("deposited?");
				return Response.ok().build();
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.put("status", 401);
		response.put("message","Not Authorized");
		return Response.status(Response.Status.OK).entity(response).build();
	}
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{id}/withdraw")
	public Response withdraw(@PathParam("id") int id,@HeaderParam("Bearer") String auth,String body) {
		hbm.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
		mapper.registerModule(hbm);
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(body);
			String doubleArg = parent.path("amount").asText();
			System.out.println(parent);
			logr.debug(parent);
			double fromArg = Double.parseDouble(doubleArg);
			if (authorized(auth,id)) {
				AccountAccess acca = new AccountAccess(ef);
				acca.withdraw(id, fromArg);
				return Response.ok().build();
			}
		}catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		response.put("status", 401);
		response.put("message","Not Authorized");
		return Response.status(Response.Status.OK).entity(response).build();
	}
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/close/{id}")
	public Response closeAccount(@PathParam("id") int id,@HeaderParam("Bearer") String auth) {
		if (authorized(auth,id)) {
			AccountAccess acca = new AccountAccess(ef);
			//Application a = appa.getApplicationById(id).get();
			acca.updateAccount(id, 'c');
			return Response.ok().build();
		}
		response.put("status", 401);
		response.put("message","Not Authorized");
		return Response.status(Response.Status.OK).entity(response).build();
	}
}
