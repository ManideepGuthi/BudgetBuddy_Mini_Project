public class DailyExpense extends Expense {
    private String category;

    public DailyExpense() {}

    public DailyExpense(int id, int userId, String category, double amount) {
        super(id, userId, amount);
        this.category = category;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public void displayDetails() {
        System.out.println("Daily Expense: " + category + " | Amount: Rs. " + getAmount());
    }
}
