package me.charlesrod.Controllers.Home;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me.charlesrod.DataAccess.UserAccess;
import me.charlesrod.Models.Account;
import me.charlesrod.Models.User;


@Path("/home")
public class HomeController {
	@Context ServletContext context;
	EntityManagerFactory eff = Persistence.createEntityManagerFactory("mainhub");
	EntityManager ef = eff.createEntityManager();
	Jws<Claims> jws;
	ObjectMapper mapper = new ObjectMapper();
	ObjectNode response = mapper.createObjectNode();
	long acceptableTimeScew = 3 * 60;
	//String signer = context.getInitParameter("me.charlesrod.secret");
	//SecretKey key = Keys.hmacShaKeyFor(signer.getBytes(StandardCharsets.UTF_8));
	
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response doGet(@Context HttpServletRequest req,@HeaderParam("Bearer") String auth){
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
				response.put("status", 400);
				response.put("message","Hmmm, what happened to your account?");
				return Response.status(Response.Status.OK).entity(response).build();
			}
			u = testu.get();
			Set<Account> accounts = new HashSet<Account>();
			accounts = u.getAccounts();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		//if (ua.getAccounts(0))
		return null;
	}
}
