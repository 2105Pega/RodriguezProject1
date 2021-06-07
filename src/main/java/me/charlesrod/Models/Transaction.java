package me.charlesrod.Models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="transaction")
public class Transaction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Getter
	@Setter
	@ManyToOne
	private Account sourceAccount;
	@Getter
	@Setter
	@ManyToOne
	private Account destinationAccount;
	@Getter
	@Setter
	private double amount;
	public Transaction(Account source, Account dest, double amount){
		this.sourceAccount = source;
		this.destinationAccount = dest;
		this.amount = amount;
	}
}
