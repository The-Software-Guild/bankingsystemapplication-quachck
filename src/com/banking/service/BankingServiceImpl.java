package com.banking.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.banking.dao.BankingDAO;
import com.banking.dao.BankingDAOImpl;
import com.banking.dao.DatabaseStorageDAO;
import com.banking.dao.FileStorageDAO;
import com.banking.dao.PersistenceDAO;
import com.banking.dto.BankAccount;
import com.banking.dto.Customer;
import com.banking.dto.FixedDepositAccount;
import com.banking.dto.SavingsAccount;
import com.banking.exceptions.CustomerNotFoundException;
import com.banking.exceptions.InsufficientFundsException;
import com.banking.interfaces.Transactable;
import com.banking.io.InputHandler;

public class BankingServiceImpl implements BankingService {
	private final InputHandler inputHandler;
	private BankingDAO bankingDAO;
	private PersistenceDAO persistenceDAO;
	
	public BankingServiceImpl() {
        bankingDAO = new BankingDAOImpl();
        this.inputHandler = new InputHandler();
    }

	@Override
	public void createCustomer(Customer customer) {
		bankingDAO.createCustomer(customer);
	}
	
	@Override
	public Customer findCustomerById(int id) throws CustomerNotFoundException{
		return bankingDAO.findCustomerById(id);
	}

	@Override
	public void updateCustomer(int id, Customer updatedCustomer) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		
	}

	public void assignBankAccountToCustomer(int id, long accountNumber, long bsbCode, String bankName,
			LocalDate openingDate, int bankAccountType) {
		Customer customer = null;
		
		try {
			customer = bankingDAO.findCustomerById(id);
		} catch (CustomerNotFoundException e) {
			System.out.println(e.getMessage());
		}

	    BankAccount bankAccount;
	    double balance;

	    if (bankAccountType == 1) {
	    	int isSalaryAccount = inputHandler.readInt("What type of savings account do you you want to open?\n1. Salary Account\n2. Non-Salary Account"); // 1 = salary, 2 = non-salary
        	if (isSalaryAccount == 1) {
        		balance = 0;
        		bankAccount = new SavingsAccount(accountNumber, bsbCode, bankName, balance, openingDate, true);
        	} else { // assume user either picks 1 or 2, validation not done yet
        		balance = 100;
        		bankAccount = new SavingsAccount(accountNumber, bsbCode, bankName, balance, openingDate, false);
        	}
	    } else {
	        // Logic for creating FixedDepositAccount
	        bankAccount = new FixedDepositAccount(accountNumber, bsbCode, bankName, 100, openingDate, 1000, 1);
	    }

	    // Assign bank account to the customer
	    customer.setBankAccount(bankAccount);

	    // Update customer in storage
	    try {
			bankingDAO.updateCustomer(id, customer);
		} catch (CustomerNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void persistCustomerData(int choice) throws Exception {
        List<Customer> allCustomers = bankingDAO.getAllCustomers();
        if (choice == 1) {
            persistenceDAO = new FileStorageDAO();
        } else if (choice == 2) {
            persistenceDAO = new DatabaseStorageDAO();
        } else {
            throw new Exception("Invalid persistence option");
        }
        persistenceDAO.saveAllCustomers(allCustomers);
    }

	@Override
	public List<Customer> getAllCustomers(int choice) {
		List<Customer> customers = new ArrayList<>();
		if (choice == 1) {
			
		} else if (choice == 2) {
			persistenceDAO = new DatabaseStorageDAO();
			customers = persistenceDAO.retrieveAllCustomers();
		}
		return customers;
	}

	public void withdraw(Transactable transactable, double withdrawAmount) {
		try {
			transactable.withdraw(withdrawAmount);
			System.out.println("Withdrawal successful");
		} catch (InsufficientFundsException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deposit(Transactable transactable, double depositAmount) {
		transactable.deposit(depositAmount);
		System.out.println("Deposit successful.");
	}

	public double getBalance(BankAccount bankAccount) {
		return bankAccount.getBalance();
	}

	public double calculateInterest(BankAccount bankAccount) {
		return bankAccount.calculateInterest();
	}
	
}
