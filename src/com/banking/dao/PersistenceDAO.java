package com.banking.dao;

import java.util.List;

import com.banking.dto.Customer;
import com.banking.exceptions.CustomerNotFoundException;

public interface PersistenceDAO {
	void saveAllCustomers(List<Customer> customers);
	List<Customer> retrieveAllCustomers();
	Customer findCustomerByName(String name) throws CustomerNotFoundException;
}
