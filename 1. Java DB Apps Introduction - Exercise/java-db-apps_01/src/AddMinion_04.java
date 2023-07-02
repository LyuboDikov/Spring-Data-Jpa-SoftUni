import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class AddMinion_04 {
    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "somepassword");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        Scanner scanner = new Scanner(System.in);
        String[] minionInfo = scanner.nextLine().split(" ");
        String minionName = minionInfo[1];
        int minionAge = Integer.parseInt(minionInfo[2]);
        String minionTown = minionInfo[3];

        String villainName = scanner.nextLine().split(" ")[1];

        int townId = getTownId(connection, minionTown);
        int villainId = getVillainId(connection, villainName);

        PreparedStatement insertMinionStmt = connection.prepareStatement(
                "INSERT INTO minions(name, age, town_id) VALUES (?, ?, ?)");

        insertMinionStmt.setString(1, minionName);
        insertMinionStmt.setInt(2, minionAge);
        insertMinionStmt.setInt(3, townId);
        insertMinionStmt.executeUpdate();

        PreparedStatement getLastMinion = connection.prepareStatement(
                "SELECT id FROM minions ORDER BY id DESC LIMIT 1"
        );

        ResultSet lastMinionSet = getLastMinion.executeQuery();
        lastMinionSet.next();

        int lastMinionId = lastMinionSet.getInt("id");

        PreparedStatement setMinionsToVillainsStmt = connection.prepareStatement(
                "INSERT INTO minions_villains VALUES (?, ?)");

        setMinionsToVillainsStmt.setInt(1, lastMinionId);
        setMinionsToVillainsStmt.setInt(2, villainId);
        setMinionsToVillainsStmt.executeUpdate();

        System.out.printf("Successfully added %s to be minion of %s.%n", minionName, villainName);

        connection.close();

    }

    private static int getVillainId(Connection connection, String villainName) throws SQLException {
        PreparedStatement selectVillainStmt = connection.prepareStatement(
                "SELECT id FROM villains WHERE name = ?");

        selectVillainStmt.setString(1, villainName);

        ResultSet villainSet = selectVillainStmt.executeQuery();

        int villainId;

        if (!villainSet.next()) {
            PreparedStatement insertVillainStmt = connection.prepareStatement(
                    "INSERT INTO villains(name, evilness_factor) VALUES (?, ?);");

            insertVillainStmt.setString(1, villainName);
            insertVillainStmt.setString(2, "evil");
            insertVillainStmt.executeUpdate();

            ResultSet newVillainSet = selectVillainStmt.executeQuery();
            newVillainSet.next();

            villainId = newVillainSet.getInt("id");

            System.out.printf("Villain %s was added to the database.%n", villainName);
        } else {
            villainId = villainSet.getInt("id");
        }

        return villainId;
    }

    private static int getTownId(Connection connection, String minionTown) throws SQLException {
        PreparedStatement selectTownStmt = connection.prepareStatement(
                "SELECT id FROM towns WHERE name = ?");

        selectTownStmt.setString(1, minionTown);

        ResultSet townSet = selectTownStmt.executeQuery();

        int townId;

        if (!townSet.next()) {
            PreparedStatement insertTownStmt = connection.prepareStatement(
                    "INSERT INTO towns(name) VALUES (?);");

            insertTownStmt.setString(1, minionTown);
            insertTownStmt.executeUpdate();

            ResultSet newTownSet = selectTownStmt.executeQuery();
            newTownSet.next();

            townId = newTownSet.getInt("id");
            System.out.printf("Town %s was added to the database.%n", minionTown);
        } else {
            townId = townSet.getInt("id");
        }

        return townId;
    }
}
