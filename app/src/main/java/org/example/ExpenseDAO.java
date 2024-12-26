package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private static final String URL = "jdbc:sqlite:C:/Users/Administrator/Documents/AlexandersMint/personal_finance_database.db"; // Use the absolute path for now

    // Method to add a new expense
    public void addExpense(int userID, double amount, String category, String description, int frequency) {
        String sql = "INSERT INTO Expenses (UserID, Amount, Category, Description, Frequency) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    
            preparedStatement.setInt(1, userID);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setString(3, category);
            preparedStatement.setString(4, description);
            preparedStatement.setInt(5, frequency);
            preparedStatement.executeUpdate();
            System.out.println("Expense added successfully!");
    
        } catch (SQLException e) {
            System.out.println("Error adding expense: " + e.getMessage());
        }
    }
    

    // Method to retrieve all expenses for a specific user
    public List<Expense> getExpensesByUserID(int userID) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM Expenses WHERE UserID = ?";
    
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                Expense expense = new Expense(
                    resultSet.getInt("ExpenseID"),
                    resultSet.getInt("UserID"),
                    resultSet.getDouble("Amount"),
                    resultSet.getString("Category"),
                    resultSet.getString("Date"),
                    resultSet.getString("Description"),
                    resultSet.getInt("Frequency") // Retrieve Frequency
                );
                expenses.add(expense);
            }
    
        } catch (SQLException e) {
            System.out.println("Error retrieving expenses: " + e.getMessage());
        }
    
        return expenses;
    }
    

    // Method to update an expense
    public void updateExpense(int expenseID, double amount, String category, String description) {
        String sql = "UPDATE Expenses SET Amount = ?, Category = ?, Description = ? WHERE ExpenseID = ?";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, category);
            preparedStatement.setString(3, description);
            preparedStatement.setInt(4, expenseID);
            preparedStatement.executeUpdate();
            System.out.println("Expense updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating expense: " + e.getMessage());
        }
    }

    public Expense getExpenseByID(int expenseID) {
        String sql = "SELECT * FROM Expenses WHERE ExpenseID = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    
            preparedStatement.setInt(1, expenseID);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                return new Expense(
                    resultSet.getInt("ExpenseID"),
                    resultSet.getInt("UserID"),
                    resultSet.getDouble("Amount"),
                    resultSet.getString("Category"),
                    resultSet.getString("Date"),
                    resultSet.getString("Description"),
                    resultSet.getInt("Frequency")
                );
            }
    
        } catch (SQLException e) {
            System.out.println("Error retrieving expense: " + e.getMessage());
        }
    
        return null;
    }
    

    // Method to delete an expense
    public boolean deleteExpense(int expenseID) {
        String sql = "DELETE FROM Expenses WHERE ExpenseID = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    
            preparedStatement.setInt(1, expenseID);
            int rowsAffected = preparedStatement.executeUpdate();
    
            return rowsAffected > 0; // Return true if a row was deleted
    
        } catch (SQLException e) {
            System.out.println("Error deleting expense: " + e.getMessage());
            return false; // Return false on error
        }
    }
    
    



    
}
