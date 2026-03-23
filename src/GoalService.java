import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class GoalService {
    Scanner sc = new Scanner(System.in);

    public void showGoalMenu(User user) {
        while (true) {
            System.out.println("\n===== GOAL TRACKING =====");
            System.out.println("1. Set New Goal");
            System.out.println("2. View Goal Progress");
            System.out.println("3. Add Savings to Goal");
            System.out.println("4. Delete Goal");
            System.out.println("5. Back");
            System.out.print("\nPlease select an option (1-5): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    setGoal(user);
                    break;
                case 2:
                    viewGoalProgress(user);
                    break;
                case 3:
                    addSavings(user);
                    break;
                case 4:
                    deleteGoal(user);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void setGoal(User user) {
        try {
            System.out.print("Enter product name you want to save for: ");
            String productName = sc.nextLine();

            System.out.print("Enter target price: ");
            double targetPrice = sc.nextDouble();
            sc.nextLine();

            if (targetPrice <= 0) {
                System.out.println("Price must be greater than 0!");
                return;
            }

            Connection con = DBConnection.getConnection();

            String query = "insert into bb_goals(user_id,product_name,target_price,current_amount) values(?,?,?,0)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ps.setString(2, productName);
            ps.setDouble(3, targetPrice);

            ps.executeUpdate();
            System.out.println("Goal set successfully for " + productName + "!");

        } catch (Exception e) {
            System.out.println("Error setting goal!");
        }
    }

    private void viewGoalProgress(User user) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "select * from bb_goals where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());

            ResultSet rs = ps.executeQuery();
            boolean found = false;

            System.out.println("\n===== YOUR GOALS =====");
            while (rs.next()) {
                found = true;
                String productName = rs.getString("product_name");
                double target = rs.getDouble("target_price");
                double current = rs.getDouble("current_amount");
                double remaining = target - current;
                double percentage = (current / target) * 100;

                System.out.println("\nProduct: " + productName);
                System.out.println("Target Price  : " + target);
                System.out.println("Current Saved : " + current);
                System.out.println("Remaining     : " + (remaining > 0 ? remaining : 0));
                System.out.printf("Progress      : [%.1f%%]%n", percentage);

                if (current >= target) {
                    System.out.println("🎉 Congratulations! You have reached your goal!");
                }
            }

            if (!found) {
                System.out.println("No goals set yet.");
            }

        } catch (Exception e) {
            System.out.println("Error viewing goals!");
        }
    }

    private void addSavings(User user) {
        try {
            viewGoalProgress(user);
            System.out.print("\nEnter exact product name to add savings to: ");
            String productName = sc.nextLine();

            System.out.print("Enter amount to add: ");
            double amount = sc.nextDouble();
            sc.nextLine();

            if (amount <= 0) {
                System.out.println("Amount must be greater than 0!");
                return;
            }

            Connection con = DBConnection.getConnection();

            String query = "update bb_goals set current_amount = current_amount + ? where user_id=? and product_name=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, amount);
            ps.setInt(2, user.getId());
            ps.setString(3, productName);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Savings added successfully!");
            } else {
                System.out.println("Product not found!");
            }

        } catch (Exception e) {
            System.out.println("Error adding savings!");
        }
    }

    private void deleteGoal(User user) {
        try {
            System.out.print("Enter product name of the goal to delete: ");
            String productName = sc.nextLine();

            Connection con = DBConnection.getConnection();

            String query = "delete from bb_goals where user_id=? and product_name=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ps.setString(2, productName);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Goal deleted!");
            } else {
                System.out.println("Goal not found!");
            }
        } catch (Exception e) {
            System.out.println("Error deleting goal!");
        }
    }
    public double getTotalGoalSavings(User user) {
        double total = 0;
        try {
            Connection con = DBConnection.getConnection();
            String query = "select ifnull(sum(current_amount),0) as total from bb_goals where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (Exception e) {
            System.out.println("Error getting total goal savings!");
        }
        return total;
    }
}
