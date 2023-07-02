import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class IncreaseAgeStoredProcedure_09 {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "somepassword");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        int minionId = Integer.parseInt(scanner.nextLine());

        CallableStatement increaseAgeStatement = connection.prepareCall(
                "{CALL usp_get_older(?)}");

        increaseAgeStatement.setInt(1, minionId);
        increaseAgeStatement.execute();

        PreparedStatement getMinionStatement = connection.prepareStatement(
                "SELECT name, age FROM minions WHERE id = ?");

        getMinionStatement.setInt(1, minionId);
        ResultSet minionResultSet = getMinionStatement.executeQuery();

        if (minionResultSet.next()) {
            String minionName = minionResultSet.getString("name");
            int minionAge = minionResultSet.getInt("age");

            System.out.printf("%s %d", minionName, minionAge);
        }

        connection.close();
    }
}
