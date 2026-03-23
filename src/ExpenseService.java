import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ExpenseService {
    Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();
    GoalService goalService = new GoalService();

    public void showExpenseMenu(User user) {
        while (true) {
            System.out.println("\n===== ADD EXPENSE =====");
            System.out.println("1. Fixed Expense (Rent, EMI, Bills)");
            System.out.println("2. Daily Expense (Groceries, Snacks)");
            System.out.println("3. Back");
            System.out.print("\nPlease select type (1-3): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    showFixedExpenseMenu(user);
                    break;
                case 2:
                    showDailyExpenseMenu(user);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    public void showFixedExpenseMenu(User user) {
        while (true) {
            System.out.println("\n===== MANAGE FIXED EXPENSES =====");
            System.out.println("1. Add New Fixed Expense");
            System.out.println("2. View Fixed Expenses");
            System.out.println("3. Delete Fixed Expense");
            System.out.println("4. Back");
            System.out.print("\nPlease select an option (1-4): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addFixedExpense(user);
                    break;
                case 2:
                    viewFixedExpenses(user);
                    break;
                case 3:
                    deleteFixedExpense(user);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void viewFixedExpenses(User user) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "select * from bb_fixed_expenses where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Your Fixed Expenses ---");
            boolean found = false;
            while (rs.next()) {
                found = true;
                // Demonstrating OOP: Creating a specific FixedExpense object
                FixedExpense fe = new FixedExpense(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"), rs.getDouble("amount"));
                fe.displayDetails(); // Calling overridden method (Polymorphism)
            }
            if (!found) System.out.println("No fixed expenses added yet.");
        } catch (Exception e) {
            System.out.println("Error viewing fixed expenses!");
        }
    }

    private void deleteFixedExpense(User user) {
        try {
            viewFixedExpenses(user);
            System.out.print("\nEnter the exact name of the expense to delete: ");
            String title = sc.nextLine();

            Connection con = DBConnection.getConnection();
            
            // 1. Get the amount before deleting
            String selectQuery = "select amount from bb_fixed_expenses where user_id=? and title=?";
            PreparedStatement selectPs = con.prepareStatement(selectQuery);
            selectPs.setInt(1, user.getId());
            selectPs.setString(2, title);
            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {
                double amount = rs.getDouble("amount");

                // 2. Delete the record
                String deleteQuery = "delete from bb_fixed_expenses where user_id=? and title=?";
                PreparedStatement deletePs = con.prepareStatement(deleteQuery);
                deletePs.setInt(1, user.getId());
                deletePs.setString(2, title);
                deletePs.executeUpdate();

                // 3. Refund the balance
                double newBalance = user.getBalance() + amount;
                userService.updateBalance(user, newBalance);

                System.out.println("Fixed expense deleted and Rs. " + amount + " refunded to balance!");
            } else {
                System.out.println("Expense not found!");
            }
        } catch (Exception e) {
            System.out.println("Error deleting fixed expense!");
        }
    }

    public void addFixedExpense(User user) {
        try {
            System.out.println("\n--- Add Fixed Expense ---");
            System.out.println("1. Rent");
            System.out.println("2. EMI");
            System.out.println("3. Electricity Bill");
            System.out.println("4. Custom (Enter your own)");
            System.out.print("\nSelect an option (1-4): ");
            int option = sc.nextInt();
            sc.nextLine();

            String name = "";
            switch (option) {
                case 1: name = "Rent"; break;
                case 2: name = "EMI"; break;
                case 3: name = "Electricity Bill"; break;
                case 4:
                    System.out.print("Enter fixed expense name: ");
                    name = sc.nextLine();
                    break;
                default:
                    System.out.println("Invalid selection!");
                    return;
            }

            System.out.print("Enter fixed expense amount: ");
            double amount = sc.nextDouble();
            sc.nextLine();

            if (amount <= 0) {
                System.out.println("Amount must be greater than 0!");
                return;
            }

            if (amount > user.getBalance()) {
                System.out.println("Expense exceeds balance! Returning to main menu.");
                return;
            }

            Connection con = DBConnection.getConnection();

            String query = "insert into bb_fixed_expenses(user_id,title,amount) values(?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ps.setString(2, name);
            ps.setDouble(3, amount);
            ps.executeUpdate();

            double newBalance = user.getBalance() - amount;
            userService.updateBalance(user, newBalance);

            System.out.println("Fixed expense added!");
            System.out.println("Current balance: " + user.getBalance());

        } catch (Exception e) {
            System.out.println("Error adding fixed expense!");
        }
    }

    public void showDailyExpenseMenu(User user) {
        while (true) {
            System.out.println("\n===== MANAGE DAILY EXPENSES =====");
            System.out.println("1. Add New Daily Expense");
            System.out.println("2. View Daily Expenses");
            System.out.println("3. Delete Daily Expense");
            System.out.println("4. Back");
            System.out.print("\nPlease select an option (1-4): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addDailyExpense(user);
                    break;
                case 2:
                    viewDailyExpenses(user);
                    break;
                case 3:
                    deleteDailyExpense(user);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void viewDailyExpenses(User user) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "select * from bb_daily_expenses where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Your Daily Expenses ---");
            boolean found = false;
            while (rs.next()) {
                found = true;
                // Demonstrating OOP: Creating a specific DailyExpense object
                DailyExpense de = new DailyExpense(rs.getInt("id"), rs.getInt("user_id"), rs.getString("category"), rs.getDouble("amount"));
                de.displayDetails(); // Calling overridden method (Polymorphism)
            }
            if (!found) System.out.println("No daily expenses added yet.");
        } catch (Exception e) {
            System.out.println("Error viewing daily expenses!");
        }
    }

    private void deleteDailyExpense(User user) {
        try {
            viewDailyExpenses(user);
            System.out.print("\nEnter the exact category of the expense to delete: ");
            String category = sc.nextLine();

            Connection con = DBConnection.getConnection();
            
            // 1. Get the amount before deleting
            String selectQuery = "select amount from bb_daily_expenses where user_id=? and category=?";
            PreparedStatement selectPs = con.prepareStatement(selectQuery);
            selectPs.setInt(1, user.getId());
            selectPs.setString(2, category);
            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {
                double amount = rs.getDouble("amount");

                // 2. Delete the record
                String deleteQuery = "delete from bb_daily_expenses where user_id=? and category=?";
                PreparedStatement deletePs = con.prepareStatement(deleteQuery);
                deletePs.setInt(1, user.getId());
                deletePs.setString(2, category);
                deletePs.executeUpdate();

                // 3. Refund the balance
                double newBalance = user.getBalance() + amount;
                userService.updateBalance(user, newBalance);

                System.out.println("Daily expense deleted and Rs. " + amount + " refunded to balance!");
            } else {
                System.out.println("Expense not found!");
            }
        } catch (Exception e) {
            System.out.println("Error deleting daily expense!");
        }
    }

    public void addDailyExpense(User user) {
        try {
            System.out.println("\n--- Add Daily Expense ---");
            System.out.println("1. Groceries");
            System.out.println("2. Snacks");
            System.out.println("3. Entertainment");
            System.out.println("4. Transport");
            System.out.println("5. Custom (Enter your own)");
            System.out.print("\nSelect an option (1-5): ");
            int option = sc.nextInt();
            sc.nextLine();

            String category = "";
            switch (option) {
                case 1: category = "Groceries"; break;
                case 2: category = "Snacks"; break;
                case 3: category = "Entertainment"; break;
                case 4: category = "Transport"; break;
                case 5:
                    System.out.print("Enter daily expense category: ");
                    category = sc.nextLine();
                    break;
                default:
                    System.out.println("Invalid selection!");
                    return;
            }

            System.out.print("Enter daily expense amount: ");
            double amount = sc.nextDouble();
            sc.nextLine();

            if (amount <= 0) {
                System.out.println("Amount must be greater than 0!");
                return;
            }

            if (amount > user.getBalance()) {
                System.out.println("Expense exceeds balance! Returning to main menu.");
                return;
            }

            Connection con = DBConnection.getConnection();

            String query = "insert into bb_daily_expenses(user_id,category,amount) values(?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ps.setString(2, category);
            ps.setDouble(3, amount);
            ps.executeUpdate();

            double newBalance = user.getBalance() - amount;
            userService.updateBalance(user, newBalance);

            System.out.println("Daily expense added!");
            System.out.println("Current balance: " + user.getBalance());

        } catch (Exception e) {
            System.out.println("Error adding daily expense!");
        }
    }

    public double getTotalFixedExpenses(User user) {
        double total = 0;

        try {
            Connection con = DBConnection.getConnection();

            String query = "select ifnull(sum(amount),0) as total from bb_fixed_expenses where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (Exception e) {
            System.out.println("Error getting fixed expenses!");
        }

        return total;
    }

    public double getTotalDailyExpenses(User user) {
        double total = 0;

        try {
            Connection con = DBConnection.getConnection();

            String query = "select ifnull(sum(amount),0) as total from bb_daily_expenses where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (Exception e) {
            System.out.println("Error getting daily expenses!");
        }

        return total;
    }

    public void showSummary(User user, CartService cartService) {
        double fixed = getTotalFixedExpenses(user);
        double daily = getTotalDailyExpenses(user);
        double platform = cartService.getTotalPlatformSpending(user);
        double goals = goalService.getTotalGoalSavings(user);
        double total = fixed + daily + platform + goals;

        System.out.println("\n===== EXPENSE SUMMARY =====");
        System.out.println("Salary            : " + user.getSalary());
        System.out.println("Fixed Expenses    : " + fixed);
        System.out.println("Daily Expenses    : " + daily);
        System.out.println("Platform Spending : " + platform);
        System.out.println("Goal Savings      : " + goals);
        System.out.println("Total Outflow     : " + total);
        System.out.println("Remaining Balance : " + user.getBalance());
    }
}