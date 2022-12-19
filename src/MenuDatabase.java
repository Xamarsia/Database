import java.sql.*;

public class MenuDatabase {
    final String query = "CREATE TABLE restaurantmenu " +
            "(dishPK SERIAL PRIMARY KEY, " +
            "dishName VARCHAR(50) NOT NULL UNIQUE, " +
            "cost Float NOT NULL, " +
            "weight Float NOT NULL, " +
            "discountPercent Integer CHECK(discountPercent BETWEEN 0 AND 100) DEFAULT 0);";
    private final String url = "jdbc:postgresql://localhost/Menu";
    private final String user = "postgres";
    //    Restaurant menu
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

    public void createTable() {

        System.out.println(query);
        // Step 1: Establishing a Connection
        try (
                Connection connection = connect();

                // Step 2:Create a statement using connection object
                Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query
            statement.execute(query);
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

    public int insertDish(Dish dish) {
        String SQL = "INSERT INTO restaurantmenu(dishName, cost, weight, discountPercent) "
                + "VALUES(?, ?, ?, ?)";

        int id = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, dish.getDishName());
            pstmt.setFloat(2, dish.getCost());
            pstmt.setFloat(3, dish.getWeight());
            pstmt.setInt(4, dish.getDiscountPercent());

            id = getID(pstmt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(String.format("Dish %s, %s, %s, %s has been inserted with id %d successfully.",
                dish.getDishName(), dish.getCost(), dish.getWeight(), dish.getDiscountPercent(), id));
        return id;
    }


    private void displayDishNameAndTotalCost(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(rs.getString("dishName") + "\t"
                    + rs.getString("totalCost"));
        }
    }

    public void findDishesByCost(float from, float to) {
        String query = "SELECT dishName, cost - (cost * 0.01 *discountPercent) AS totalCost " +
                "FROM restaurantmenu " +
                "WHERE cost - (cost * 0.01 *discountPercent) BETWEEN ? AND ?;";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setFloat(1, from);
            pstmt.setFloat(2, to);
            ResultSet rs = pstmt.executeQuery();

            displayDishNameAndTotalCost(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void displayDishNameAndDiscount(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(rs.getString("dishName") + "\t"
                    + rs.getString("discountPercent"));
        }
    }

    public void findDishesWithDiscount() {
        String query = "SELECT dishName, discountPercent " +
                "FROM restaurantmenu " +
                "WHERE discountPercent > 0;";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query,
                     Statement.RETURN_GENERATED_KEYS)) {

            ResultSet rs = pstmt.executeQuery();
            displayDishNameAndDiscount(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
