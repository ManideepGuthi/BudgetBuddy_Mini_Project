import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserService {
    Scanner sc = new Scanner(System.in);

    public void register() {
        try {
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            System.out.print("Enter salary: ");
            double salary = sc.nextDouble();
            sc.nextLine();

            if (salary <= 0) {
                System.out.println("Salary must be greater than 0!");
                return;
            }

            Connection con = DBConnection.getConnection();

            String checkQuery = "select * from bb_users where username=?";
            PreparedStatement checkPs = con.prepareStatement(checkQuery);
            checkPs.setString(1, username);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                System.out.println("Record already exists!");
                return;
            }

            String insertQuery = "insert into bb_users(username,password,salary,balance) values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(insertQuery);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setDouble(3, salary);
            ps.setDouble(4, salary);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Registration successful!");
            } else {
                System.out.println("Registration failed!");
            }

        } catch (Exception e) {
            System.out.println("Error in registration!");
        }
    }

    public User login() {
        User user = null;

        try {
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            Connection con = DBConnection.getConnection();

            String query = "select * from bb_users where username=? and password=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setSalary(rs.getDouble("salary"));
                user.setBalance(rs.getDouble("balance"));

                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid username or password!");
            }

        } catch (Exception e) {
            System.out.println("Error in login!");
        }

        return user;
    }

    public void updateBalance(User user, double newBalance) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "update bb_users set balance=? where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, newBalance);
            ps.setInt(2, user.getId());
            ps.executeUpdate();

            user.setBalance(newBalance);

        } catch (Exception e) {
            System.out.println("Balance update failed!");
        }
    }

    public void updateSalary(User user, double newSalary) {
        try {
            double diff = newSalary - user.getSalary();
            double newBalance = user.getBalance() + diff;

            Connection con = DBConnection.getConnection();

            String query = "update bb_users set salary=?, balance=? where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, newSalary);
            ps.setDouble(2, newBalance);
            ps.setInt(3, user.getId());
            ps.executeUpdate();

            user.setSalary(newSalary);
            user.setBalance(newBalance);

        } catch (Exception e) {
            System.out.println("Salary update failed!");
        }
    }
}