package me.charlesrod.DataAccess;

import java.util.Optional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jvnet.hk2.annotations.Service;

import me.charlesrod.Models.Account;
import me.charlesrod.Models.Transaction;


public class TransactionAccess {

	private EntityManager ef;
	private AccountAccess acca;
	public TransactionAccess(EntityManager efArg) {
		ef.flush();
		ef.clear();
		acca = new AccountAccess(efArg);
		this.ef = efArg;
	}
	public Optional<Transaction> findById(int id){
		ef.flush();
		ef.clear();
		Transaction trans = ef.find(Transaction.class, id);
		return trans != null ? Optional.of(trans) : Optional.empty();
	}
	public void addTransfer(Account sourceAccount,Account destinationAccount,double amount) {
		ef.flush();
		ef.clear();
		sourceAccount.withdrawl(amount);
		destinationAccount.deposit(amount);
		Transaction trans = new Transaction(sourceAccount,destinationAccount,amount);
		ef.getTransaction().begin();
		ef.persist(trans);
		ef.merge(sourceAccount);
		ef.merge(destinationAccount);
		ef.getTransaction().commit();
		
	}
}
