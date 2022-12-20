//Transactions: ID, AccountsFK from, AccountsFK to, money, data
public class Transaction {
    private int senderAccountsFK;
    private int recipientAccountsFK;
    private Double money;



    public Transaction(int senderAccountsFK, int recipientAccountsFK, Double money) {
        this.setSenderAccountsFK(senderAccountsFK);
        this.setRecipientAccountsFK(recipientAccountsFK);
        this.setMoney(money);
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
}
