/*
 * package me.charlesrod.Bank;
 * 
 * import javax.servlet.annotation.WebListener;
 * 
 * import org.hibernate.cfg.Configuration;
 * 
 * 
 * //@WebListener public class App {
 * 
 * public static void main(String[] args) { // TODO Auto-generated method stub
 * // Configuration con = new Configuration().configure();
 * 
 * }
 * 
 * }
 */
package me.charlesrod.Bank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
//import javax.inject.Singleton;
import javax.servlet.ServletContextListener;

import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
//
import org.glassfish.hk2.api.Immediate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;



import org.hibernate.service.ServiceRegistry;

import me.charlesrod.DataAccess.AccountAccess;
import me.charlesrod.DataAccess.ApplicationAccess;
import me.charlesrod.DataAccess.RolesAccess;
import me.charlesrod.DataAccess.UserAccess;
import me.charlesrod.Models.Account;
import me.charlesrod.Models.Application;
import me.charlesrod.Models.Roles;
import me.charlesrod.Models.Transaction;
import me.charlesrod.Models.User;



public class App implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Configuration conn = new Configuration().configure().addAnnotatedClass(Account.class)
        													.addAnnotatedClass(Application.class)
        													.addAnnotatedClass(Roles.class)
        													.addAnnotatedClass(Transaction.class)
        													.addAnnotatedClass(User.class);
        ServiceRegistry sg = new StandardServiceRegistryBuilder().applySettings(conn.getProperties()).build();
        SessionFactory sf = conn.buildSessionFactory(sg);
    	EntityManagerFactory eff = Persistence.createEntityManagerFactory("mainhub");
    	EntityManager ef = eff.createEntityManager();
    	RolesAccess ra = new RolesAccess(ef);
    	Roles customer = new Roles("Customer");
    	Roles employee = new Roles("Employee");
    	ra.createRole(customer);
    	ra.createRole(employee);
    	UserAccess ua = new UserAccess(ef);
    	User gnoe = new User("gnoejuan","apple",customer);
    	User bob = new User("bob","password",customer);
    	User amy = new User("amy","qawesdef",employee);
    	User dude = new User("dude","pirate",customer);
    	User qbert = new User("qbert","test",customer);
    	User bank = new User("bank","bank", employee);
    	List<User> users = new ArrayList<User>(Arrays.asList(gnoe,bob,amy,qbert,bank));
    	for (User user : users) {
			ua.createUser(user);
		}
    	AccountAccess acca = new AccountAccess(ef);
    	Account bankacc = new Account('o',50000000,bank);
    	acca.createAccount(bankacc);
    	ApplicationAccess appa = new ApplicationAccess(ef);
    	Application app1 = new Application(50,'a',gnoe);
    	Set<User> app2users = new HashSet<User>();
    	app2users.add(qbert);
    	app2users.add(gnoe);
    	Application app2 = new Application(100000,'p',app2users);
    	Application app3 = new Application(0,'a',dude);
    	appa.createApplication(app1);
    	appa.createApplication(app2);
    	appa.createApplication(app3);
    }
}