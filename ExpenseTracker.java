import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Week 1 Project - Student Expense Tracker (version 7, final)
 * A console app to record daily expenses, view them, and see the total.
 * Concepts: ArrayList, loops, switch, methods, Scanner input,
 * file I/O (BufferedReader / BufferedWriter), dates (LocalDate,
 * YearMonth), grouping with a Map, input validation
 * (try/catch, NumberFormatException, loops that re-ask), and
 * searching / removing items from a list.
 *
 * Build plan for the week:
 * - Day 1: add expense, view all, total
 * - Day 2: categories with filter by category
 * - Day 3: save and load expenses from a CSV file
 * - Day 4: monthly summary report
 * - Day 5: input validation and a cleaner menu
 * - Day 6: search and delete an expense
 * - Day 7: final polish, sample data, full README (this version)
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
            showMenu();
            int choice = readInt(input, "Choose an option (1-9): ", 1, 9);

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
                    showMonthlySummary();
                    break;
                case 7:
                    searchExpenses(input);
                    break;
                case 8:
                    deleteExpense(input);
                    break;
                case 9:
                    saveExpenses();
                    running = false;
                    System.out.println("Goodbye!");
                    break;
            }
        }

        input.close();
    }

    // The main menu, printed with a header and a separator so it is
    // easy to read at a glance. (Day 5)
    private static void showMenu() {
        System.out.println();
        System.out.println("===== Student Expense Tracker =====");
        System.out.println("  1. Add expense");
        System.out.println("  2. View all expenses");
        System.out.println("  3. Show total spent");
        System.out.println("  4. Filter expenses by category");
        System.out.println("  5. Save expenses to file");
        System.out.println("  6. Monthly summary report");
        System.out.println("  7. Search expenses by title");
        System.out.println("  8. Delete an expense");
        System.out.println("  9. Exit");
        System.out.println("-----------------------------------");
    }

    // Reads a whole number between min and max. If the user types
    // letters, a decimal, or a number outside the range, the program
    // explains the problem and asks again instead of crashing.
    // (Day 5 - this replaces the old nextInt() calls, which crashed
    // with InputMismatchException on non-numeric input.)
    private static int readInt(Scanner input, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("That is not a whole number. Please try again.");
            }
        }
    }

    // Reads an amount in Rs. The amount must be a number greater
    // than zero: letters, zero, and negative amounts are all
    // rejected with a message, and the user is asked again. (Day 5)
    private static double readPositiveAmount(Scanner input) {
        while (true) {
            System.out.print("Amount in Rs.: ");
            String line = input.nextLine().trim();
            try {
                double amount = Double.parseDouble(line);
                if (Double.isNaN(amount) || Double.isInfinite(amount)) {
                    System.out.println("Please enter a real amount.");
                } else if (amount <= 0) {
                    System.out.println("Amount must be more than Rs. 0. Please try again.");
                } else {
                    return amount;
                }
            } catch (NumberFormatException e) {
                System.out.println("That is not a number. Please enter the amount in Rs.");
            }
        }
    }

    // Reads a line of text that must not be blank. A title of only
    // spaces is rejected and the user is asked again. (Day 5)
    private static String readNonEmptyLine(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("This cannot be empty. Please type something.");
            } else {
                return line;
            }
        }
    }

    private static void addExpense(Scanner input) {
        String title = readNonEmptyLine(input, "Title (for example, Lunch): ");

        // The user picks a category from the fixed list instead of typing
        // free text, so spelling mistakes cannot create duplicate categories.
        String category = pickCategory(input);

        double amount = readPositiveAmount(input);

        Expense expense = new Expense(nextId, title, category, amount);
        nextId++;
        expenses.add(expense);
        System.out.println("Added: " + expense);
        saveExpenses();
    }

    // Shows the numbered category list and returns the chosen category name.
    // An out-of-range or non-numeric choice is now re-asked instead of
    // silently falling back to 'Other'. (Day 5)
    private static String pickCategory(Scanner input) {
        System.out.println("Choose a category:");
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.println("  " + (i + 1) + ". " + CATEGORIES[i]);
        }
        int choice = readInt(input, "Category number: ", 1, CATEGORIES.length);
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
        // Day 7 polish: a count at the end, so the list size is
        // clear without counting the lines by hand.
        System.out.println("(" + expenses.size() + " expense" + (expenses.size() == 1 ? "" : "s") + ")");
    }

    private static void showTotal() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        // Day 7 polish: show the number of expenses next to the
        // total, so "Rs. 0.00" can never be mistaken for a bug
        // when the list is simply empty.
        System.out.printf("Total spent so far: Rs. %.2f (%d expense%s)%n",
                total, expenses.size(), expenses.size() == 1 ? "" : "s");
    }

    // Search expenses by title (Day 6). The match is case-insensitive
    // and looks for the keyword anywhere inside the title, so "lunch"
    // finds "Lunch with friends" as well as "Lunch". The search shows
    // every match plus how many were found.
    private static void searchExpenses(Scanner input) {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        String keyword = readNonEmptyLine(input, "Search for (part of a title): ").toLowerCase();
        int found = 0;

        System.out.println("\nMatching expenses:");
        for (Expense expense : expenses) {
            if (expense.getTitle().toLowerCase().contains(keyword)) {
                System.out.println(expense);
                found++;
            }
        }

        if (found == 0) {
            System.out.println("No expenses match '" + keyword + "'.");
        } else {
            System.out.println("Found " + found + " expense" + (found == 1 ? "" : "s") + ".");
        }
    }

    // Delete an expense by its id number (Day 6). The list is shown
    // first so the user can see the ids. After the user picks an id,
    // the expense is shown back and deleted only after a "y" confirm,
    // so a wrong number cannot silently wipe an entry. The file is
    // saved straight away, so the deleted expense stays gone.
    private static void deleteExpense(Scanner input) {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        viewExpenses();
        int id = readInt(input, "Enter the id of the expense to delete: ", 1, Integer.MAX_VALUE);

        Expense toDelete = null;
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                toDelete = expense;
                break;
            }
        }

        if (toDelete == null) {
            System.out.println("No expense with id #" + id + ". Nothing was deleted.");
            return;
        }

        System.out.println("You are about to delete: " + toDelete);
        System.out.print("Delete it? Type y to confirm, anything else to cancel: ");
        String answer = input.nextLine().trim().toLowerCase();

        if (!answer.equals("y") && !answer.equals("yes")) {
            System.out.println("Cancelled. Nothing was deleted.");
            return;
        }

        expenses.remove(toDelete);
        saveExpenses();
        System.out.println("Deleted expense #" + id + ".");
    }

    // Monthly summary report (Day 4): for the current month, shows
    // how much went to each category, the overall total, the average
    // expense, and the category where the most money went. Then a
    // short overview lists every month that has expenses, so the
    // user can compare months at a glance. A TreeMap keeps the
    // months in calendar order.
    private static void showMonthlySummary() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMMM yyyy");
        YearMonth currentMonth = YearMonth.now();

        // Totals for the current month, one entry per category.
        Map<String, Double> monthCategoryTotals = new TreeMap<>();
        Map<String, Integer> monthCategoryCounts = new TreeMap<>();
        double monthTotal = 0;
        int monthCount = 0;

        // Totals for every month, for the overview at the end.
        Map<YearMonth, Double> allMonthTotals = new TreeMap<>();
        Map<YearMonth, Integer> allMonthCounts = new TreeMap<>();

        for (Expense expense : expenses) {
            YearMonth expenseMonth = YearMonth.from(expense.getDate());
            allMonthTotals.merge(expenseMonth, expense.getAmount(), Double::sum);
            allMonthCounts.merge(expenseMonth, 1, Integer::sum);

            if (expenseMonth.equals(currentMonth)) {
                monthCategoryTotals.merge(expense.getCategory(), expense.getAmount(), Double::sum);
                monthCategoryCounts.merge(expense.getCategory(), 1, Integer::sum);
                monthTotal += expense.getAmount();
                monthCount++;
            }
        }

        System.out.println("\n--- Monthly Summary: " + currentMonth.format(monthFormat) + " ---");
        if (monthCount == 0) {
            System.out.println("No expenses recorded this month yet.");
        } else {
            String topCategory = null;
            double topTotal = 0;
            for (Map.Entry<String, Double> entry : monthCategoryTotals.entrySet()) {
                String category = entry.getKey();
                double categoryTotal = entry.getValue();
                int count = monthCategoryCounts.get(category);
                System.out.printf("%-15s Rs. %8.2f  (%d expense%s)%n",
                        category, categoryTotal, count, count == 1 ? "" : "s");
                if (categoryTotal > topTotal) {
                    topTotal = categoryTotal;
                    topCategory = category;
                }
            }
            System.out.println("----------------------------------------");
            System.out.printf("Total this month: Rs. %.2f (%d expense%s)%n",
                    monthTotal, monthCount, monthCount == 1 ? "" : "s");
            System.out.printf("Average expense:  Rs. %.2f%n", monthTotal / monthCount);
            System.out.println("Biggest spending: " + topCategory + " (Rs. " + String.format("%.2f", topTotal) + ")");
        }

        System.out.println("\n--- All months ---");
        for (Map.Entry<YearMonth, Double> entry : allMonthTotals.entrySet()) {
            YearMonth month = entry.getKey();
            int count = allMonthCounts.get(month);
            System.out.printf("%-15s Rs. %8.2f  (%d expense%s)%n",
                    month.format(monthFormat), entry.getValue(), count, count == 1 ? "" : "s");
        }
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
    // id,title,category,amount,date
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

