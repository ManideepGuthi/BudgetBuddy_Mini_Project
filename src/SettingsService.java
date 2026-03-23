import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class SettingsService {
    Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();

    public void showSettings(User user) {
        while (true) {
            System.out.println("\n===== SETTINGS =====");
            System.out.println("1. Update Password");
            System.out.println("2. Update Salary");
            System.out.println("3. Reset Monthly Data");
            System.out.println("4. Back");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    updatePassword(user);
                    break;
                case 2:
                    updateSalary(user);
                    break;
                case 3:
                    resetMonthlyData(user);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    public void updatePassword(User user) {
        try {
            System.out.print("Enter new password: ");
            String newPassword = sc.nextLine();

            Connection con = DBConnection.getConnection();

            String query = "update bb_users set password=? where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, newPassword);
            ps.setInt(2, user.getId());

            ps.executeUpdate();

            user.setPassword(newPassword);

            System.out.println("Password updated successfully!");

        } catch (Exception e) {
            System.out.println("Error updating password!");
        }
    }

    public void updateSalary(User user) {
        try {
            System.out.print("Enter new salary: ");
            double salary = sc.nextDouble();
            sc.nextLine();

            if (salary <= 0) {
                System.out.println("Salary must be greater than 0!");
                return;
            }

            userService.updateSalary(user, salary);

            System.out.println("Salary updated successfully!");
            System.out.println("New Balance: " + user.getBalance());

        } catch (Exception e) {
            System.out.println("Error updating salary!");
        }
    }

    public void resetMonthlyData(User user) {
        try {
            Connection con = DBConnection.getConnection();

            String q1 = "delete from bb_cart where user_id=?";
            PreparedStatement ps1 = con.prepareStatement(q1);
            ps1.setInt(1, user.getId());
            ps1.executeUpdate();

            String q2 = "delete from bb_daily_expenses where user_id=?";
            PreparedStatement ps2 = con.prepareStatement(q2);
            ps2.setInt(1, user.getId());
            ps2.executeUpdate();

            String q3 = "delete from bb_platform_expenses where user_id=?";
            PreparedStatement ps3 = con.prepareStatement(q3);
            ps3.setInt(1, user.getId());
            ps3.executeUpdate();

            ExpenseService expenseService = new ExpenseService();
            double fixedExpenses = expenseService.getTotalFixedExpenses(user);
            double newBalance = user.getSalary() - fixedExpenses;

            userService.updateBalance(user, newBalance);

            System.out.println("Monthly data reset successful!");
            System.out.println("Fixed expenses are kept. Deducted " + fixedExpenses + " from salary.");
            System.out.println("Balance restored: " + user.getBalance());

        } catch (Exception e) {
            System.out.println("Error resetting data!");
        }
    }
}