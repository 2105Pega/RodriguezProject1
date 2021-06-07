package me.charlesrod.DataAccess;

import java.util.Optional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jvnet.hk2.annotations.Service;

import me.charlesrod.Models.Roles;


public class RolesAccess {

	private EntityManager ef;
	public RolesAccess(EntityManager efArg) {
		this.ef = efArg;
	}
	  public Optional<Roles> createRole(Roles role){
		  try {
			  ef.getTransaction().begin();
			  ef.persist(role);
			  ef.getTransaction().commit();
			  return Optional.of(role);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  return Optional.empty();
	  }
}
