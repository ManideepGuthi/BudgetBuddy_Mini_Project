public abstract class Expense {
    private int id;
    private int userId;
    private double amount;

    public Expense() {}

    public Expense(int id, int userId, double amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    // Abstract method to be implemented by subclasses (Abstraction)
    public abstract void displayDetails();
}
