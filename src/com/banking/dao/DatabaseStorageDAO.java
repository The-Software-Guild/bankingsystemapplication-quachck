package com.banking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.banking.dto.BankAccount;
import com.banking.dto.Customer;
import com.banking.dto.FixedDepositAccount;
import com.banking.dto.SavingsAccount;
import com.mysql.jdbc.Statement;

public class DatabaseStorageDAO implements PersistenceDAO {

    @Override
    public void saveAllCustomers(List<Customer> customers) {
        Connection connection = openConnection();
        if (connection == null) {
            System.out.println("Could not establish a connection with the database");
            return;
        }

        try {
            for (Customer customer : customers) {
                String sql = "INSERT INTO customer (id, name, age, mobile_number, passport_number, dob) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, customer.getId());
                statement.setString(2, customer.getName());
                statement.setInt(3, customer.getAge());
                statement.setInt(4, customer.getMobileNumber());
                statement.setString(5, customer.getPassportNumber());
                statement.setDate(6, java.sql.Date.valueOf(customer.getDob()));

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new customer was inserted successfully!");
                }

                saveBankAccount(connection, customer.getBankAccount(), customer.getId());
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while inserting data into the database");
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    public long saveBankAccount(Connection connection, BankAccount bankAccount, int customerId) {
        long accountId = -1;
        try {
            String sql = "INSERT INTO bankaccount (account_number, bsb_code, bank_name, balance, opening_date, customer_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, bankAccount.getAccountNumber());
            statement.setLong(2, bankAccount.getBsbCode());
            statement.setString(3, bankAccount.getBankName());
            statement.setDouble(4, bankAccount.getBalance());
            statement.setDate(5, java.sql.Date.valueOf(bankAccount.getOpeningDate()));
            statement.setInt(6, customerId);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new bank account was inserted successfully!");

                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    accountId = rs.getLong(1);
                }
            }

            // Depending on the type of the account, insert into the corresponding table
            if (bankAccount instanceof SavingsAccount) {
                saveSavingsAccount(connection, (SavingsAccount) bankAccount, accountId);
            } else if (bankAccount instanceof FixedDepositAccount) {
                saveFixedDepositAccount(connection, (FixedDepositAccount) bankAccount, accountId);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while inserting bank account data into the database");
            e.printStackTrace();
        }

        return accountId;
    }


    public void saveSavingsAccount(Connection connection, SavingsAccount savingsAccount, long bankAccountId) {
        try {
            String sql = "INSERT INTO savingsaccount (account_id, is_salary_account, minimum_balance) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, bankAccountId);
            statement.setBoolean(2, savingsAccount.isSalaryAccount());
            statement.setDouble(3, savingsAccount.getMinimumBalance());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new savings account was inserted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while inserting savings account data into the database");
            e.printStackTrace();
        }
    }

    public void saveFixedDepositAccount(Connection connection, FixedDepositAccount fixedDepositAccount, long bankAccountId) {
        try {
            String sql = "INSERT INTO fixeddepositaccount (account_id, fixed_deposit_amount, tenure_years) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, bankAccountId);
            statement.setDouble(2, fixedDepositAccount.getDepositAmount());
            statement.setInt(3, fixedDepositAccount.getTenure());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new fixed deposit account was inserted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while inserting fixed deposit account data into the database");
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> retrieveAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        Connection connection = openConnection();
        if (connection == null) {
            System.out.println("Could not establish a connection with the database");
            return customers;
        }

        try {
            String sql = "SELECT * FROM customer";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet customerResults = statement.executeQuery();

            while (customerResults.next()) {
                int id = customerResults.getInt("id");
                String name = customerResults.getString("name");
                int age = customerResults.getInt("age");
                int mobileNumber = customerResults.getInt("mobile_number");
                String passportNumber = customerResults.getString("passport_number");
                LocalDate dob = customerResults.getDate("dob").toLocalDate();

                Customer customer = new Customer(name, age, mobileNumber, passportNumber, dob);
                customer.setId(id);
                customer.setBankAccount(retrieveBankAccountForCustomer(id, connection));

                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving data from the database");
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }

        return customers;
    }

    public BankAccount retrieveBankAccountForCustomer(int customerId, Connection connection) {
        try {
            String sql = "SELECT * FROM bankaccount WHERE customer_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, customerId);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                long accountNumber = results.getLong("account_number");
                long bsbCode = results.getLong("bsb_code");
                String bankName = results.getString("bank_name");
                double balance = results.getDouble("balance");
                LocalDate openingDate = results.getDate("opening_date").toLocalDate();
                long accountId = results.getLong("account_id");

                SavingsAccount savingsAccount = retrieveSavingsAccount(accountNumber, bsbCode, bankName, balance, openingDate, accountId, connection);
                if (savingsAccount != null) {
                    return savingsAccount;
                }

                FixedDepositAccount fixedDepositAccount = retrieveFixedDepositAccount(accountNumber, bsbCode, bankName, balance, openingDate, accountId, connection);
                if (fixedDepositAccount != null) {
                    return fixedDepositAccount;
                }

            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving bank account data from the database");
            e.printStackTrace();
        }

        return null;
    }

    public SavingsAccount retrieveSavingsAccount(long accountNumber, long bsbCode, String bankName, double balance, LocalDate openingDate, long accountId, Connection connection) {
        try {
            String sql = "SELECT * FROM savingsaccount WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, accountId);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                boolean isSalaryAccount = results.getBoolean("is_salary_account");
                double minimumBalance = results.getDouble("minimum_balance");
                
                SavingsAccount savingsAccount = new SavingsAccount(accountNumber, bsbCode, bankName, balance, openingDate, isSalaryAccount);
                savingsAccount.setMinimumBalance(minimumBalance);
                return savingsAccount;
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving savings account data from the database");
            e.printStackTrace();
        }

        return null;
    }

    public FixedDepositAccount retrieveFixedDepositAccount(long accountNumber, long bsbCode, String bankName, double balance, LocalDate openingDate, long accountId, Connection connection) {
        try {
            String sql = "SELECT * FROM fixeddepositaccount WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, accountId);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                double depositAmount = results.getDouble("fixed_deposit_amount");
                int tenure = results.getInt("tenure_years");

                return new FixedDepositAccount(accountNumber, bsbCode, bankName, balance, openingDate, depositAmount, tenure);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving fixed deposit account data from the database");
            e.printStackTrace();
        }

        return null;
    }

	
	public Connection openConnection() {
		//register for type 4 drive(pure java)
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver"); //type 4 driver is registered with DriverManager
			System.out.println("Successfully registered MySQL driver with DriverManager");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/bank", "root", "root");
			System.out.println("Successfully connected to: " + con);
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL suitable driver not found");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public void closeConnection(Connection c) {
		try {
			c.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
