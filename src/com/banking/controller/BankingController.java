package com.banking.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.banking.dto.BankAccount;
import com.banking.dto.Customer;
import com.banking.dto.FixedDepositAccount;
import com.banking.dto.SavingsAccount;
import com.banking.exceptions.CustomerNotFoundException;
import com.banking.exceptions.InsufficientFundsException;
import com.banking.interfaces.Transactable;
import com.banking.io.InputHandler;
import com.banking.service.BankingServiceImpl;

public class BankingController {
	private final BankingServiceImpl bankingService;
	private final InputHandler inputHandler;
 

    public BankingController() {
        this.bankingService = new BankingServiceImpl();
		this.inputHandler = new InputHandler();
    }

    public void start() {
        while (true) {
            displayMenu();
            int choice = inputHandler.readInt("Enter your choice: ");

			switch (choice) {
			    case 1:
			        createCustomer();
			        break;
			    case 2:
			        assignBankAccountToCustomer();
			        break;
			    case 3:
			    	displayCustomerActions();
			        break;
			    case 4:
			        sortCustomerData();
			        break;
			    case 5:
			        persistCustomerData();
			        break;
			    case 6:
			        showAllCustomers();
			        break;
			    case 7:
			        searchCustomersByName();
			        break;
			    case 8:
			    	System.out.println("Exiting the Banking App... Thank you!");
			    	return;
			    default:
			    	System.out.println("Invalid choice. Please try again.");
			        break;
			}
	    }
	}
	
	private void displayMenu() {
	    System.out.println("Menu:");
		System.out.println("1. Create New Customer Data");
		System.out.println("2. Assign a Bank Account to a Customer");
		System.out.println("3. Displayer Customer Actions");
		System.out.println("4. Sort Customer Data");
		System.out.println("5. Persist Customer Data");
		System.out.println("6. Show All Customers");
		System.out.println("7. Search Customers by Name");
		System.out.println("8. Exit");
	}
	
	private void createCustomer() {
		printDivider();
		System.out.println("1. Creating new customer data...");
		
		// get customer attributes from console
        String name = inputHandler.readString("Enter Name:");
        int age = inputHandler.readInt("Enter Age:");
        int mobileNumber = inputHandler.readInt("Enter Mobile Number:");
        String passportNumber = inputHandler.readString("Enter Passport Number:");
        LocalDate dob = inputHandler.getDateFromUser("Enter Date of Birth (DD/MM/YYYY):");
        
        bankingService.createCustomer(new Customer(name, age, mobileNumber, passportNumber, dob));

		printDivider();
	}
	
	private void assignBankAccountToCustomer() {
		printDivider();
		System.out.println("Assigning bank account to customer...");
		
		int id;
	    Customer customer = null;
	    
	    while (true) {
	        id = inputHandler.readInt("Enter Customer ID: ");
	        try {
	            customer = bankingService.findCustomerById(id);
	            break;
	        } catch (CustomerNotFoundException e) {
	            System.out.println("Customer not found. Please try again.");
	        }
	    }
		
	    long accountNumber = inputHandler.readLong("Enter Account Number: ");
	    long bsbCode = inputHandler.readLong("Enter BSB Code: ");
	    String bankName = inputHandler.readString("Enter Bank Name: ");
	    LocalDate openingDate = LocalDate.now();
	    int bankAccountType = inputHandler.readInt("What account do you want to open?\n1. Savings Account\n2. Fixed Desposit Account");

	    try {
	        bankingService.assignBankAccountToCustomer(id, accountNumber, bsbCode, bankName, openingDate, bankAccountType);
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
		
		printDivider();
	}
	
	
	private void displayCustomerActions() {
	    printDivider();
	    System.out.println("Option 3: Display balance or interest earned of a Customer");

	    int id = inputHandler.readInt("Please enter customer ID:");

	    try {
	        Customer customer = bankingService.findCustomerById(id);
	        BankAccount bankAccount = customer.getBankAccount();

	        if (bankAccount instanceof SavingsAccount) {
	            Transactable transactable = (Transactable) bankAccount;

	            while (true) {
	                int choice = inputHandler.readInt("1. Withdraw\n2. Deposit\n3. Show Balance\n4. Show Interest Earned:");

	                switch (choice) {
	                    case 1:
	                        double withdrawAmount = inputHandler.readDouble("Enter the withdrawal amount: ");
	                        bankingService.withdraw(transactable, withdrawAmount);
	                        break;
	                    case 2:
	                        double depositAmount = inputHandler.readDouble("Enter the deposit amount: ");
	                        bankingService.deposit(transactable, depositAmount);
	                        break;
	                    case 3:
	                        System.out.println("Balance: " + bankingService.getBalance(bankAccount));
	                        break;
	                    case 4:
	                        System.out.println("Interest Earned: " + bankingService.calculateInterest(bankAccount));
	                        break;
	                    default:
	                        System.out.println("Invalid choice. Please try again.");
	                        continue;
	                }
	                break;
	            }
	        } else if (bankAccount instanceof FixedDepositAccount) {
	            while (true) {
	                int choice = inputHandler.readInt("1. Show Balance\n2. Show Interest Earned:");

	                switch (choice) {
	                    case 1:
	                        System.out.println("Balance: " + bankingService.getBalance(bankAccount));
	                        break;
	                    case 2:
	                        System.out.println("Interest Earned: " + bankingService.calculateInterest(bankAccount));
	                        break;
	                    default:
	                        System.out.println("Invalid choice. Please try again.");
	                        continue;
	                }
	                break;
	            }
	        } else {
	            System.out.println("Invalid account type.");
	        }
	    } catch (CustomerNotFoundException e) {
	        System.out.println("Error: " + e.getMessage());
	    }

	    printDivider();
	}

	
	private void sortCustomerData() {
        System.out.println("Option 4: Sort Customer Data");
        
        List<Customer> sortedCustomers = new ArrayList<>();
        int choice = inputHandler.readInt("Sort by:\n1. Customer Names\n2. Customer IDs\n3. Customer Bank Balance");

        switch (choice) {
        case 1:
            sortedCustomers = bankingService.sortCustomersByName();
            System.out.println("Customers sorted by names:");
            break;
        case 2:
            sortedCustomers = bankingService.sortCustomersById();
            System.out.println("Customers sorted by IDs:");
            break;
        case 3:
            sortedCustomers = bankingService.sortCustomersByBalance();
            System.out.println("Customers sorted by bank balance:");
            break;
        default:
            System.out.println("Invalid choice.");
            return;
	    }
	
	    for (Customer customer : sortedCustomers) {
	        System.out.println(customer.toString());
	    }   
	}
	
	private void persistCustomerData() {
	    System.out.println("Option 5: Persist Customer Data");

	    int choice = inputHandler.readInt("Choose method of persistence: \n1. Filesystem\n2. Database\n");

	    // Call service method
	    try {
	        bankingService.persistCustomerData(choice);
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	}

	
	private void showAllCustomers() {
		System.out.println("Showing all customers...");
		
		int choice = inputHandler.readInt("Choose storage type: \n1. Filesystem\n2. Database\n");
		System.out.println(bankingService.getAllCustomers(choice));
	}
	
	private void searchCustomersByName() {
	    // Placeholder method for searching customers by name
		System.out.println("Searching customers by name...");
	}
	
	private void printDivider() {
    	System.out.println("----------------------------");
    }
}
