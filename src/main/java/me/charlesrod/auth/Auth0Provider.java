//package me.charlesrod.auth;
//
//import com.auth0.AuthenticationController;
//import com.auth0.jwk.JwkProvider;
//import com.auth0.jwk.JwkProviderBuilder;
//
//import java.io.UnsupportedEncodingException;
//
//import org.glassfish.jersey.servlet.WebServletConfig;
//
//public abstract class Auth0Provider {
//
//	public static AuthenticationController getInstance(WebServletConfig config) throws UnsupportedEncodingException {
//		String domain = config.getServletConfig().getServletContext().getInitParameter("com.auth0.domain");
//		String clientId = config.getServletConfig().getServletContext().getInitParameter("com.auth0.clientId");
//		String clientSecret = config.getServletConfig().getServletContext().getInitParameter("com.auth0.clientSecret");
//		
//		JwkProvider jwkProvider = new JwkProviderBuilder(domain).build();
//		return AuthenticationController.newBuilder(domain, clientId, clientSecret)
//				.withJwkProvider(jwkProvider).build();
//	}
//}
