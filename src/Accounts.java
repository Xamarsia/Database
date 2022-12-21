public class Accounts {
    private int accountPK;
    private Currency currency;
    private Double money;
    private int userFk;
    public Accounts(int accountPK, Currency currency, Double money) {
        this.setCurrency(currency);
        this.setMoney(money);
        this.setAccountPK(accountPK);
    }

    public Accounts(Currency currency, Double money, int userFk) {
        this.setCurrency(currency);
        this.setMoney(money);
        this.setUserFk(userFk);
    }

    public Accounts(Currency currency, Double money) {
        this.setCurrency(currency);
        this.setMoney(money);
    }

    public Accounts() {}

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public int getUserFk() {
        return userFk;
    }

    public void setUserFk(int userFk) {
        this.userFk = userFk;
    }

    public int getAccountPK() {
        return accountPK;
    }

    public void setAccountPK(int accountPK) {
        this.accountPK = accountPK;
    }

    public Currency toCurrency(String currency) {
        Currency result = null;

        if (Currency.EUR.toString().equals(currency)) {
            result = Currency.EUR;
        } else if (Currency.UAH.toString().equals(currency)) {
            result = Currency.UAH;
        } else if (Currency.USD.toString().equals(currency)) {
            result = Currency.USD;
        }
        return result;
    }

    public enum Currency {
        USD,
        EUR,
        UAH
    }
}
