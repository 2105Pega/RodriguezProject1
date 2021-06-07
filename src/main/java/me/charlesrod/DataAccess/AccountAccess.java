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


public class AccountAccess {
	//EntityManagerFactory eff;
	private EntityManager ef;
	public AccountAccess(EntityManager efArg) {
		this.ef = efArg;
	}
	public Optional<Account> findById(int id){
		Account acc = ef.find(Account.class, id);
		return acc != null ? Optional.of(acc) : Optional.empty();
	}
	@SuppressWarnings("unchecked")
	public List<Account> getAll(){
		return ef.createQuery("from account").getResultList();
	}

	public Optional<Account> createAccount(Account acc) {
		try {
			ef.getTransaction().begin();
			ef.persist(acc);
			ef.getTransaction().commit();
			return Optional.of(acc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	public void deleteById(int id) {
		Account acc = ef.find(Account.class, id);
		if (acc != null) {
			try {
				ef.getTransaction().begin();
				acc.getCustomers().forEach(c -> {
					c.getAccounts().remove(acc);
				});
				ef.remove(acc);
				ef.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
