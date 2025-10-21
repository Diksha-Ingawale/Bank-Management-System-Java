// BankManagementSystem.java
// Version 1.0
// Created by Diksha Ingawale
// A simple file-based banking system using Java

import java.io.*;
import java.util.*;

class Account {
    int accNo;
    String name;
    String password;
    double balance;
    String transactions = "";

    void createAccount(int no, String n, String pass, double bal) {
        accNo = no;
        name = n;
        password = pass;
        balance = bal;
        transactions = "Account created with " + bal + "\n";
    }

    void deposit(double amt) {
        balance = balance + amt;
        transactions = transactions + "Deposited " + amt + "\n";
    }

    void withdraw(double amt) {
        if (amt <= balance) {
            balance = balance - amt;
            transactions = transactions + "Withdrawn " + amt + "\n";
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    void showBalance() {
        System.out.println("Current Balance: " + balance);
    }

    void showTransactions() {
        System.out.println("----- Transaction History -----");
        System.out.println(transactions);
    }
}

public class BankManagementSystem {
    static Account[] accounts = new Account[100];
    static int count = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String args[]) {
        loadAccountsFromFile();

        int choice = 0;
        while (choice != 4) {
            System.out.println("\n====== BANK MENU ======");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. View All Accounts");
            System.out.println("4. Save & Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    loginAccount();
                    break;
                case 3:
                    viewAccounts();
                    break;
                case 4:
                    saveAccountsToFile();
                    System.out.println("Exiting... All accounts saved.");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    static void createAccount() {
        System.out.print("Enter Account No: ");
        int no = sc.nextInt();
        sc.nextLine(); // clear buffer

        System.out.print("Enter Name: ");
        String n = sc.nextLine();

        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        System.out.print("Enter Initial Balance: ");
        double bal = sc.nextDouble();

        Account acc = new Account();
        acc.createAccount(no, n, pass, bal);
        accounts[count] = acc;
        count++;

        System.out.println("Account Created Successfully!");
    }

    static void loginAccount() {
        System.out.print("Enter Account No: ");
        int no = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        int found = -1;
        for (int i = 0; i < count; i++) {
            if (accounts[i].accNo == no && accounts[i].password.equals(pass)) {
                found = i;
                break;
            }
        }

        if (found != -1) {
            System.out.println("Login Successful!");
            accountMenu(accounts[found]);
        } else {
            System.out.println("Invalid Account or Password!");
        }
    }

    static void accountMenu(Account acc) {
        int ch = 0;
        while (ch != 4) {
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Transactions");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter Amount: ");
                    double depositAmt = sc.nextDouble();
                    acc.deposit(depositAmt);
                    break;
                case 2:
                    System.out.print("Enter Amount: ");
                    double withdrawAmt = sc.nextDouble();
                    acc.withdraw(withdrawAmt);
                    break;
                case 3:
                    acc.showTransactions();
                    acc.showBalance();
                    break;
                case 4:
                    System.out.println("Logged out successfully!");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    static void viewAccounts() {
        if (count == 0) {
            System.out.println("No accounts available.");
        } else {
            System.out.println("\nList of Accounts:");
            for (int i = 0; i < count; i++) {
                System.out.println(accounts[i].accNo + " - " + accounts[i].name + " (" + accounts[i].balance + ")");
            }
        }
    }

    static void saveAccountsToFile() {
        try {
            FileWriter fw = new FileWriter("accounts.txt");
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < count; i++) {
                Account a = accounts[i];
                bw.write(a.accNo + "," + a.name + "," + a.password + "," + a.balance + "," + a.transactions.replace("\n", "|") + "\n");
            }

            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    static void loadAccountsFromFile() {
        try {
            File file = new File("data/accounts.txt");
            file.getParentFile().mkdirs(); // create folder if not exist
            if (!file.exists()) {
                return;
            }

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Account a = new Account();
                a.accNo = Integer.parseInt(data[0]);
                a.name = data[1];
                a.password = data[2];
                a.balance = Double.parseDouble(data[3]);
                a.transactions = data[4].replace("|", "\n");
                accounts[count] = a;
                count++;
            }

            br.close();
            fr.close();
            System.out.println("Accounts loaded successfully from file!");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}
