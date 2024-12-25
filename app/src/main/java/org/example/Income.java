package org.example;

public class Income {
    private int incomeID;
    private int userID;
    private double amount;
    private String source;
    private String date;
    private String description;

    public Income(int incomeID, int userID, double amount, String source, String date, String description) {
        this.incomeID = incomeID;
        this.userID = userID;
        this.amount = amount;
        this.source = source;
        this.date = date;
        this.description = description;
    }

    // Getters and Setters
    public int getIncomeID() { return incomeID; }
    public int getUserID() { return userID; }
    public double getAmount() { return amount; }
    public String getSource() { return source; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
}
