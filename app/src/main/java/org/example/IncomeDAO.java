package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {
    private static final String URL = "jdbc:sqlite:C:/Users/Administrator/Documents/AlexandersMint/personal_finance_database.db"; 

    // Method to add a new income record
    public void addIncome(int userID, double amount, String source, String description) {
        String sql = "INSERT INTO Income (UserID, Amount, Source, Description) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userID);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setString(3, source);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();
            System.out.println("Income added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding income: " + e.getMessage());
        }
    }

    // Method to retrieve all income records for a specific user
    public List<Income> getIncomeByUserID(int userID) {
        List<Income> incomeList = new ArrayList<>();
        String sql = "SELECT * FROM Income WHERE UserID = ?";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int incomeID = resultSet.getInt("IncomeID");
                double amount = resultSet.getDouble("Amount");
                String source = resultSet.getString("Source");
                String date = resultSet.getString("Date");
                String description = resultSet.getString("Description");

                Income income = new Income(incomeID, userID, amount, source, date, description);
                incomeList.add(income);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving income records: " + e.getMessage());
        }

        return incomeList;
    }

    // Method to update an income record
    public void updateIncome(int incomeID, double amount, String source, String description) {
        String sql = "UPDATE Income SET Amount = ?, Source = ?, Description = ? WHERE IncomeID = ?";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, source);
            preparedStatement.setString(3, description);
            preparedStatement.setInt(4, incomeID);
            preparedStatement.executeUpdate();
            System.out.println("Income updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating income: " + e.getMessage());
        }
    }

    // Method to delete an income record
    public void deleteIncome(int incomeID) {
        String sql = "DELETE FROM Income WHERE IncomeID = ?";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, incomeID);
            preparedStatement.executeUpdate();
            System.out.println("Income deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting income: " + e.getMessage());
        }
    }
}
