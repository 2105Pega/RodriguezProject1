package me.charlesrod.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
//@Embeddable
@Entity
@Table(name="application")
public class Application implements Serializable{
    @Override
	public String toString() {
		return "Application [id=" + id + ", initialAmount=" + initialAmount + ", status=" + status + "]";
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * p pending
     * a approved
     * d denied
     */
	@Id
	@GeneratedValue
    @Getter 
    @Setter
    private int id;
    @Getter 
    @Setter
    private double initialAmount = 0;
    @JoinColumn(name="acc_id",columnDefinition="integer",nullable=true)
    @Getter
    @Setter
    private Account acc;
    @Getter 
    @Setter
    private char status = 'p';
    @Getter 
    @Setter
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="end_user_application", joinColumns=@JoinColumn(name="app_id"),inverseJoinColumns=@JoinColumn(name="end_user_id"))
    private Set<User> customers = new HashSet<User>();
    public Application(int id, double amount, char status, User user) {
    	this.id = id;
    	this.initialAmount = amount;
    	this.status = status;
    	this.customers.add(user);
    }
    public Application(int id, double amount, char status, Set<User> customers) {
    	this.id = id;
    	this.initialAmount = amount;
    	this.status = status;
    	this.customers = customers;
    }
    public Application( double amount, char status, User user) {
    	this.initialAmount = amount;
    	this.status = status;
    	this.customers.add(user);
    }
    public Application(double amount, char status, Set<User> customers) {
    	this.initialAmount = amount;
    	this.status = status;
    	this.customers = customers;
    }
    public Application(double amount,char status){
        this.initialAmount = amount;
        this.status = status;
    }
    public Application(char status) {
    	this.status = status;
    }
    public Application() {
    	
    }
    public void removeAccount() {
    	this.acc = null;
    }
    public void addUser(User user) {
    	customers.add(user);
    	user.getApplications().add(this);
    }
    public void removeUser(User user) {
    	customers.remove(user);
    	user.getAccounts().remove(this);
    }
}