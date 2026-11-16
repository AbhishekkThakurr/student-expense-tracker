import java.time.LocalDate;

/**
 * One expense entry in the Student Expense Tracker.
 * Concepts: class, fields, constructor, getters, toString.
 * Each expense now carries the date it was added, so the
 * monthly summary report (Day 4) can group expenses by month.
 */
public class Expense {
    private final int id;
    private final String title;
    private final String category;
    private final double amount;
    private final LocalDate date;

    public Expense(int id, String title, String category, double amount, LocalDate date) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // New expenses are dated today.
    public Expense(int id, String title, String category, double amount) {
        this(id, title, category, amount, LocalDate.now());
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

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "#" + id + " | " + title + " | " + category + " | Rs. " + String.format("%.2f", amount)
                + " | " + date;
    }

    // One CSV line: id,title,category,amount,date (date in yyyy-MM-dd form)
    // Commas in the title are replaced with a space so a line
    // always splits into exactly five fields.
    public String toCsvLine() {
        return id + "," + title.replace(",", " ") + "," + category + "," + amount + "," + date;
    }

    // Builds an Expense from one CSV line, or returns null when
    // the line is blank or malformed, so one bad line never
    // stops the whole file from loading.
    // Lines saved before Day 4 have only four fields and no date;
    // those are read as today's date, since the old file did not
    // store one.
    public static Expense fromCsvLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = line.split(",", -1);
        if (parts.length != 4 && parts.length != 5) {
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0].trim());
            String title = parts[1].trim();
            String category = parts[2].trim();
            double amount = Double.parseDouble(parts[3].trim());
            if (parts.length == 5) {
                LocalDate date = LocalDate.parse(parts[4].trim());
                return new Expense(id, title, category, amount, date);
            }
            return new Expense(id, title, category, amount);
        } catch (Exception e) {
            return null;
        }
    }
}
