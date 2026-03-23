public class FixedExpense extends Expense {
    private String title;

    public FixedExpense() {}

    public FixedExpense(int id, int userId, String title, double amount) {
        super(id, userId, amount);
        this.title = title;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @Override
    public void displayDetails() {
        System.out.println("Fixed Expense: " + title + " | Amount: Rs. " + getAmount());
    }
}
