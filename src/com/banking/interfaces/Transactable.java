package com.banking.interfaces;

import com.banking.exceptions.InsufficientFundsException;

public interface Transactable {
	void withdraw(double amount) throws InsufficientFundsException;
	void deposit(double amount);
}
