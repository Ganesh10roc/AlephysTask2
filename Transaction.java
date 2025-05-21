import enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private TransactionType type;
    private String category;
    private LocalDate date;
    private BigDecimal amount;

    public Transaction(TransactionType type, String category, LocalDate date, BigDecimal amount) {
        this.type = type;
        this.category = category;
        this.date = date;
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String toCSV() {
        return type + "," + category + "," + date + "," + amount;
    }
}
