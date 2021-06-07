package me.charlesrod.DataAccess;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jvnet.hk2.annotations.Service;

import me.charlesrod.Models.Account;
import me.charlesrod.Models.Application;
import me.charlesrod.Models.User;

public class UserAccess {

	private EntityManager ef;
	public UserAccess(EntityManager efArg) {
		this.ef = efArg;
	}
	  public Optional<User> findById(int id){
		  User u = ef.find(User.class, id);
		  return u != null ? Optional.of(u) : Optional.empty();
	  }
	  public Optional<User> createUser(User user){
		  try {
			  ef.getTransaction().begin();
			  ef.persist(user);
			  ef.getTransaction().commit();
			  return Optional.of(user);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  return Optional.empty();
	  }
	  public void deleteById(int id) {
		  User user = ef.find(User.class, id);
		  if (user != null) {
			  try {
				  ef.getTransaction().begin();
				  ef.remove(user);
				  ef.getTransaction().commit();
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
		  }
	  }
	  public Set<Account> getAccounts(int id){
		  User user = ef.find(User.class, id);
		  return user.getAccounts();
	  }
	  public Set<Application> getApplications(int id){
		  User user = ef.find(User.class, id);
		  return user.getApplications();
	  }
	  public void updatePassword(String newPassword, int id) {
		  User user = ef.find(User.class, id);
		  ef.getTransaction().begin();
		  user.setPassword(newPassword);
		  ef.merge(user);
		  ef.getTransaction().commit();
	  }
	  public Optional<User> findByUsername(String username){
		  Query q = ef.createQuery("select u from User u where u.username like :usernameArg", User.class);
		  return q.setParameter("usernameArg", username)
				  .setMaxResults(1)
				  .getResultList()
				  .stream()
				  .findFirst();
	  }
	  public void updateSession(String session, int id) {
		  User user = ef.find(User.class, id);
		  try {
			  ef.getTransaction().begin();
			  user.setSession(session);
			  ef.getTransaction().commit();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	  }
	  @SuppressWarnings("unchecked")
	public List<User> getAll(){
		  return ef.createQuery("from User").getResultList();
	  }
	  public boolean checkSession(String session) {
		  User u;
		  Query q = ef.createQuery("select u from User u where u.session = :sessionArg");
		  q.setParameter("sessionArg", session);
		  u = (User) q.getSingleResult();
		  return (u != null) ? true : false;
	  }
}
