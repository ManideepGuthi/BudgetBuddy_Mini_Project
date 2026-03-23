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
                    addFixedExpense(user);
                    return;
                case 2:
                    addDailyExpense(user);
                    return;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
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