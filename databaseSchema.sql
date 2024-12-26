-- Drop existing tables
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Expenses;
DROP TABLE IF EXISTS Income;
DROP TABLE IF EXISTS Budgets;

-- Recreate the Users table
CREATE TABLE Users (
    UserID INTEGER PRIMARY KEY AUTOINCREMENT,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Recreate the Expenses table with Frequency column
CREATE TABLE Expenses (
    ExpenseID INTEGER PRIMARY KEY AUTOINCREMENT,
    UserID INTEGER NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    Category VARCHAR(50) NOT NULL,
    Date DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    Description TEXT,
    Frequency INTEGER DEFAULT 0, -- New column for payment frequency (in days)
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Recreate the Income table with Frequency column
CREATE TABLE Income (
    IncomeID INTEGER PRIMARY KEY AUTOINCREMENT,
    UserID INTEGER NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    Source VARCHAR(50) NOT NULL,
    Date DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    Description TEXT,
    Frequency INTEGER DEFAULT 0, -- New column for payment frequency (in days)
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Recreate the Budgets table
CREATE TABLE Budgets (
    BudgetID INTEGER PRIMARY KEY AUTOINCREMENT,
    UserID INTEGER NOT NULL,
    Category VARCHAR(50) NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    StartDate DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    EndDate DATETIME,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
