import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        UserService userService = new UserService();
        ExpenseService expenseService = new ExpenseService();
        CartService cartService = new CartService();
        AnalysisService analysisService = new AnalysisService();
        SettingsService settingsService = new SettingsService();
        GoalService goalService = new GoalService();

        while (true) {
            System.out.println(
                    "  _______________________________________________________________________________________________________  ");
            System.out.println(
                    " |                                                                                                       | ");
            System.out.println(
                    " |   ██████╗ ██╗   ██╗██████╗  ██████╗ ███████╗████████╗    ██████╗ ██╗   ██╗██████╗ ██████╗ ██╗   ██╗   | ");
            System.out.println(
                    " |   ██╔══██╗██║   ██║██╔══██╗██╔════╝ ██╔════╝╚══██╔══╝    ██╔══██╗██║   ██║██║  ██╗██║  ██╗╚██╗ ██╔╝   | ");
            System.out.println(
                    " |   ██████╔╝██║   ██║██║  ██║██║  ███╗█████╗     ██║       ██████╔╝██║   ██║██║  ██║██║  ██║ ╚████╔╝    | ");
            System.out.println(
                    " |   ██╔══██╗██║   ██║██║  ██║██║   ██║██╔══╝     ██║       ██╔══██╗██║   ██║██║  ██║██║  ██║  ╚██╔╝     | ");
            System.out.println(
                    " |   ██████╔╝╚██████╔╝██████╔╝╚██████╔╝███████╗   ██║       ██████╔╝╚██████╔╝██████╔╝██████╔╝   ██║      | ");
            System.out.println(
                    " |   ╚═════╝  ╚═════╝ ╚═════╝  ╚═════╝ ╚══════╝   ╚═╝       ╚═════╝  ╚═════╝ ╚═════╝ ╚═════╝    ╚═╝      | ");
            System.out.println(
                    " |_______________________________________________________________________________________________________| ");
            System.out.println(
                    "                         >>>  THE SMART FINANCE COMPANION  <<<                                 ");
            System.out.println("1. Register  (New user)");
            System.out.println("2. Login     (Existing user)");
            System.out.println("3. Exit      (Close application)");
            System.out.print("\nPlease enter your choice (1-3): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    userService.register();
                    break;

                case 2:
                    User user = userService.login();

                    if (user != null) {
                        while (true) {
                            System.out.println("\n===== MAIN MENU =====");
                            System.out.println("What would you like to do today?");
                            System.out.println("1. Explore Platforms  (Shopping, Food, Ride)");
                            System.out.println("2. Add Expense        (Fixed or Daily)");
                            System.out.println("3. Expense Summary    (View Total Spendings)");
                            System.out.println("4. Smart Analysis     (Get Financial Insights)");
                            System.out.println("5. Settings           (Update Profile)");
                            System.out.println("6. Goal Tracking      (Set financial goals)");
                            System.out.println("7. Logout             (Sign out)");
                            System.out.print("\nPlease select an option (1-7): ");

                            int menuChoice = sc.nextInt();
                            sc.nextLine();

                            switch (menuChoice) {
                                case 1:
                                    cartService.showPlatforms(user);
                                    break;
                                case 2:
                                    expenseService.showExpenseMenu(user);
                                    break;
                                case 3:
                                    expenseService.showSummary(user, cartService);
                                    break;
                                case 4:
                                    analysisService.showAnalysis(user, expenseService, cartService);
                                    break;
                                case 5:
                                    settingsService.showSettings(user);
                                    break;
                                case 6:
                                    goalService.showGoalMenu(user);
                                    break;
                                case 7:
                                    System.out.println("Logout successful!");
                                    user = null;
                                    break;
                                default:
                                    System.out.println("Invalid choice!");
                            }

                            if (user == null) {
                                break;
                            }
                        }
                    }
                    break;

                case 3:
                    System.out.println("Thank you!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
