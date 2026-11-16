# Student Expense Tracker

A console-based Java app for tracking daily student expenses — hostel food, travel, recharge, stationery, and everything in between.

**Status: Week 1 project, in progress (Day 4 of 7 done).**

## Features so far
- Add an expense with a title, category, and amount
- View all recorded expenses
- See the total spent so far
- Fixed categories (Food, Travel, Recharge, Stationery, Hostel, Entertainment, Other) picked from a list, so no spelling mistakes create duplicate categories
- Filter expenses by category and see the total for that category
- Expenses are saved to `expenses.csv` (after every add, on demand, and on exit) and loaded again when the app starts, so nothing is lost between runs
- Every expense carries the date it was added, and the monthly summary report shows this month's spending per category, the total, the average expense, the biggest spending category, plus an overview of every month

## Build plan for the week
| Day | Work |
| --- | ---- |
| 1 | Project setup, Expense model, add / view / total |
| 2 | Categories with filter by category |
| 3 | Save and load expenses from a CSV file |
| 4 | Monthly summary report ✅ |
| 5 | Input validation and a cleaner menu |
| 6 | Search and delete an expense |
| 7 | Final polish, sample data, complete README |

## Concepts used
Classes and objects, ArrayList, loops, switch, methods, Scanner input, file I/O (BufferedReader / BufferedWriter), dates (LocalDate, YearMonth) and grouping with a Map.

## How to run
```bash
javac Expense.java ExpenseTracker.java
java ExpenseTracker
```

Expenses are stored in `expenses.csv` next to the program — one line per expense (`id,title,category,amount,date`). Files saved before Day 4 have no date column and still load fine (their date is read as today).
