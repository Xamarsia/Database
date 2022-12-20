import java.sql.*;

public class Bank {
    private final String url = "jdbc:postgresql://localhost/Bank";
    private final String user = "postgres";
    private final String password = "*************";

    public static void printSQLException(SQLException ex) {
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

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void createTable(String createTableSQLString) {
        System.out.println(createTableSQLString);

        try (
                Connection connection = connect();
                Statement statement = connection.createStatement();) {

            // Execute the query or update query
            statement.execute(createTableSQLString);
        } catch (SQLException e) {

            // print SQL exception information
            printSQLException(e);
        }
    }

    public void createBankSystem() {
        final String queryAccounts = "CREATE TABLE Accounts " +
                "(accountPK SERIAL PRIMARY KEY, " +
                " money NUMERIC(9,2), " +
                "currency VARCHAR(10) UNIQUE CONSTRAINT allowed_currency_types " +
                " CHECK (currency IN ('USD', 'EUR', 'UAH')));";

        final String queryUsers = " CREATE TABLE users" +
                " ( userPK SERIAL PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "accountFK INTEGER REFERENCES accounts(accountPK) ON DELETE CASCADE," +
                "CONSTRAINT account_for_user_unique UNIQUE (userPK, accountFK));";

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
                        "currencyRate NUMERIC(9,2)," +
                        "transactionDate DATE NOT NULL DEFAULT now()," +
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
                "recipientAccountsFK, money) " +
                "VALUES(?, ?, ?)";

        int id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, transaction.getSenderAccountsFK());
            pstmt.setInt(2, transaction.getRecipientAccountsFK());
            pstmt.setDouble(3, transaction.getMoney());

            id = getID(pstmt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(String.format("Transaction %s, %s, %s has been inserted with id %d successfully.",
                transaction.getSenderAccountsFK(), transaction.getRecipientAccountsFK(), transaction.getMoney(), id));
        return id;
    }

    public int insertUser(User user) {
        String SQL = "INSERT INTO users(name, accountFK) " +
                "VALUES(?, ?)";

        int id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getName());
            pstmt.setInt(2, user.getAccountsFK());

            id = getID(pstmt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(String.format("User %s, %s has been inserted with id %d successfully.",
                user.getName(), user.getAccountsFK(), id));
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
}
