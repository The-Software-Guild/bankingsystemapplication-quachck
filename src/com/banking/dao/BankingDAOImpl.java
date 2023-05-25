package com.banking.dao;

import java.util.ArrayList;
import java.util.List;

import com.banking.dto.Customer;
import com.banking.exceptions.CustomerNotFoundException;

public class BankingDAOImpl implements BankingDAO {
	private static int customerIdCounter = 100;
	List<Customer> customers = new ArrayList<>();
	
	@Override
	public void createCustomer(Customer customer) {
		customer.setId(customerIdCounter++);
		customers.add(customer);
	}

	@Override
	public Customer findCustomerById(int id) throws CustomerNotFoundException {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        throw new CustomerNotFoundException("Customer not found with ID: " + id);
	}

	@Override
	public void updateCustomer(int id, Customer updatedCustomer) throws CustomerNotFoundException {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId() == id) {
                customers.set(i, updatedCustomer);
                System.out.println(customers.get(i).getBankAccount());
                return;
            }
        }
        throw new CustomerNotFoundException("Customer with id: " + id + " not found.");
    }

	@Override
	public List<Customer> getAllCustomers() {
		return customers;
	}

	@Override
	public Customer findCustomerByName(String name) throws CustomerNotFoundException {
		for (Customer customer : customers) {
            if (customer.getName().equals(name)) {
                return customer;
            }
        }
        throw new CustomerNotFoundException("Customer not found with name: " + name);
	}
}
