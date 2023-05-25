package com.banking.dao;

import java.util.List;

import com.banking.dto.Customer;
import com.banking.exceptions.CustomerNotFoundException;

public interface BankingDAO {
	void createCustomer(Customer customer);
	Customer findCustomerById(int id) throws CustomerNotFoundException;
	void updateCustomer(int id, Customer updatedCustomer) throws CustomerNotFoundException;
	List<Customer> getAllCustomers();
}
