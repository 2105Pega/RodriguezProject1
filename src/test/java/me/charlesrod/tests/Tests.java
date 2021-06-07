package me.charlesrod.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import me.charlesrod.Models.Account;

class Tests {

	@Test
	void onlySomeMoney() {
		Account acc = new Account(50.00);
		acc.withdrawl(80);
		assertEquals(50, acc.getBalance());
	}
	@Test
	void okHereyouGo() {
		Account acc = new Account(50.00);
		acc.withdrawl(20);
		assertEquals(30, acc.getBalance());
	}

}
