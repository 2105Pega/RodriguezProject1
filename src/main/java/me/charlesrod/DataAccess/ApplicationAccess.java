package me.charlesrod.DataAccess;

import java.util.List;
import java.util.Optional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.jvnet.hk2.annotations.Service;

import me.charlesrod.Models.Account;
import me.charlesrod.Models.Application;
import me.charlesrod.Models.User;


public class ApplicationAccess {
	private EntityManager ef;
    private char statusConverter(char a){
        switch(Character.valueOf(a)){
            case 'p':
            return 'p';
            case 'a':
            return 'o';
            case 'd':
            return 'c';
            default:
            return 'p';
        }
    }
	public ApplicationAccess(EntityManager efArg) {
		this.ef = efArg;
	}
	public Optional<Application> getApplicationById(int id){
		//ef.flush();
		//ef.clear();
		Application app = ef.find(Application.class, id);
		return app != null ? Optional.of(app) : Optional.empty();
	}
	@SuppressWarnings("unchecked")
	public List<Application> getAll(){
		//ef.flush();
		//ef.clear();
		return ef.createQuery("from application").getResultList();
	}
	public Optional<Application> createApplication(Application app){
		//ef.flush();
		//ef.clear();
		try {
			ef.getTransaction().begin();
			ef.persist(app);
			Account acc = new Account(app);
			ef.persist(acc);
			ef.getTransaction().commit();
			return Optional.of(app);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	public void deleteById(Integer id) {
		//ef.flush();
		//ef.clear();
		Application app = ef.find(Application.class, id);
		if (app != null) {
			try {
				ef.getTransaction().begin();
				ef.remove(app);
				ef.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
