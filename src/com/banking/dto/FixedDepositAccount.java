package com.banking.dto;

import java.time.LocalDate;

public class FixedDepositAccount extends BankAccount {
    private double depositAmount;
    private int tenure;

    public FixedDepositAccount(long accountNumber, long bsbCode, String bankName, double balance, LocalDate openingDate,
            double depositAmount, int tenure) {
        super(accountNumber, bsbCode, bankName, balance, openingDate);
        this.depositAmount = depositAmount;
        this.tenure = tenure;
    }
      
    public double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public int getTenure() {
		return tenure;
	}

	public void setTenure(int tenure) {
		this.tenure = tenure;
	}

	@Override
    public double calculateInterest() {
        double interestRate = 0.08; // 8% per annum
        double interest = depositAmount * interestRate * tenure;
        return interest;
    }
    
    @Override
    public String toString() {
        return super.toString() + 
                "\nDeposit Amount: " + depositAmount +
                "\nTenure: " + tenure + " years";
    }
}

