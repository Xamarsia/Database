//Курси валют "Currency rates" : ID, AccountsFK, int Accounts cost
public class CurrencyRates {
    private Accounts.Currency carrency;
    private Double moneyInUSD;

    public CurrencyRates(Accounts.Currency carrency, Double moneyInUSD) {
        this.setCarrency(carrency);
        this.setMoneyInUSD(moneyInUSD);
    }

    public Accounts.Currency getCarrency() {
        return carrency;
    }

    public void setCarrency(Accounts.Currency carrency) {
        this.carrency = carrency;
    }

    public Double getMoneyInUSD() {
        return moneyInUSD;
    }

    public void setMoneyInUSD(Double moneyInUSD) {
        this.moneyInUSD = moneyInUSD;
    }
}
