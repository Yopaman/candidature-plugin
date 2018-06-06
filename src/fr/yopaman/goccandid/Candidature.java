package fr.yopaman.goccandid;

import org.bukkit.entity.Player;
import java.sql.*;
import java.util.HashMap;


public class Candidature {

    private HashMap responses;
    private Player sender;
    private String uuid;

    public Candidature (HashMap responses, Player sender, String uuid) {
        this.responses = responses;
        this.sender = sender;
        this.uuid = uuid;
    }

    Connection myConnect() {
        try {
            String myUrl = GocCandid.getMyConfig().getMysqlUrl();
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(GocCandid.getMyConfig().getMysqlUrl(), GocCandid.getMyConfig().getMysqlUser(), GocCandid.getMyConfig().getMysqlPassword());
        } catch (Exception e) {
            GocCandid.getPlugin().getLogger().warning(e.getMessage());
            return null;
        }
    }

    void post() {
        Connection conn = myConnect();
        String query = "INSERT INTO candidatures (pseudo, uuid)"
                + " values (?, ?)";

        try {
            //Insert candidature
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, sender.toString());
            preparedStmt.setString(2, uuid);
            preparedStmt.execute();
            preparedStmt.close();

            //Select inserted candidature id
            query = "SELECT id FROM candidatures WHERE uuid = " + uuid;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            int candid_id = rs.getInt(0);
            query = "INSERT INTO reponses (candid_id, question, reponse) values(?, ?, ?)";

            //Insert questions and responses
            for (int i = 0; i < responses.size(); i++) {
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1, candid_id);
                preparedStmt.setString(2, GocCandid.getMyConfig().getQuestions()[i]);
                preparedStmt.setString(3, (String) responses.get(GocCandid.getMyConfig().getQuestions()[i]));
                preparedStmt.execute();
                preparedStmt.close();

            }
            conn.close();
        } catch (SQLException e) {
            GocCandid.getPlugin().getLogger().warning(e.getMessage());
        }
    }



}
