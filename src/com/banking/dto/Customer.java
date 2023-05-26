package com.banking.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class Customer implements Serializable {
	private int id;
	private String name;
	private int age;
	private int mobileNumber;
	private String passportNumber;
	private BankAccount bankAccount;
	private LocalDate dob;

	public Customer(String name, int age, int mobileNumber, String passportNumber, LocalDate dob) {
		this.name = name;
		this.age = age;
		this.mobileNumber = mobileNumber;
		this.passportNumber = passportNumber;
		this.dob = dob;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(int mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String toString() {
		return "Customer ID: " + id + "\nCustomer Name: " + name + "\nAge: " + age + "\nMobile Number: " + mobileNumber
				+ "\nPassport Number: " + passportNumber + "\nDOB: " + dob + "\nBank Account: " + bankAccount;
	}
}
