import java.util.ArrayList;
import java.util.Scanner;

/**
 * Week 1 Project - Student Expense Tracker (version 1)
 * A console app to record daily expenses, view them, and see the total.
 * Concepts: ArrayList, loops, switch, methods, Scanner input.
 *
 * Build plan for the week:
 * - Day 1: add expense, view all, total (this version)
 * - Day 2: categories with filter by category
 * - Day 3: save and load expenses from a CSV file
 * - Day 4: monthly summary report
 * - Day 5: input validation and a cleaner menu
 * - Day 6: search and delete an expense
 * - Day 7: final polish, sample data, full README
 */
public class ExpenseTracker {

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
            System.out.println("4. Exit");
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
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 4.");
            }
        }

        input.close();
    }

    private static void addExpense(Scanner input) {
        System.out.print("Title (for example, Lunch): ");
        String title = input.nextLine();

        System.out.print("Category (for example, Food): ");
        String category = input.nextLine();

        System.out.print("Amount in Rs.: ");
        double amount = input.nextDouble();
        input.nextLine();

        Expense expense = new Expense(nextId, title, category, amount);
        nextId++;
        expenses.add(expense);
        System.out.println("Added: " + expense);
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
}
