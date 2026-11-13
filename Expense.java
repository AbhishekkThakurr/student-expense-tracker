/**
 * One expense entry in the Student Expense Tracker.
 * Concepts: class, fields, constructor, getters, toString.
 */
public class Expense {
    private final int id;
    private final String title;
    private final String category;
    private final double amount;

    public Expense(int id, String title, String category, double amount) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "#" + id + " | " + title + " | " + category + " | Rs. " + String.format("%.2f", amount);
    }
}
