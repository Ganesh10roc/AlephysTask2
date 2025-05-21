

import enums.TransactionType;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class TransactionService {

    private final List<Transaction> transactionList = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    public List<Transaction> getMonthlyTransactions(int year, int month) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactionList) {
            if (t.getDate().getYear() == year && t.getDate().getMonthValue() == month) {
                result.add(t);
            }
        }
        return result;
    }

    public void saveAllToCsvFile(File file) {
        if (transactionList.isEmpty()) {
            System.out.println("No transactions to save.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            bw.write("Type,Category,Date,Amount");
            bw.newLine();


            for (Transaction t : transactionList) {
                bw.write(t.toCSV());
                bw.newLine();
            }

            System.out.println("All transactions saved to file successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }


    public void loadFromFile(File file) {
        transactionList.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                String[] parts = line.split(",");

                if (parts.length != 4) {
                    System.out.println("Invalid format on line " + lineNumber + ": " + line);
                    continue;
                }

                try {
                    TransactionType type = TransactionType.valueOf(parts[0].trim().toUpperCase());
                    String category = parts[1].trim();
                    LocalDate date = LocalDate.parse(parts[2].trim());
                    BigDecimal amount = new BigDecimal(parts[3].trim());

                    transactionList.add(new Transaction(type, category, date, amount));
                } catch (Exception ex) {
                    System.out.println("Error parsing line " + lineNumber + ": " + line);
                    System.out.println("Cause: " + ex.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }


    public void printTransactionTable() {
        if (transactionList.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }


        System.out.printf("%-15s %-20s %-15s %-15s%n", "TYPE", "CATEGORY", "DATE", "AMOUNT");
        System.out.println("--------------------------------------------------------------------------");


        for (Transaction t : transactionList) {
            System.out.printf("%-15s %-20s %-15s %-15s%n",
                    t.getType(),
                    t.getCategory(),
                    t.getDate(),
                    t.getAmount());
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Total Transactions: " + transactionList.size());
    }



}