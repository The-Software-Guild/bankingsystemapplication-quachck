package com.banking.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.banking.dto.Customer;
import com.banking.exceptions.CustomerNotFoundException;

public class FileStorageDAO implements PersistenceDAO {

	@Override
	public void saveAllCustomers(List<Customer> customers) {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream("C:\\customers.txt")))) {
			for (Customer customer : customers) {
				objectOutputStream.writeObject(customer);
			}
		} catch (IOException e) {
			System.out.println("An error occurred while saving customer data");
			e.printStackTrace();
		}
	}

	@Override
	public List<Customer> retrieveAllCustomers() {
		List<Customer> customers = new ArrayList<>();

		try (ObjectInputStream objectInputStream = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream("C:\\customers.txt")))) {
			while (true) {
				try {
					Customer customer = (Customer) objectInputStream.readObject();
					customers.add(customer);
				} catch (EOFException e) {
					// End of file reached
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("An error occurred while retrieving customer data");
			e.printStackTrace();
		}

		return customers;
	}

	@Override
	public Customer findCustomerByName(String name) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
