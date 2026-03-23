import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        Connection con = null;

        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/budgetbuddy_simple",
                    "root",
                    "manu@2005"
            );
        } catch (Exception e) {
            System.out.println("Database connection failed!");
        }

        return con;
    }
}