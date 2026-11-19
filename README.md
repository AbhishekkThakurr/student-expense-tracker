# Student Expense Tracker

A console-based Java app for tracking daily student expenses — hostel food, travel, recharge, stationery, and everything in between.

**Status: Week 1 project, complete (Day 7 of 7 done).**

## Features
- Add an expense with a title, category, and amount
- View all recorded expenses
- See the total spent so far
- Fixed categories (Food, Travel, Recharge, Stationery, Hostel, Entertainment, Other) picked from a list, so no spelling mistakes create duplicate categories
- Filter expenses by category and see the total for that category
- Expenses are saved to `expenses.csv` (after every add, on demand, and on exit) and loaded again when the app starts, so nothing is lost between runs
- Every expense carries the date it was added, and the monthly summary report shows this month's spending per category, the total, the average expense, the biggest spending category, plus an overview of every month
- Input validation: typing letters where a number is expected no longer crashes the app — the menu choice, category number, and amount are all re-asked with a clear message; a blank title is rejected, the amount must be more than Rs. 0, and a wrong category number is re-asked instead of silently becoming 'Other'
- Cleaner menu with a header and separator, so the options are easy to read at a glance
- Search expenses by part of the title (case-insensitive, so "lunch" finds "Lunch with friends")
- Delete an expense by its id number, with a confirmation prompt first, and the file is saved straight away so the deleted expense stays gone
- View and total screens show the number of expenses, so an empty list is never mistaken for a bug

## Build plan for the week
| Day | Work |
| --- | ----- |
| 1 | Project setup, Expense model, add / view / total ✅ |
| 2 | Categories with filter by category ✅ |
| 3 | Save and load expenses from a CSV file ✅ |
| 4 | Monthly summary report ✅ |
| 5 | Input validation and a cleaner menu ✅ |
| 6 | Search and delete an expense ✅ |
| 7 | Final polish, sample data, complete README ✅ |

## Concepts used
Classes and objects, ArrayList, loops, switch, methods, Scanner input, file I/O (BufferedReader / BufferedWriter), dates (LocalDate, YearMonth), grouping with a Map, input validation (try/catch, NumberFormatException, re-asking loops), and searching / removing items from a list.

## How to run
```bash
javac Expense.java ExpenseTracker.java
java ExpenseTracker
```

Expenses are stored in `expenses.csv` next to the program — one line per expense (`id,title,category,amount,date`). Files saved before Day 4 have no date column and still load fine (their date is read as today).

## Sample data
`expenses.csv` ships with 12 sample expenses: 10 from November 2026 and 2 from October 2026, so the monthly summary shows this month's breakdown plus an overview of both months. Run the app and try options 2 (view), 3 (total), and 6 (monthly summary) to see it working right away. Adding your own expense saves over the sample file, and deleting the file starts you fresh.
