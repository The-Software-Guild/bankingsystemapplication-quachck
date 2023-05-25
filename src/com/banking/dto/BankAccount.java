package com.banking.dto;

import java.time.LocalDate;

public abstract class BankAccount {
    private long accountNumber;
    private long bsbCode;
    private String bankName;
    private double balance;
    private LocalDate openingDate;

    public BankAccount() {

    }

    public BankAccount(long accountNumber, long bsbCode, String bankName, double balance, LocalDate openingDate) {
        this.accountNumber = accountNumber;
        this.bsbCode = bsbCode;
        this.bankName = bankName;
        this.balance = balance;
        this.openingDate = openingDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }
    

    public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public long getBsbCode() {
		return bsbCode;
	}

	public void setBsbCode(long bsbCode) {
		this.bsbCode = bsbCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String toString() {
        return "Account Number: " + accountNumber +
                "\nBSB Code: " + bsbCode +
                "\nBank Name: " + bankName +
                "\nBalance: " + balance +
                "\nOpening Date: " + openingDate;
    }

    public abstract double calculateInterest();
}

