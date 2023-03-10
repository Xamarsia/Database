public class Main {

    static public void setMenuData(Restaurant restaurant) {
        restaurant.insertDish(new Dish("Fish curry", 125, 200, 0));
        restaurant.insertDish(new Dish("Sushi", 345, 100, 10));
        restaurant.insertDish(new Dish("Ramen", 225, 80, 5));
        restaurant.insertDish(new Dish("Tom Yam Goong", 125, 130, 20));
        restaurant.insertDish(new Dish("Kebab", 50, 150, 0));
    }

    static public void setBankData(Bank bank) {
        bank.insertCurrencyRates(new CurrencyRates(Accounts.Currency.EUR, 0.942732));
        bank.insertCurrencyRates(new CurrencyRates(Accounts.Currency.USD, 1.0));
        bank.insertCurrencyRates(new CurrencyRates(Accounts.Currency.UAH, 36.94));

        int id = bank.insertAccountsMoneyAndCurrency(new Accounts(Accounts.Currency.EUR, 300.8));
        int id2 = bank.insertAccountsMoneyAndCurrency(new Accounts(Accounts.Currency.UAH, 105.34));
        int id3 = bank.insertAccountsMoneyAndCurrency(new Accounts(Accounts.Currency.USD, 220.7));
        int id4 = bank.insertAccountsMoneyAndCurrency(new Accounts(Accounts.Currency.EUR, 37.6));
        int id5 = bank.insertAccountsMoneyAndCurrency(new Accounts(Accounts.Currency.UAH, 15.34));
        int id6 = bank.insertAccountsMoneyAndCurrency(new Accounts(Accounts.Currency.USD, 22.72));

        int userTom = bank.insertUser(new User("Tom"));
        int userJohn = bank.insertUser(new User("John"));
        int userOla = bank.insertUser(new User("Ola"));

        bank.updateAccounts(id, userTom);
        bank.updateAccounts(id2, userJohn);
        bank.updateAccounts(id3, userOla);
        bank.updateAccounts(id4, userJohn);
        bank.updateAccounts(id5, userOla);
        bank.updateAccounts(id6, userJohn);

        bank.insertTransaction(new Transaction(id, id2, 56.9, true));
        bank.insertTransaction(new Transaction(id2, id3, 500.0, false));
        bank.insertTransaction(new Transaction(id3, id, 25.6, false));
    }

    static public void task1Solution() {
        Restaurant restaurant = new Restaurant();
        restaurant.createTable();
        setMenuData(restaurant);
        restaurant.findDishesByCost(60, 300);
        restaurant.findDishesWithDiscount();
    }

    static public void task2Solution() {
        Bank bank = new Bank();
        bank.createBankSystem();
        setBankData(bank);

        bank.bankAccountReplenishment(3, Accounts.Currency.UAH, 300);
        bank.financialTransaction(3, 4, 10.0);
        bank.receivingTotalFundsOnUserUAHAccount(2);
    }

    public static void main(String[] args) {
        task1Solution();
        task2Solution();
    }
}