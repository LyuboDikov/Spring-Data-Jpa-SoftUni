import java.sql.*;
import java.util.Properties;
public class GetVillainsName_02 {
    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "somepassword");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement statement = connection.prepareStatement(
                "SELECT v.name, COUNT(DISTINCT mv.minion_id) AS minions_count " +
                        "FROM villains AS v JOIN minions_villains mv ON v.id = mv.villain_id " +
                        "GROUP BY v.id " +
                        "HAVING minions_count > ? " +
                        "ORDER BY minions_count DESC;"
        );

        statement.setInt(1, 15);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {

            String villainName = resultSet.getString("name");
            int minionCount = resultSet.getInt("minions_count");

            System.out.println(villainName + " " + minionCount);
        }
        connection.close();
    }
}