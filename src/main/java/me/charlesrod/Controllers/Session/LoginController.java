package me.charlesrod.Controllers.Session;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jvnet.hk2.annotations.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

import me.charlesrod.DataAccess.UserAccess;
import me.charlesrod.Models.Credentials;
import me.charlesrod.Models.User;

import org.glassfish.jersey.servlet.WebServletConfig;

//@Service
@Path("/login")
public class LoginController extends Application{
	@Context ServletContext context;
	//@PersistenceContext
	EntityManagerFactory eff = Persistence.createEntityManagerFactory("mainhub");
	EntityManager ef = eff.createEntityManager();
	UserAccess ua = new UserAccess(ef);
	ObjectMapper mapper = new ObjectMapper();
	ObjectNode response = mapper.createObjectNode();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(Credentials cred) throws ServletException {
		try {
			String username = cred.getUsername();
			String password = cred.getPassword();
			User u = authenticate(username, password);
			String token = issueToken(username,u);
			return Response.ok(token).build();
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 400);
			return Response.status(Response.Status.OK).entity(response).build();
		}
	}

	private User authenticate(String username, String password) throws Exception{
		Optional<User> u = ua.findByUsername(username);
		if (u.isEmpty()) {
			 throw new Exception();
		}
		return u.get();
	}
	private String issueToken(String username, User u) {
		String signer = context.getInitParameter("me.charlesrod.secret");
		SecretKey key = Keys.hmacShaKeyFor(signer.getBytes(StandardCharsets.UTF_8));
		String jws = Jwts.builder()
				.setSubject(username)
				.claim("role",u.getRole().toString())
				.claim("user_id", u.getId())
				.signWith(key)
				.compact();
		return jws;
	}
}
