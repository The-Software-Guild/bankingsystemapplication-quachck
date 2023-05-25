package com.banking.dto;

import java.time.LocalDate;

import com.banking.exceptions.InsufficientFundsException;
import com.banking.interfaces.Transactable;

public class SavingsAccount extends BankAccount implements Transactable {
    private boolean isSalaryAccount;
    private double minimumBalance;

    public SavingsAccount(long accountNumber, long bsbCode, String bankName, double balance, LocalDate openingDate,
            boolean isSalaryAccount) {
        super(accountNumber, bsbCode, bankName, balance, openingDate);
        this.isSalaryAccount = isSalaryAccount;
        this.minimumBalance = isSalaryAccount ? 0 : 100;
    }
    
    public SavingsAccount() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (isSalaryAccount) {
            if (getBalance() >= amount) {
                setBalance(getBalance() - amount);
            } else {
                throw new InsufficientFundsException("Insufficient funds");
            }
        } else {
            if (getBalance() - amount >= minimumBalance) {
                setBalance(getBalance() - amount);
            } else {
                throw new InsufficientFundsException("Insufficient balance for non-salary savings account. Minimum balance should be $100");
            }
        }
    }
    
    public void deposit(double amount) {
        setBalance(getBalance() + amount);
    }
    
    public boolean isSalaryAccount() {
        return isSalaryAccount;
    }

    public void setSalaryAccount(boolean isSalaryAccount) {
        this.isSalaryAccount = isSalaryAccount;
    }

    public double getMinimumBalance() {
		return minimumBalance;
	}

	public void setMinimumBalance(double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}

	@Override
    public double calculateInterest() {
        return getBalance() * 0.04;
    }

    @Override
    public String toString() {
        return super.toString() + 
                "\nSalary Account: " + isSalaryAccount +
                "\nMinimum Balance: " + minimumBalance;
    }
}


