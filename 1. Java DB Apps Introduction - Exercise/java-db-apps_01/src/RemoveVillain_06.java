import java.sql.*;
import java.util.Properties;
import java.util.Scanner;


public class RemoveVillain_06 {
    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "somepassword");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);

        int villainId = Integer.parseInt(scanner.nextLine());

        PreparedStatement getVillainById = connection.prepareStatement(
                "SELECT name FROM villains WHERE id = ?");

        getVillainById.setInt(1, villainId);
        ResultSet villainSet = getVillainById.executeQuery();

        if (!villainSet.next()) {
            System.out.println("No such villain was found");
            return;
        }

        String villainName = villainSet.getString("name");


        PreparedStatement selectAllMinionsForVillain = connection.prepareStatement(
                "SELECT COUNT(minion_id) AS count " +
                        "FROM minions_villains WHERE villain_id = ?"
        );
        selectAllMinionsForVillain.setInt(1, villainId);
        ResultSet minionsCountSet = selectAllMinionsForVillain.executeQuery();
        minionsCountSet.next();
        int deletedMinionsCount = minionsCountSet.getInt("count");

        connection.setAutoCommit(false);

        try {
            PreparedStatement releaseMinions = connection.prepareStatement(
                    "DELETE FROM minions_villains WHERE villain_id = ?");
            releaseMinions.setInt(1, villainId);
            releaseMinions.executeUpdate();

            PreparedStatement deleteVillain = connection.prepareStatement(
                    "DELETE FROM villains WHERE id = ?");
            deleteVillain.setInt(1, villainId);
            deleteVillain.executeUpdate();

            connection.commit();
            System.out.printf("%s was deleted%n", villainName);
            System.out.printf("%d minions deleted", deletedMinionsCount);

        } catch (SQLException e) {
            connection.rollback();
        }

        connection.close();
    }
}
