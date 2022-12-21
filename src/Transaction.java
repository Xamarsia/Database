public class Transaction {
    private int senderAccountsFK;
    private int recipientAccountsFK;
    private Double money;

    private boolean isTransactionSuccessful;

    public Transaction(int senderAccountsFK, int recipientAccountsFK, Double money, boolean isTransactionSuccessful) {
        this.setSenderAccountsFK(senderAccountsFK);
        this.setRecipientAccountsFK(recipientAccountsFK);
        this.setMoney(money);
        this.setTransactionSuccessful(isTransactionSuccessful);
    }

    public int getSenderAccountsFK() {
        return senderAccountsFK;
    }

    public void setSenderAccountsFK(int senderAccountsFK) {
        this.senderAccountsFK = senderAccountsFK;
    }

    public int getRecipientAccountsFK() {
        return recipientAccountsFK;
    }

    public void setRecipientAccountsFK(int recipientAccountsFK) {
        this.recipientAccountsFK = recipientAccountsFK;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public boolean isTransactionSuccessful() {
        return isTransactionSuccessful;
    }

    public void setTransactionSuccessful(boolean transactionSuccessful) {
        isTransactionSuccessful = transactionSuccessful;
    }
}
