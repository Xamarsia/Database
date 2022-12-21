import java.sql.*;
import java.util.LinkedList;

public class Bank {
    private final String url = "jdbc:postgresql://localhost/Bank";
    private final String user = "postgres";
    private final String password = "*************";

    private static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public void createBankSystem() {
        final String queryAccounts = "CREATE TABLE Accounts " +
                "(accountPK SERIAL PRIMARY KEY, " +
                " money NUMERIC(9,2), " +
                "currency VARCHAR(10) CONSTRAINT allowed_currency_types " +
                " CHECK (currency IN ('USD', 'EUR', 'UAH')));";

        final String queryUsers = " CREATE TABLE users" +
                " ( userPK SERIAL PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL UNIQUE);";

        final String queryAlterAccounts = "ALTER TABLE Accounts " +
                " ADD COLUMN userFK INTEGER REFERENCES USERS(userPK) ON DELETE CASCADE;";

        final String queryCurrencyRates = "CREATE TABLE CurrencyRates " +
                "(currencyPK SERIAL PRIMARY KEY," +
                "currency VARCHAR(10) CONSTRAINT allowed_currency_types " +
                "CHECK (currency IN ('USD', 'EUR', 'UAH'))," +
                "moneyInUSD NUMERIC(9,2)," +
                "currencyUpdateDate DATE NOT NULL DEFAULT now());";

        final String queryTransaction =
                "CREATE TABLE Transaction" +
                        "(senderAccountsFK INTEGER REFERENCES accounts(accountPK) ON DELETE SET NULL," +
                        "recipientAccountsFK INTEGER REFERENCES accounts(accountPK) ON DELETE SET NULL," +
                        "transactionDate DATE NOT NULL DEFAULT now()," +
                        "isTransactionSuccessful BOOL NOT NULL, " +
                        "money NUMERIC(9,2)," +
                        "CONSTRAINT accounts_should_be_different CHECK(senderAccountsFK != recipientAccountsFK));";

        createTable(queryAccounts);
        createTable(queryUsers);
        createTable(queryAlterAccounts);
        createTable(queryCurrencyRates);
        createTable(queryTransaction);
    }

    public int insertTransaction(Transaction transaction) {
        String SQL = "INSERT INTO Transaction(senderAccountsFK, " +
                "recipientAccountsFK, money, isTransactionSuccessful) " +
                "VALUES(?, ?, ?, ?)";

        int id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, transaction.getSenderAccountsFK());
            pstmt.setInt(2, transaction.getRecipientAccountsFK());
            pstmt.setDouble(3, transaction.getMoney());
            pstmt.setBoolean(4, transaction.isTransactionSuccessful());

            id = getID(pstmt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(String.format("Transaction %s, %s, %s has been inserted with id %d successfully.",
                transaction.getSenderAccountsFK(), transaction.getRecipientAccountsFK(), transaction.getMoney(), id));
        return id;
    }

    public int insertUser(User user) {
        String SQL = "INSERT INTO users(name) " +
                "VALUES(?)";

        int id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getName());

            id = getID(pstmt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(String.format("User %s, has been inserted with id %d successfully.",
                user.getName(), id));
        return id;
    }

    public int insertCurrencyRates(CurrencyRates currencyRates) {
        String SQL = "INSERT INTO CurrencyRates(currency, moneyInUSD) " +
                "VALUES(?, ?)";

        int id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, currencyRates.getCarrency().name());
            pstmt.setDouble(2, currencyRates.getMoneyInUSD());

            id = getID(pstmt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(String.format("CurrencyRates %s, %s has been inserted with id %d successfully.",
                currencyRates.getCarrency().name(), currencyRates.getMoneyInUSD(), id));
        return id;
    }

    public int updateAccounts(int accountPK, int userFk) {
        String SQL = "UPDATE Accounts "
                + "SET userFK = ? "
                + "WHERE accountPK = ?";

        int affectedrows = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, userFk);
            pstmt.setInt(2, accountPK);

