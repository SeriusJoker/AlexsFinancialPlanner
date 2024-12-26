package org.example;

public class Expense {
    private int expenseID;
    private int userID;
    private double amount;
    private String category;
    private String date;
    private String description;
    private int frequency;

    public Expense(int expenseID, int userID, double amount, String category, String date, String description, int frequency) {
        this.expenseID = expenseID;
        this.userID = userID;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.frequency = frequency;
    }

    // Getters and Setters
    public int getExpenseID() { return expenseID; }
    public int getUserID() { return userID; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public int getFrequency() {return frequency;}
}
