public class User {
    private String name;
    private int accountsFK;

    User(String name, int accountsFK) {
        this.setName(name);
        this.setAccountsFK(accountsFK);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountsFK() {
        return accountsFK;
    }

    public void setAccountsFK(int accountsFK) {
        this.accountsFK = accountsFK;
    }
}
