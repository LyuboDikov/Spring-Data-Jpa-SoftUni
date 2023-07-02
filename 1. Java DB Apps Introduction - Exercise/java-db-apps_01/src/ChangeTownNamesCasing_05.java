import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class ChangeTownNamesCasing_05 {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "somepassword");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        String countryName = scanner.nextLine();

        PreparedStatement updateTownsNames = connection.prepareStatement(
                "UPDATE towns SET name = UPPER(name) WHERE country = ?");

        updateTownsNames.setString(1, countryName);
        int townsCount = updateTownsNames.executeUpdate();

        if (townsCount == 0) {
            System.out.println("No town names were affected.");
            return;
        }

        System.out.printf("%d town names were affected.%n", townsCount);

        PreparedStatement getTownsFromCountry = connection.prepareStatement(
                "SELECT name FROM towns WHERE country = ?");

        getTownsFromCountry.setString(1, countryName);

        ResultSet townSet = getTownsFromCountry.executeQuery();

        List<String> towns = new ArrayList<>();

        while (townSet.next()) {
            String townName = townSet.getString("name");
            towns.add(townName);
        }

        System.out.println(towns);

        connection.close();
    }
}
