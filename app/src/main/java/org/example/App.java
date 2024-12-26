package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;



public class App {

    private static User loggedInUser = null;


    private static final String URL = "jdbc:sqlite:personal_finance_tracker.db";

    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Generate the hash bytes
            byte[] hashBytes = digest.digest(password.getBytes());

            // Convert the hash bytes to a base64-encoded string
            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }


    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n---Main Menu---");
            System.out.println("1) Log In");
            System.out.println("2) Expenses");
            System.out.println("3) Incomes");
            System.out.println("4) Balance Sheet Summary");
            System.out.println("5) Logout");
            System.out.println("6) Quit");
            System.out.println("7) Register");
            System.out.print("Select Option: ");

            try {
                int option = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer

                switch (option) {
                    case 1:
                        loginMenu(scanner);
                        break;
                    case 2:
                        expenseMenu(scanner);
                        break;
                    case 3:
                        checkIncomes();
                        break;
                    case 4:
                        balanceSheetSummary();
                        break;
                    case 5:
                        logout();
                        break;
                    case 6:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    case 7:
                        registerNewUser(scanner);
                        break;
                        
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    // Logout procedure
    public void logout() {
        if (loggedInUser != null) {
            System.out.println("Goodbye, " + loggedInUser.getUsername() + "!");
            loggedInUser = null; // Clear session
        } else {
            System.out.println("You are not logged in.");
        }
    }

    private void registerNewUser(Scanner scanner) {
        UserDAO userDAO = new UserDAO();
    
        System.out.println("--- Register New User ---");
    
        // Prompt for username
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
    
        // Check if username already exists
        if (userDAO.getUser(username) != null) {
            System.out.println("Username already exists. Please try another one.");
            return;
        }
    
        // Prompt for email
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
    
        // Check if email already exists
        if (userDAO.getUserByEmail(email) != null) { // You need to implement getUserByEmail in UserDAO
            System.out.println("Email already exists. Please try another one.");
            return;
        }
    
        // Prompt for password twice
        String password = null;
        while (true) {
            System.out.print("Enter password: ");
            String password1 = scanner.nextLine();
    
            System.out.print("Confirm password: ");
            String password2 = scanner.nextLine();
    
            if (password1.equals(password2)) {
                password = password1;
                break;
            } else {
                System.out.println("Passwords do not match. Please try again.");
            }
        }
    
        // Hash the password
        String hashedPassword = hashPassword(password);
    
        // Create new user
        User newUser = new User(username, hashedPassword, email);
        userDAO.addUser(newUser);
    
        System.out.println("Registration successful! You can now log in.");
    }



    private void handleAddExpense(Scanner scanner, ExpenseDAO expenseDAO) {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }
    
        System.out.println("Adding a new expense...");
    
        try {
            System.out.print("Enter the expense amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
    
            System.out.print("Enter the expense category: ");
            String category = scanner.nextLine().trim();
    
            System.out.print("Enter a description (optional): ");
            String description = scanner.nextLine().trim();
    
            System.out.print("Enter the payment frequency in days (0 for one-time payments): ");
            int frequency = Integer.parseInt(scanner.nextLine().trim());
    
            // Call the DAO method to add the expense
            expenseDAO.addExpense(loggedInUser.getUserID(), amount, category, description, frequency);
    
            System.out.println("Expense added successfully!");
    
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please ensure you enter numeric values for amount and frequency.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
  
    
    private void deleteExpenseMenu() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }
    
        ExpenseDAO expenseDAO = new ExpenseDAO();
        handleCheckExpenses(expenseDAO); // Show all the expenses so user can select which one to delete
    
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the Expense ID to delete: ");
        try {
            int expenseID = Integer.parseInt(scanner.nextLine().trim());
            deleteExpense(expenseID);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Expense ID. Please enter a valid number.");
        }
    
        // Do NOT close the scanner
    }

    private void deleteExpense(int expenseID) {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }
    
        ExpenseDAO expenseDAO = new ExpenseDAO(); // Instantiate DAO
    
        // Fetch the expense by ID to verify ownership
        Expense expense = expenseDAO.getExpenseByID(expenseID);
        if (expense == null) {
            System.out.println("Expense with ID " + expenseID + " does not exist.");
            return;
        }
    
        // Check if the logged-in user is the owner of the expense
        if (expense.getUserID() != loggedInUser.getUserID()) {
            System.out.println("You can only delete your own expenses.");
            return;
        }
    
        // Proceed with deletion if ownership is verified
        boolean isDeleted = expenseDAO.deleteExpense(expenseID);
        if (isDeleted) {
            System.out.println("Expense with ID " + expenseID + " deleted successfully.");
        } else {
            System.out.println("Error: Expense with ID " + expenseID + " could not be deleted.");
        }
    }
    
    

    private void handleCheckExpenses(ExpenseDAO expenseDAO) {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }
    
        System.out.println("Showing expenses for " + loggedInUser.getUsername());
        System.out.println("Your Expenses:");
        System.out.println("ExpenseID  Category        Amount     Frequency  Description          Date");
        System.out.println("--------------------------------------------------------------------------");
    
        List<Expense> expenses = expenseDAO.getExpensesByUserID(loggedInUser.getUserID());
    
        for (Expense expense : expenses) {
            System.out.printf(
                "%-10d %-15s %-10.2f %-10d %-20s %-20s%n",
                expense.getExpenseID(),
                expense.getCategory(),
                expense.getAmount(),
                expense.getFrequency(), 
                expense.getDescription(),
                expense.getDate()
            );
        }
    }
    
    

    // 1) Check expenses   
    // 2) Add an expense
    // 3) Delete an Expense   
    public void expenseMenu(Scanner scanner) {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }
    
        System.out.println("\n--- Expense Menu ---");
    
        boolean inExpenseMenu = true; // Control loop
        while (inExpenseMenu) {
            System.out.println("\n1) Check Expenses");
            System.out.println("2) Add an Expense");
            System.out.println("3) Delete an Expense");
            System.out.println("4) Exit");
            System.out.print("Select Option: ");
    
            int option;
            try {
                option = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
                continue; // Retry
            }
    
            switch (option) {
                case 1:
                    System.out.println("Showing expenses for " + loggedInUser.getUsername());
                    // Add logic for checking expenses
                    handleCheckExpenses(new ExpenseDAO());
                    break;
    
                case 2:
                    System.out.println("Adding a new expense...");
                    // Add logic for adding an expense
                    handleAddExpense(scanner, new ExpenseDAO());
                    break;
    
                case 3:
                    System.out.println("Deleting an expense...");
                    deleteExpenseMenu();
                    break;
    
                case 4:
                    System.out.println("Exiting Expense Menu.");
                    inExpenseMenu = false; // Exit loop
                    break;
    
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    
    
    
    
    
    

    public void checkIncomes() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.println("Showing incomes for " + loggedInUser.getUsername());
        IncomeDAO incomeDAO = new IncomeDAO();
        List<Income> incomes = incomeDAO.getIncomeByUserID(loggedInUser.getUserID());
        for (Income income : incomes) {
            System.out.println("Source: " + income.getSource() + ", Amount: " + income.getAmount());
        }
    }

    public void balanceSheetSummary() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.println("Balance Sheet Summary for " + loggedInUser.getUsername());
        // Placeholder for summary logic
    }





    /*public class LoginMenuTracker {
        public static boolean haveTheySeenThisMenuBefore = false;
    
        public static boolean hasSeenMenu() {
            return haveTheySeenThisMenuBefore;
        }
    
        public static void setSeenMenu(boolean seen) {
            haveTheySeenThisMenuBefore = seen;
        }
    } */

    /*public void loginProcedure(String Username, String unencryptedPassword){       

        String encryptedPassword = hashPassword(unencryptedPassword);



    }*/


    public void loginMenu(Scanner scanner) {
        if (loggedInUser != null) {
            System.out.println("You are already logged in as " + loggedInUser.getUsername());
            return;
        }

        UserDAO userDAO = new UserDAO();

        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userDAO.getUser(username); // Retrieve user from the database
        if (user != null && hashPassword(password).equals(user.getHashedPassword())) {
            loggedInUser = user; // Set logged-in user
            System.out.println("Login successful! Welcome, " + loggedInUser.getUsername() + ".");
        } else {
            System.out.println("Invalid username or password.");
        }
    }


    public void incomeMenu(){
        System.out.println("---Income Menu---");
    }

    public void balanceMenu(){
        System.out.println("---Balance Sheet Menu---");
    }




    /*public void testDatabase(){


        try (Connection connection = DriverManager.getConnection(URL)) {
            if (connection != null) {
                System.out.println("Connection to SQLite has been established.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        UserDAO userDAOTest = new UserDAO();
        userDAOTest.addUser("Alex", "password", "alexintka1@gmail.com");

        ExpenseDAO expenseDAOTest = new ExpenseDAO();
        expenseDAOTest.addExpense(userDAOTest.getUserID("Alex"), 100, "Weekly Expenses","Groceries");

        IncomeDAO incomeDAOTest = new IncomeDAO();
        incomeDAOTest.addIncome(userDAOTest.getUserID("Alex"), 500.0, "IncomeTest", "Income from test");


        Integer userID = userDAOTest.getUserID("Alex"); //Check if user exists first
        if (userID != null) {
            System.out.println("Expenses for user " + userID + ":");
            List<Expense> expenses = expenseDAOTest.getExpensesByUserID(userID);
            for (Expense expense : expenses) {
                System.out.println("Category: " + expense.getCategory() + ", Amount: $" + expense.getAmount() + ", Date: " + expense.getDate() + ", ExpenseID: " + expense.getExpenseID());
            }

            System.out.println("Incomes for user "+ userID + ":");
            List<Income> incomes = incomeDAOTest.getIncomeByUserID(userID);
            for(Income income : incomes){
                System.out.println("Income Source: " + income.getSource() + ", Amount: $" + income.getAmount() + ", Date: " + income.getDate() + ", IncomeID: " + income.getIncomeID());
            }

        }


    } */



    

    public static void main(String[] args) {

        
        System.out.println("Welcome to the Personal Finance Tracker!");

        // Test database connection
        App app = new App();
        try (Connection connection = DriverManager.getConnection(URL)) {
            if (connection != null) {
                System.out.println("Connection to SQLite has been established.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //app.testDatabase();
        app.mainMenu();










    }
}
