import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CartService {
    Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();

    public void showPlatforms(User user) {
        while (true) {
            System.out.println("\n===== PLATFORMS =====");
            System.out.println("1. Shopping");
            System.out.println("2. Food");
            System.out.println("3. Ride");
            System.out.println("4. Manage Cart");
            System.out.println("5. Back");
            System.out.print("\nSelect a platform to explore products (1-5): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    showShoppingItems(user);
                    break;
                case 2:
                    showFoodItems(user);
                    break;
                case 3:
                    showRideItems(user);
                    break;
                case 4:
                    cartMenu(user);
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    return;
                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 5.");
            }
        }
    }

    private void showShoppingItems(User user) {
        System.out.println("\n--- Shopping Products ---");
        System.out.println("1. T-Shirt  - 800");
        System.out.println("2. Jeans    - 1500");
        System.out.println("3. Sneakers - 2500");
        System.out.println("4. Back");
        System.out.print("\nSelect a product to buy (1-4): ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1: addToCart(user, "Shopping", "T-Shirt", 800); break;
            case 2: addToCart(user, "Shopping", "Jeans", 1500); break;
            case 3: addToCart(user, "Shopping", "Sneakers", 2500); break;
            case 4: return;
            default: System.out.println("Invalid selection!");
        }
    }

    private void showFoodItems(User user) {
        System.out.println("\n--- Food Items ---");
        System.out.println("1. Burger   - 200");
        System.out.println("2. Pizza    - 400");
        System.out.println("3. Pasta    - 300");
        System.out.println("4. Back");
        System.out.print("\nSelect an item to buy (1-4): ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1: addToCart(user, "Food", "Burger", 200); break;
            case 2: addToCart(user, "Food", "Pizza", 400); break;
            case 3: addToCart(user, "Food", "Pasta", 300); break;
            case 4: return;
            default: System.out.println("Invalid selection!");
        }
    }

    private void showRideItems(User user) {
        System.out.println("\n--- Ride Services ---");
        System.out.print("Enter start destination: ");
        String start = sc.nextLine();
        System.out.print("Enter end destination: ");
        String end = sc.nextLine();

        System.out.println("\nAvailable Ride Types:");
        System.out.println("1. Auto Ride   - 100");
        System.out.println("2. Mini Cab    - 300");
        System.out.println("3. Sedan Cab   - 500");
        System.out.println("4. Back");
        System.out.print("\nSelect a ride to book (1-4): ");
        int choice = sc.nextInt();
        sc.nextLine();

        String rideDetails = " (" + start + " to " + end + ")";

        switch (choice) {
            case 1: addToCart(user, "Ride", "Auto Ride" + rideDetails, 100); break;
            case 2: addToCart(user, "Ride", "Mini Cab" + rideDetails, 300); break;
            case 3: addToCart(user, "Ride", "Sedan Cab" + rideDetails, 500); break;
            case 4: return;
            default: System.out.println("Invalid selection!");
        }
    }

    public void addToCart(User user, String platform, String itemName, double amount) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "insert into bb_cart(user_id,platform,item_name,amount) values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ps.setString(2, platform);
            ps.setString(3, itemName);
            ps.setDouble(4, amount);

            ps.executeUpdate();

            System.out.println(itemName + " added to cart!");

        } catch (Exception e) {
            System.out.println("Error adding item to cart!");
        }
    }

    public void viewCart(User user) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "select * from bb_cart where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== CART ITEMS =====");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        rs.getInt("id") + " - " +
                                rs.getString("platform") + " - " +
                                rs.getString("item_name") + " - " +
                                rs.getDouble("amount")
                );
            }

            if (!found) {
                System.out.println("Cart is empty!");
            }

            System.out.println("Cart Total: " + getCartTotal(user));
            System.out.println("Current Balance: " + user.getBalance());

        } catch (Exception e) {
            System.out.println("Error viewing cart!");
        }
    }

    public double getCartTotal(User user) {
        double total = 0;

        try {
            Connection con = DBConnection.getConnection();

            String query = "select ifnull(sum(amount),0) as total from bb_cart where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (Exception e) {
            System.out.println("Error getting cart total!");
        }

        return total;
    }

    public void removeFromCart(User user) {
        try {
            viewCart(user);

            System.out.print("Enter cart item id to remove: ");
            int id = sc.nextInt();
            sc.nextLine();

            Connection con = DBConnection.getConnection();

            String query = "delete from bb_cart where id=? and user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2, user.getId());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Item removed!");
            } else {
                System.out.println("Item not found!");
            }

        } catch (Exception e) {
            System.out.println("Error removing item!");
        }
    }

    public void checkout(User user) {
        try {
            double total = getCartTotal(user);

            if (total == 0) {
                System.out.println("Cart is empty!");
                return;
            }

            if (total > user.getBalance()) {
                System.out.println("Cart amount exceeds balance! Returning to main menu.");
                return;
            }

            saveCartToPlatformExpenses(user);

            double newBalance = user.getBalance() - total;
            userService.updateBalance(user, newBalance);

            clearCart(user);

            System.out.println("Checkout successful!");
            System.out.println("Cart spending: " + total);
            System.out.println("Remaining balance: " + user.getBalance());

        } catch (Exception e) {
            System.out.println("Error in checkout!");
        }
    }

    public void clearCart(User user) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "delete from bb_cart where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error clearing cart!");
        }
    }

    public void saveCartToPlatformExpenses(User user) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "insert into bb_platform_expenses(user_id,platform,item_name,amount) " +
                    "select user_id,platform,item_name,amount from bb_cart where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error saving cart history!");
        }
    }

    public double getTotalPlatformSpending(User user) {
        double total = 0;

        try {
            Connection con = DBConnection.getConnection();

            String query = "select ifnull(sum(amount),0) as total from bb_platform_expenses where user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (Exception e) {
            System.out.println("Error getting platform spending!");
        }

        return total;
    }

    public void cartMenu(User user) {
        while (true) {
            System.out.println("\n===== CART MENU =====");
            System.out.println("1. View Cart");
            System.out.println("2. Remove Item");
            System.out.println("3. Checkout");
            System.out.println("4. Back");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewCart(user);
                    break;
                case 2:
                    removeFromCart(user);
                    break;
                case 3:
                    checkout(user);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}