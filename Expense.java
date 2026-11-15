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

    // One CSV line: id,title,category,amount
    // Commas in the title are replaced with a space so a line
    // always splits into exactly four fields.
    public String toCsvLine() {
        return id + "," + title.replace(",", " ") + "," + category + "," + amount;
    }

    // Builds an Expense from one CSV line, or returns null when
    // the line is blank or malformed, so one bad line never
    // stops the whole file from loading.
    public static Expense fromCsvLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = line.split(",", -1);
        if (parts.length != 4) {
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0].trim());
            String title = parts[1].trim();
            String category = parts[2].trim();
            double amount = Double.parseDouble(parts[3].trim());
            return new Expense(id, title, category, amount);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
