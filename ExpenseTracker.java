import java.util.ArrayList;
import java.util.Scanner;

/**
 * Week 1 Project - Student Expense Tracker (version 2)
 * A console app to record daily expenses, view them, and see the total.
 * Concepts: ArrayList, loops, switch, methods, Scanner input.
 *
 * Build plan for the week:
 * - Day 1: add expense, view all, total
 * - Day 2: categories with filter by category (this version)
 * - Day 3: save and load expenses from a CSV file
 * - Day 4: monthly summary report
 * - Day 5: input validation and a cleaner menu
 * - Day 6: search and delete an expense
 * - Day 7: final polish, sample data, full README
 */
public class ExpenseTracker {

    // Fixed list of categories, so every expense belongs to one of them
    // and filtering by category always works.
    private static final String[] CATEGORIES = {
        "Food", "Travel", "Recharge", "Stationery", "Hostel", "Entertainment", "Other"
    };

    private static final ArrayList<Expense> expenses = new ArrayList<>();
    private static int nextId = 1;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to Student Expense Tracker");

        while (running) {
            System.out.println("\n1. Add expense");
            System.out.println("2. View all expenses");
            System.out.println("3. Show total spent");
            System.out.println("4. Filter expenses by category");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    addExpense(input);
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    showTotal();
                    break;
                case 4:
                    filterByCategory(input);
                    break;
                case 5:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 5.");
            }
        }

        input.close();
    }

    private static void addExpense(Scanner input) {
        System.out.print("Title (for example, Lunch): ");
        String title = input.nextLine();

        // The user picks a category from the fixed list instead of typing
        // free text, so spelling mistakes cannot create duplicate categories.
        String category = pickCategory(input);

        System.out.print("Amount in Rs.: ");
        double amount = input.nextDouble();
        input.nextLine();

        Expense expense = new Expense(nextId, title, category, amount);
        nextId++;
        expenses.add(expense);
        System.out.println("Added: " + expense);
    }

    // Shows the numbered category list and returns the chosen category name.
    private static String pickCategory(Scanner input) {
        System.out.println("Choose a category:");
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.println((i + 1) + ". " + CATEGORIES[i]);
        }
        System.out.print("Category number: ");
        int choice = input.nextInt();
        input.nextLine();

        if (choice < 1 || choice > CATEGORIES.length) {
            System.out.println("Invalid choice, using 'Other'.");
            return "Other";
        }
        return CATEGORIES[choice - 1];
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        System.out.println("\nYour expenses:");
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

    private static void showTotal() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        System.out.printf("Total spent so far: Rs. %.2f%n", total);
    }

    // Shows only the expenses in one category, plus their total,
    // so the user can see where the money is going.
    private static void filterByCategory(Scanner input) {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        String category = pickCategory(input);
        double categoryTotal = 0;
        int found = 0;

        System.out.println("\nExpenses in " + category + ":");
        for (Expense expense : expenses) {
            if (expense.getCategory().equals(category)) {
                System.out.println(expense);
                categoryTotal += expense.getAmount();
                found++;
            }
        }

        if (found == 0) {
            System.out.println("No expenses in this category yet.");
        } else {
            System.out.printf("Total in %s: Rs. %.2f (%d expense%s)%n",
                    category, categoryTotal, found, found == 1 ? "" : "s");
        }
    }
}
