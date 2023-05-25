package com.banking.service;

import java.util.List;

import com.banking.dto.Customer;
import com.banking.exceptions.CustomerNotFoundException;

public interface BankingService {
	void createCustomer(Customer customer);
	Customer findCustomerById(int id) throws CustomerNotFoundException;
	Customer findCustomerByName(String name) throws CustomerNotFoundException;
	void updateCustomer(int id, Customer updatedCustomer) throws CustomerNotFoundException;
	List<Customer> getAllCustomers(int choice);
}
