package com.banking.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
	private InputHandler inputHandler;
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
	public Customer findCustomerById(int id) throws CustomerNotFoundException {
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
			int isSalaryAccount = inputHandler.readInt(
					"What type of savings account do you you want to open?\n1. Salary Account\n2. Non-Salary Account");
			if (isSalaryAccount == 1) {
				balance = 0;
				bankAccount = new SavingsAccount(accountNumber, bsbCode, bankName, balance, openingDate, true);
			} else { // assume user either picks 1 or 2, validation not done yet
				balance = 100;
				bankAccount = new SavingsAccount(accountNumber, bsbCode, bankName, balance, openingDate, false);
			}
		} else {
			double depositAmount = 0.0;
			do {
				depositAmount = inputHandler
						.readDouble("Enter the deposit amount for your fixed deposit account (minimum 1000): ");
				if (depositAmount < 1000) {
					System.out.println("Deposit amount is less than the minimum requirement (1000). Please try again.");
				}
			} while (depositAmount < 1000);

			int tenure = 0;
			do {
				tenure = inputHandler.readInt(
						"Enter the tenure in years for your fixed deposit account (between 1 to 7 inclusive): ");
				if (tenure < 1 || tenure > 7) {
					System.out.println("Invalid tenure. The tenure must be between 1 and 7 years. Please try again.");
				}
			} while (tenure < 1 || tenure > 7);

			bankAccount = new FixedDepositAccount(accountNumber, bsbCode, bankName, depositAmount, openingDate, depositAmount, tenure);
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
			persistenceDAO = new FileStorageDAO();
			customers = persistenceDAO.retrieveAllCustomers();
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

	public List<Customer> sortCustomersByName() {
		List<Customer> sortedCustomers = bankingDAO.getAllCustomers();
		sortedCustomers.sort(Comparator.comparing(Customer::getName));
		return sortedCustomers;
	}

	public List<Customer> sortCustomersById() {
		List<Customer> sortedCustomers = bankingDAO.getAllCustomers();
		sortedCustomers.sort(Comparator.comparingInt(Customer::getId));
		return sortedCustomers;
	}

	public List<Customer> sortCustomersByBalance() {
		List<Customer> sortedCustomers = bankingDAO.getAllCustomers();
		sortedCustomers.sort(Comparator.comparing(customer -> customer.getBankAccount().getBalance()));
		return sortedCustomers;
	}

	@Override
	public Customer findCustomerByName(String name) throws CustomerNotFoundException {
		persistenceDAO = new DatabaseStorageDAO();
		return persistenceDAO.findCustomerByName(name);
	}

}
