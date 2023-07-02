import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class GetMinionNames_03 {
    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "123456");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);

        int villainId = Integer.parseInt(scanner.nextLine());

        PreparedStatement villainStatement = connection.prepareStatement(
                "SELECT name FROM villains WHERE id = ?");

        villainStatement.setInt(1, villainId);

        ResultSet villainSet = villainStatement.executeQuery();

        if (villainSet.next()) {
            String villainName = villainSet.getString("name");
            System.out.printf("Villain: %s%n", villainName);

            getMinionsAge(connection, villainId);
        } else {
            System.out.printf("No villain with ID %d exists in the database.%n", villainId);
        }

        connection.close();
    }

    private static void getMinionsAge(Connection connection, int villainId) throws SQLException {

        PreparedStatement minionStatement = connection.prepareStatement(
                    "SELECT name, age " +
                        "FROM minions AS m " +
                        "JOIN minions_villains AS mv ON mv.minion_id = m.id " +
                        "WHERE mv.villain_id = ?;");

        minionStatement.setInt(1, villainId);
        ResultSet minionSet = minionStatement.executeQuery();

        for (int i = 1; minionSet.next(); i++) {
            String minionName = minionSet.getString("name");
            int minionAge = minionSet.getInt("age");

            System.out.printf("%d. %s %d%n", i, minionName, minionAge);
        }
    }
}