package com.banking.dao;

import java.util.List;

import com.banking.dto.Customer;
import com.banking.exceptions.CustomerNotFoundException;

public class FileStorageDAO implements PersistenceDAO {

	@Override
	public void saveAllCustomers(List<Customer> customers) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Customer> retrieveAllCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer findCustomerByName(String name) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
