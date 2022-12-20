public class Accounts {

    private Currency currency;
    private Double money;
    private int userFk;

    public Accounts(Currency currency, Double money, int userFk) {
        this.setCurrency(currency);
        this.setMoney(money);
        this.setUserFk(userFk);
    }

    public Accounts(Currency currency, Double money) {
        this.setCurrency(currency);
        this.setMoney(money);
    }

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

    public enum Currency {
        USD,
        EUR,
        UAH
    }
}
