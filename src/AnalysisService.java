public class AnalysisService {

    public void showAnalysis(User user, ExpenseService expenseService, CartService cartService) {
        double fixed = expenseService.getTotalFixedExpenses(user);
        double daily = expenseService.getTotalDailyExpenses(user);
        double platform = cartService.getTotalPlatformSpending(user);
        GoalService goalService = new GoalService();
        double goals = goalService.getTotalGoalSavings(user);
        double salary = user.getSalary();

        System.out.println("\n===== SMART ANALYSIS =====");
        System.out.println("Salary            : Rs. " + salary);
        System.out.println("Remaining Balance : Rs. " + user.getBalance());
        System.out.println("----------------------------------------");

        printBar("Fixed", fixed, salary);
        printBar("Daily", daily, salary);
        printBar("Platform", platform, salary);
        printBar("Goal Savings", goals, salary);

        double max = fixed;
        String highest = "Fixed Expenses";

        if (daily > max) {
            max = daily;
            highest = "Daily Expenses";
        }

        if (platform > max) {
            max = platform;
            highest = "Platform Spending";
        }

        if (goals > max) {
            max = goals;
            highest = "Goal Savings";
        }

        System.out.println("Highest Outflow Category: " + highest);

        double totalOutflow = fixed + daily + platform + goals;
        if (totalOutflow > salary * 0.8) {
            System.out.println("Suggestion: Your total outflow (expenses + savings) is high (>80% of salary).");
        } else {
            System.out.println("Suggestion: Good job. Your outflow is well-managed.");
        }
    }

    public void printBar(String name, double amount, double salary) {
        double percentage = 0;
        if (salary > 0) {
            percentage = (amount / salary) * 100;
        }

        int bars = (int) Math.round(percentage / 100.0 * 20); // 20 units for full width

        System.out.printf("%-10s: [", name);

        for (int i = 0; i < 20; i++) {
            if (i < bars) {
                System.out.print("█");
            } else {
                System.out.print("-");
            }
        }

        System.out.printf("] %5.1f%% (%.2f)%n", percentage, amount);
    }
}