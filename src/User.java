public class User {
    private int id;
    private String username;
    private String password;
    private double salary;
    private double balance;

    public User() {
    }

    public User(int id, String username, String password, double salary, double balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salary = salary;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}