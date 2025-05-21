package enums;
public enum TransactionType {
    INCOME(new String[]{"SALARY", "BUSINESS", "INTEREST", "BONUS", "OTHER"}),
    EXPENSE(new String[]{"FOOD", "RENT", "TRAVEL", "UTILITIES", "ENTERTAINMENT", "OTHER"});

    private final String[] subCategories;

    TransactionType(String[] subCategories) {
        this.subCategories = subCategories;
    }

    public String[] getSubCategories() {
        return subCategories;
    }
}

