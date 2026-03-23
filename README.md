# BudgetBuddy (Simple)

`BudgetBuddy_Simple` is a Java-based personal finance management application designed to help users track their income and expenses efficiently. It features a console-based interface and uses a MySQL database for secure data persistence.

## 🚀 Key Features

- **User Authentication**: Secure registration and login system.
- **Consolidated Expense Tracking**: Easily add both Fixed (Rent, EMI) and Daily (Groceries, Snacks) expenses from a single "Add Expense" menu.
- **Goal Tracking**: Set financial goals for specific products, add savings, and track your progress with real-time percentage updates.
- **Platform Spending & Cart**: Explore products from various platforms (Shopping, Food, Ride) and manage your cart (View, Remove, Checkout) all in one place.
- **Smart Analysis**: Get visual financial insights through text-based bar charts and personalized spending advice.
- **Account Settings**: Update profile information, change passwords, and reset monthly data while keeping fixed expenses.

## 🛠️ Technologies Used

- **Language**: Java
- **Database**: MySQL
- **API**: JDBC (Java Database Connectivity)

## 🧩 OOP Concepts in the Project

The project demonstrates several core Object-Oriented Programming (OOP) concepts:

### 1. Encapsulation
Encapsulation is the practice of bundling data and methods that operate on that data within a single unit (class) and restricting direct access to some components.

- **Example**: [User.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/User.java)
    - **Line 2-6**: Fields like `username`, `password`, `salary`, and `balance` are marked `private`.
    - **Line 19-57**: Access to these fields is provided only through public `getter` and `setter` methods, ensuring data integrity and validation (e.g., checking for negative salary).

### 2. Abstraction
Abstraction involves hiding complex implementation details and showing only the essential features of an object.

- **Example**: [DBConnection.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/DBConnection.java)
    - **Line 5-19**: The `getConnection()` method abstracts away the JDBC URL, driver loading, and credential management. Other classes simply call this method to interact with the database without knowing the underlying connection details.
- **Example**: [AnalysisService.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/AnalysisService.java)
    - **Line 3**: The `showAnalysis` method provides a simple interface for generating a complex financial report, hiding the internal logic of calculating percentages and rendering progress bars.

### 3. Classes and Objects
The application is structured into logical components using classes, which serve as blueprints for objects.

- **Example**: [MainApp.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/MainApp.java)
    - **Line 7-11**: The application creates objects of service classes like `UserService`, `ExpenseService`, and `CartService` to delegate specific responsibilities.
- **Example**: [User.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/User.java)
    - **Line 1**: Defines the `User` class template.
    - **Line 79** in [UserService.java](file:///c:/Users/iicte/IdeaProjects/BudgetBuddy_Simple/src/UserService.java): Instantiates a `new User()` object to hold data for the currently logged-in user.

---
*Created by Antigravity*
