package com.banking.dao;

import java.util.List;

import com.banking.dto.Customer;

public interface PersistenceDAO {
	void saveAllCustomers(List<Customer> customers);
    List<Customer> retrieveAllCustomers();
}
