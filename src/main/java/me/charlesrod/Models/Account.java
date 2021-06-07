package me.charlesrod.Models;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
@Entity
//@Embeddable
@Table(name="account")
public class Account implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logr = LogManager.getLogger(Account.class);
	/**
     * o open
     * c closed
     * p pending
     */
	@Id
	@GeneratedValue
	@Getter
	@Setter
    private int id;
    @Getter
    @Setter 
    private char status ='p';
    @Getter 
    @Setter 
    private double balance = 0;
	@Getter
	@Setter
	@JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="app_id",nullable=true,columnDefinition="integer")
    private Application app;
    @Setter
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="end_user_account", joinColumns=@JoinColumn(name="acc_id"),inverseJoinColumns=@JoinColumn(name="end_user_id"))
    private Set<User> customers = new HashSet<>();
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

    public Account() {
    	
    }
    public Account(int id, Application app) {
    	this.id = id;
    	this.balance = app.getInitialAmount();
    	this.customers = new HashSet(app.getCustomers());
    	this.status = statusConverter(app.getStatus());
    }
    public Account(Application app) {
    	this.balance = app.getInitialAmount();
    	this.customers = new HashSet(app.getCustomers());
    	this.status = statusConverter(app.getStatus());
    	this.app = app;
    }
    public Account(int id, char status, double balance, Application app) {
    	this.id = id;
    	this.status = status;
    	this.balance = balance;
    	this.app = app;
    	this.customers = new HashSet(app.getCustomers());
    }
    public Account(char status, double balance, Application app) {
    	this.status = status;
    	this.balance = balance;
    	this.app = app;
    	this.customers = new HashSet(app.getCustomers());
    }
    public Account(int id, char status, double balance, User user) {
    	this.id = id;
    	this.status = status;
    	this.balance = balance;
    	this.customers.add(user);
    }
    public Account(char status, double balance, User user) {
    	this.status = status;
    	this.balance = balance;
    	this.customers.add(user);
    }
    public Account(int id, char status, double balance) {
    	this.id = id;
    	this.status = status;
    	this.balance = balance;
    }
//    public Account(char status, double balance) {
//    	this.status = status;
//    	this.balance = balance;
//    }
    public Account(double balance){
        this.balance = balance;
    }
//    public Account(char status){
//        this.status = status;
//    }
    public Set<User> getCustomers(){
    	Set<User> copy = new HashSet<>();
    	copy.addAll(this.customers);
    	return copy;
    }
    public void addUser(User user) {
    	customers.add(user);
    	user.getAccounts().add(this);
    }
    public void removeUser(User user) {
    	customers.remove(user);
    	user.getAccounts().remove(this);
    }
    public void deposit(double amount) {
    	this.balance += amount;
    }
    public void withdrawl(double amount) {
    	if (this.balance < amount) {
    		logr.error("Attempted to overdraw!");
    		return;
    	}
    	this.balance -= amount;
    }
    @Override
    public int hashCode() {
    	return Objects.hash(this.id);
    }
    @Override
    public boolean equals(Object o) {
    	if (this == o) {
    		return true;
    	}
    	if ( o == null || getClass() != o.getClass()) {
    		return false;
    	}
    	Account acc = (Account) o;
    	return Objects.equals(id,acc.id);
    }
	@Override
	public String toString() {
		return "Account [id=" + id + ", status=" + status + ", balance=" + balance + ", app=" + app + "]";
	}

}