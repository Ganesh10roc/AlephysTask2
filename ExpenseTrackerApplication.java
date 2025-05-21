
import enums.TransactionType;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ExpenseTrackerApplication {

    private static final String filePath = "resources/file.txt";
    private final TransactionService service = new TransactionService();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new ExpenseTrackerApplication().run();
    }

    public void run() {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                service.loadFromFile(file);
                System.out.println("Loaded previous transactions from file.txt.");
            }
        } catch (Exception e) {
            System.out.println("Failed to load file.txt: " + e.getMessage());
        }

        System.out.println("=== Expense Tracker ===");

        while (true) {
            System.out.println("1. Add Transaction");
            System.out.println("2. Save All to the File");
            System.out.println("3. View Monthly Transactions");
            System.out.println("4. Load Transactions From the File");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1" -> addTransaction();
                case "2" -> saveAllToFile();
                case "3" -> showMonthlySummary();
                case "4" -> loadFile();
                case "5" -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void addTransaction() {
        try {
            System.out.print("Enter type (Income/Expense): ");
            TransactionType type = TransactionType.valueOf(scanner.nextLine().trim().toUpperCase());

            String[] subCategories = type.getSubCategories();
            System.out.println("Available categories:");
            for (String cat : subCategories) {
                System.out.println("- " + cat);
            }

            System.out.print("Enter category: ");
            String category = scanner.nextLine().trim().toUpperCase();

            boolean valid = false;
            for (String sub : subCategories) {
                if (sub.equalsIgnoreCase(category)) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.println("Invalid category.");
                return;
            }

            System.out.print("Enter amount: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());

            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine().trim());

            Transaction transaction = new Transaction(type, category, date, amount);
            service.addTransaction(transaction);

            System.out.println("Transaction added to memory!");
        } catch (Exception e) {
            System.out.println("Invalid input. Try again.");
        }
    }

    private void showMonthlySummary() {
        try {
            System.out.print("Enter year: ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter month (1-12): ");
            int month = Integer.parseInt(scanner.nextLine().trim());

            List<Transaction> transactions = service.getMonthlyTransactions(year, month);

            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }

            BigDecimal totalIncome = BigDecimal.ZERO;
            BigDecimal totalExpense = BigDecimal.ZERO;

            System.out.println("Date       | Type    | Category   | Amount");
            System.out.println("---------------------------------------------");

            for (Transaction t : transactions) {
                System.out.printf("%s | %-7s | %-10s | %s\n", t.getDate(), t.getType(), t.getCategory(), t.getAmount());
                if (t.getType() == TransactionType.INCOME) {
                    totalIncome = totalIncome.add(t.getAmount());
                } else {
                    totalExpense = totalExpense.add(t.getAmount());
                }
            }

            System.out.println("---------------------------------------------");
            System.out.println("Total Income : " + totalIncome);
            System.out.println("Total Expense: " + totalExpense);
            System.out.println("Balance      : " + totalIncome.subtract(totalExpense));

        } catch (Exception e) {
            System.out.println("Invalid input. Try again.");
        }
    }

    private void saveAllToFile() {
        System.out.print("For Reference file path is resources/file.txt ");
        System.out.print("Enter CSV file path: ");
        String path = scanner.nextLine().trim();

        if (!path.equals(filePath)) {
            System.out.println("No file name found");
            return;
        }

        File file = new File(path);
        service.saveAllToCsvFile(file);
    }

    private void loadFile() {
        try {
            System.out.print("For Reference file path is resources/file.txt ");
            System.out.print("Enter CSV file path: ");
            String path = scanner.nextLine().trim();
            File file = new File(path);

            if (!file.exists()) {
                System.out.println("File does not exist.");
                return;
            }

            service.loadFromFile(file);
            service.printTransactionTable();
            System.out.println("Transactions loaded from file.");

        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}
