package com.banking.io;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public int readInt(String message) {
        System.out.print(message);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter an integer: ");
            }
        }
    }

    public String readString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public double readDouble(String message) {
        System.out.print(message);
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a decimal number: ");
            }
        }
    }

    public long readLong(String message) {
        System.out.print(message);
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a long integer: ");
            }
        }
    }
    
    public LocalDate getDateFromUser(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        while (true) {  
            System.out.print(message);
            String inputDate = scanner.nextLine();

            try {
                return LocalDate.parse(inputDate, formatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }
    }
}

