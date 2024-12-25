package org.example;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static final String URL = "jdbc:sqlite:C:/Users/Administrator/Documents/AlexandersMint/personal_finance_database.db";


    // Method to add a new user
    public void addUser(String username, String password, String email) {
        String sql = "INSERT INTO Users (Username, Password, Email) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
            System.out.println("User added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    // Method to retrieve a user by username
    public User getUser(String username) {
        String sql = "SELECT * FROM Users WHERE Username = ?";
    
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int userID = resultSet.getInt("UserID");
                String hashedPassword = resultSet.getString("Password");
                String email = resultSet.getString("Email");
                String createdAt = resultSet.getString("CreatedAt");
    
                // Return a new User object populated with the retrieved data
                return new User(userID, username, hashedPassword, email, createdAt);
            } else {
                System.out.println("User not found.");
            }
    
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }
    
        return null; // Return null if user is not found or an error occurs
    }
    


    public Integer getUserID(String username) {
        String sql = "SELECT UserID FROM Users WHERE Username = ?";
        Integer userID = null;

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userID = resultSet.getInt("UserID");
                System.out.println("User found with ID: " + userID);
            } else {
                System.out.println("User not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving user ID: " + e.getMessage());
        }

        return userID; // Will be null if the user was not found
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int userID = resultSet.getInt("UserID");
                String username = resultSet.getString("Username");
                String hashedPassword = resultSet.getString("Password");
                String createdAt = resultSet.getString("CreatedAt");
    
                return new User(userID, username, hashedPassword, email, createdAt);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user by email: " + e.getMessage());
        }
    
        return null; // Return null if no user found
    }

    public void addUser(User user) {
        String sql = "INSERT INTO Users (Username, Password, Email) VALUES (?, ?, ?)";
    
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getHashedPassword()); // Store hashed password
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
    
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }
    
    


    public String getUsernameByID(int userID) {
        String sql = "SELECT Username FROM Users WHERE UserID = ?";
        String username = null;

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                username = resultSet.getString("Username");
                System.out.println("Username for user ID " + userID + ": " + username);
            } else {
                System.out.println("User not found with ID: " + userID);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving username by ID: " + e.getMessage());
        }

        return username; // Will be null if the user was not found
    }

}