            affectedrows = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }

    public int insertAccountsMoneyAndCurrency(Accounts account) {
        String SQL = "INSERT INTO Accounts(money, currency) " +
                "VALUES(?, ?)";

        int id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setDouble(1, account.getMoney());
            pstmt.setString(2, account.getCurrency().name());

            id = getID(pstmt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(String.format("Account %s, %s,  has been inserted with id %d successfully.",
                account.getMoney(), account.getCurrency().name(), id));
        return id;
    }

    public int bankAccountReplenishment(int accountPK, Accounts.Currency currency, double money) {
        String SQL = "UPDATE Accounts " +
                " SET money = money + (SELECT moneyInUSD::float FROM CurrencyRates WHERE " +
                " (Accounts.currency = CurrencyRates.currency))  * " +
                " (SELECT (?::float/CurrencyRates.moneyInUSD::float) " +
                " FROM CurrencyRates WHERE CurrencyRates.currency = ?) " +
                " WHERE accountPK = ? ; ";

        int affectedrows = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setDouble(1, money);
            pstmt.setString(2, currency.toString());
            pstmt.setInt(3, accountPK);

            affectedrows = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }

    public void financialTransaction(int senderAccountsFK, int recipientAccountsFK, Double money) {
        double moneyFromAccount = 0.0;

        String SQL = "SELECT money, currency FROM Accounts WHERE Accounts.accountPK = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, senderAccountsFK);

            moneyFromAccount = getMoney(pstmt);
            Accounts.Currency senderCurrency = new Accounts().toCurrency(getCurrency(pstmt, 2));

            pstmt.setInt(1, recipientAccountsFK);
            Accounts.Currency recipientCurrency = new Accounts().toCurrency(getCurrency(pstmt, 2));

            if (moneyFromAccount < money) {
                this.insertTransaction(new Transaction(senderAccountsFK, recipientAccountsFK, money, false));
            } else {
                this.insertTransaction(new Transaction(senderAccountsFK, recipientAccountsFK, money, true));
                this.updateMoneyFromAccounts(senderAccountsFK, -money);

                Double convertedMoney = this.currencyConverter(senderCurrency, recipientCurrency, money);
                this.updateMoneyFromAccounts(recipientAccountsFK, convertedMoney);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void receivingTotalFundsOnUserUAHAccount(int userPK) {
        String SQL = "SELECT accountPK, money, currency FROM users " +
                "JOIN Accounts ON Accounts.userFK = users.userPK " +
                "WHERE userPK = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, userPK);

            LinkedList<Accounts> accounts = getAccounts(pstmt);
            if (accounts.size() <= 1) {
                return;
            }
            int UAHAccountPK = getAccountPKInUAH(accounts);
            if (UAHAccountPK < 0) {
                System.out.println("The user does not have accounts in UAH.");
                return;
            }
            for (Accounts account : accounts) {
                int accountPK = account.getAccountPK();
                double money = account.getMoney();
                if (accountPK != UAHAccountPK) {
                    financialTransaction(accountPK, UAHAccountPK, money);
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    private void createTable(String createTableSQLString) {
        System.out.println(createTableSQLString);

        try (Connection connection = connect();
             Statement statement = connection.createStatement();) {

            // Execute the query or update query
            statement.execute(createTableSQLString);
        } catch (SQLException e) {

            // print SQL exception information
            printSQLException(e);
        }
    }

    private int getID(PreparedStatement pstmt) throws SQLException {
        int id = 0;
        int affectedRows = pstmt.executeUpdate();
        // check the affected rows
        if (affectedRows > 0) {
            // get the ID back
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return id;
    }

    private String getCurrency(PreparedStatement pstmt, int column) throws SQLException {
        String currency = "";
        ResultSet rs;
        rs = pstmt.executeQuery();
        while (rs.next()) {
            currency = rs.getString(column);
        }
        rs.close();
        return currency;
    }

    private LinkedList<Accounts> getAccounts(PreparedStatement pstmt) throws SQLException {
        LinkedList<Accounts> accounts = new LinkedList<>();
        ResultSet rs;
        rs = pstmt.executeQuery();
        while (rs.next()) {
            int accountPK = rs.getInt(1);
            Double money = rs.getDouble(2);
            Accounts.Currency currency = new Accounts().toCurrency(rs.getString(3));

            accounts.addLast(new Accounts(accountPK, currency, money));
        }
        rs.close();
        return accounts;
    }

    private double getMoney(PreparedStatement pstmt) throws SQLException {
        double money = 0.0;
        ResultSet rs;
        rs = pstmt.executeQuery();
        while (rs.next()) {
            money = rs.getDouble(1);
        }
        rs.close();
        return money;
    }

    private double currencyConverter(Accounts.Currency currencyFrom, Accounts.Currency currencyTo, double money) {
        String SQL = "SELECT ? * (second.moneyInUSD::float / first.moneyInUSD::float) " +
                "FROM CurrencyRates first, CurrencyRates second " +
                "WHERE first.currency = ? AND second.currency = ? ";
        double convertedMoney = 0.0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setDouble(1, money);
            pstmt.setString(2, currencyFrom.toString());
            pstmt.setString(3, currencyTo.toString());

            convertedMoney = getMoney(pstmt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return convertedMoney;
    }

    private void updateMoneyFromAccounts(int accountPK, Double money) {
        String SQL = " UPDATE Accounts " +
                " SET money = money + ? " +
                " WHERE Accounts.accountPK = ? ";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setDouble(1, money);
            pstmt.setInt(2, accountPK);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private int getAccountPKInUAH(LinkedList<Accounts> accounts) {
        for (Accounts account : accounts) {
            if (account.getCurrency() == Accounts.Currency.UAH) {
                return account.getAccountPK();
            }
        }
        return -1;
    }

}
