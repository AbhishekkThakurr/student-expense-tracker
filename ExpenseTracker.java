import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Week 1 Project - Student Expense Tracker (version 3)
 * A console app to record daily expenses, view them, and see the total.
 * Concepts: ArrayList, loops, switch, methods, Scanner input,
 * file I/O (BufferedReader / BufferedWriter).
 *
 * Build plan for the week:
 * - Day 1: add expense, view all, total
 * - Day 2: categories with filter by category
 * - Day 3: save and load expenses from a CSV file (this version)
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

    // The file where expenses are saved between runs.
    private static final String FILE_NAME = "expenses.csv";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        loadExpenses();
        System.out.println("Welcome to Student Expense Tracker");

        while (running) {
            System.out.println("\n1. Add expense");
            System.out.println("2. View all expenses");
            System.out.println("3. Show total spent");
            System.out.println("4. Filter expenses by category");
            System.out.println("5. Save expenses to file");
            System.out.println("6. Exit");
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
                    saveExpenses();
                    break;
                case 6:
                    saveExpenses();
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 6.");
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
        saveExpenses();
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

    // Writes every expense as one line in expenses.csv:
    // id,title,category,amount
    // Commas inside a title are replaced with a space so the
    // file always has exactly four fields per line.
    private static void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense expense : expenses) {
                writer.write(expense.toCsvLine());
                writer.newLine();
            }
            System.out.println("Saved " + expenses.size() + " expense(s) to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Could not save expenses: " + e.getMessage());
        }
    }

    // Reads expenses.csv at startup (if it exists) so nothing is lost
    // between runs. nextId continues after the largest saved id, so
    // new expenses never reuse an old id.
    private static void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int loaded = 0;
            while ((line = reader.readLine()) != null) {
                Expense expense = Expense.fromCsvLine(line);
                if (expense != null) {
                    expenses.add(expense);
                    if (expense.getId() >= nextId) {
                        nextId = expense.getId() + 1;
                    }
                    loaded++;
                }
            }
            if (loaded > 0) {
                System.out.println("Loaded " + loaded + " expense(s) from " + FILE_NAME);
            }
        } catch (IOException e) {
            // First run: no file yet, so start with an empty list.
            System.out.println("No saved expenses found. Starting fresh.");
        }
    }
}
