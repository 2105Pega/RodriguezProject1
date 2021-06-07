package me.charlesrod.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="end_user")
public class User implements Serializable{
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", role=" + role + "]";
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
    @Getter
    @Setter 
    private int id;
    @Getter 
    @Setter 
    private String username;
    @Getter 
    @Setter 
    private String password;
    @Getter 
    @Setter 
    private String session;
	@Getter
	@Setter
	@ManyToMany(mappedBy="customers")
    private Set<Account> accounts = new HashSet<Account>();
	@Getter
	@Setter
	@ManyToMany(mappedBy="customers")
	private Set<Application> applications = new HashSet<Application>();
	@Getter
	@Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name="user_role",joinColumns ={
    		@JoinColumn(name="user_id",referencedColumnName = "id")
    		},
    		inverseJoinColumns = {
    		@JoinColumn(name="role_id", referencedColumnName="id")		
    		})
    private Roles role;
    public User(){
    	
    }
    
    public User(int id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public User(String username, String password, Roles role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(username,user.username) && Objects.equals(password, user.password);
	}
	public void addAccount(Account acc) {
		accounts.add(acc);
	}
	public void removeAccount(Account acc) {
		accounts.remove(acc);
	}
	public void addApplication(Application app) {
		applications.add(app);
	}
	public void removeApplication(Application app) {
		applications.remove(app);
	}
}
