import java.sql.*;
import java.util.*;

public class PrintAllMinionNames_07 {
    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "somepassword");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement getAllMinions = connection.prepareStatement(
                "SELECT name FROM minions");

        ResultSet minionSet = getAllMinions.executeQuery();
        List<String> minionNames = new ArrayList<>();

        while (minionSet.next()) {
            String name = minionSet.getString("name");
            minionNames.add(name);
        }

        List<String> newMinionNameOrder = new ArrayList<>();

        for (int i = 0; i < minionNames.size() / 2; i++) {
            String nextMinion = minionNames.get(i);
            String nextLastMinion = minionNames.get(minionNames.size() - 1 - i);

            newMinionNameOrder.add(nextMinion);
            newMinionNameOrder.add(nextLastMinion);
        }

        Set<String> finalMinionNamesSet = new LinkedHashSet<>(newMinionNameOrder);

        for (String name : finalMinionNamesSet) {
            System.out.println(name);
        }

        connection.close();
    }
}
