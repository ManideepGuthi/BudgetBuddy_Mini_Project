# BudgetBuddy (Simple) 💸

`BudgetBuddy_Simple` is a Java application for personal finance management. It helps users track income, manage recurring and daily expenses, set financial goals, and gain insights through smart analysis, console-based terminal interface.



## 🚀 Key Features

- **User Authentication**: Secure registration and login with salary management.
- **Consolidated Expense Tracking**: Manage both **Fixed Expenses** (Rent, EMI, Bills) and **Daily Expenses** (Groceries, Entertainment, Transport) from a single hub.
- **Expense Management**: View and Delete logged expenses. Deleting an expense automatically **refunds** the amount back to your balance!
- **Goal Tracking**: Set targets for specific products/prices and track your savings progress with visual percentage bars.
- **Platform Spending**: Explore products from Food, Ride (with fare calculation), and Shopping platforms, seamlessly integrated into your budget.
- **Smart Analysis**: Visual text-based bar charts and personalized financial advice based on your spending patterns.
- **Account Settings**: Update profile, change passwords, and perform monthly resets while preserving critical data.




## 🧩 Advanced OOP Concepts

This project is a showcase of robust Object-Oriented Programming principles:

### 1. Inheritance & Abstraction
The project uses a clear class hierarchy for expense management.
- **Example**: [Expense.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/Expense.java)
    - This is an **Abstract Class** that defines the core structure of an expense.
    - Subclasses like [FixedExpense.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/FixedExpense.java) and [DailyExpense.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/DailyExpense.java) inherit from it, demonstrating code reuse.

### 2. Polymorphism
Different expense types are handled through a single interface.
- **Example**: [ExpenseService.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/ExpenseService.java)
    - The `displayDetails()` method is overridden in subclasses. The service calls this method on `Expense` objects, and Java automatically executes the correct version based on the specific type (Fixed or Daily).

### 3. Encapsulation
Data integrity is maintained by restricting direct access to fields.
- **Example**: [User.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/User.java)
    - Fields like `salary` and `balance` are `private`, accessible only through public getters and setters to ensure safe state changes.

### 4. Modular Design
The application is organized into separate **Service** classes (e.g., `GoalService`, `AnalysisService`), each responsible for a single functional area, following the **Single Responsibility Principle**.



## 🛠️ Tech Stack
- **Language**: Java
- **Database**: MySQL
- **Driver**: JDBC (MySQL Connector/J)

---
*Created with ❤️ by Manu*
