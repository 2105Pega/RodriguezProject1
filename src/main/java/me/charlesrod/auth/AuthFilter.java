//package me.charlesrod.auth;
//
//import java.io.IOException;
//
//import javax.annotation.Priority;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.Provider;
//import javax.ws.rs.Priorities;
//import me.charlesrod.auth.Secured;
//
//import me.charlesrod.DataAccess.UserAccess;
//
//@Secured
//@Provider
//@Priority(Priorities.AUTHENTICATION)
//public class AuthFilter implements ContainerRequestFilter{
//	private static final String REALM = "bank";
//	private static final String AUTHENTICATION_SCHEME = "Bearer";
//	EntityManagerFactory eff = Persistence.createEntityManagerFactory("mainhub");
//	EntityManager ef = eff.createEntityManager();
//	UserAccess ua = new UserAccess(ef);
//	@Override
//	public void filter(ContainerRequestContext requestContext) throws IOException {
//		// TODO Auto-generated method stub
//        String authorizationHeader =
//                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
//        if (!isTokenBasedAuthentication(authorizationHeader)) {
//            abortWithUnauthorized(requestContext);
//            return;
//        }
//        String token = authorizationHeader
//                .substring(AUTHENTICATION_SCHEME.length()).trim();
//        try {
//
//            // Validate the token
//            validateToken(token);
//
//        } catch (Exception e) {
//            abortWithUnauthorized(requestContext);
//        }
//	}
//	private void validateToken(String token) throws Exception {
//		// TODO Auto-generated method stub
//		if (ua.checkSession(token)) {
//			return;
//		}
//		throw new Exception("bad token");
//	}
//	private void abortWithUnauthorized(ContainerRequestContext requestContext) {
//		// TODO Auto-generated method stub
//        requestContext.abortWith(
//                Response.status(Response.Status.UNAUTHORIZED)
//                        .header(HttpHeaders.WWW_AUTHENTICATE, 
//                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
//                        .build());
//	}
//	private boolean isTokenBasedAuthentication(String authorizationHeader) {
//		// TODO Auto-generated method stub
//        return authorizationHeader != null && authorizationHeader.toLowerCase()
//                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
//	}
//
//}
