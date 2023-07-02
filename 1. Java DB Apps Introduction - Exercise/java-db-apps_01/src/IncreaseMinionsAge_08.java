import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class IncreaseMinionsAge_08 {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "somepassword");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        int[] minionsIds = Arrays.stream(scanner.nextLine().split(" "))
                            .mapToInt(Integer::parseInt).toArray();

        for (int minionId : minionsIds) {
            updateMinionAge(connection, minionId);
            updateMinionName(connection, minionId);
        }

        ResultSet minionsResultSet = getResultSet(connection);

        while (minionsResultSet.next()) {
            String name = minionsResultSet.getString("name");
            int age = minionsResultSet.getInt("age");

            System.out.printf("%s %d%n", name, age);
        }

        connection.close();
    }

    private static ResultSet getResultSet(Connection connection) throws SQLException {
        PreparedStatement getAllMinions = connection.prepareStatement(
                "SELECT name, age FROM minions"
        );

        return getAllMinions.executeQuery();
    }

    private static void updateMinionName(Connection connection, int minionId) throws SQLException {
        PreparedStatement updateMinionNames = connection.prepareStatement(
                "UPDATE minions SET name = LOWER(name) WHERE id = ?"
        );

        updateMinionNames.setInt(1, minionId);
        updateMinionNames.executeUpdate();
    }

    private static void updateMinionAge(Connection connection, int minionId) throws SQLException {
        PreparedStatement updateMinionAge = connection.prepareStatement(
                "UPDATE minions SET age = age + 1 WHERE id = ?"
        );

        updateMinionAge.setInt(1, minionId);
        updateMinionAge.executeUpdate();
    }
}
