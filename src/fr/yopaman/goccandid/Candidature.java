package fr.yopaman.goccandid;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.HashMap;

import static fr.yopaman.goccandid.GocCandid.getPlugin;


public class Candidature {

    private HashMap responses;
    private Player sender;
    private String uuid;

    public Candidature (HashMap responses, Player sender, String uuid) {
        this.responses = responses;
        this.sender = sender;
        this.uuid = uuid;
    }

    private Connection myConnect() {
        try {
            String myUrl = GocCandid.getMyConfig().getMysqlUrl();
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(myUrl, GocCandid.getMyConfig().getMysqlUser(), GocCandid.getMyConfig().getMysqlPassword());
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().info(e.getLocalizedMessage());
        }
        return null;
    }

    public void post() {
        try {
            Connection conn = myConnect();
            String query = "INSERT INTO candidatures (pseudo, uuid, status) values (?, ?, ?)";
            //Insert candidature
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, sender.toString());
            preparedStmt.setString(2, uuid);
            preparedStmt.setString(3, "waiting");
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
            getPlugin().getLogger().warning(e.getMessage());
        }
    }

    public String getUnaccepted() {
        String result = "";
        Connection conn = myConnect();
        String query = "SELECT id, pseudo FROM candidatures WHERE status = waiting";
        try {
            Statement st = conn.createStatement();
            ResultSet candidRs = st.executeQuery(query);
            while (candidRs.next()) {
                result = result + ChatColor.RESET + "====================================" + "\n" + ChatColor.GOLD + "" + ChatColor.UNDERLINE + candidRs.getString("pseudo") + "\n";
                query = "SELECT question, reponse FROM reponses WHERE candid_id = " + candidRs.getInt("id");
                ResultSet responsesRs = st.executeQuery(query);
                while (responsesRs.next()) {
                    result = result + (ChatColor.BLUE + "" + ChatColor.BOLD + responsesRs.getString("question") + " : " + ChatColor.RESET + "" + ChatColor.AQUA + responsesRs.getString("reponse") + "\n");
                }
            }
            st.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            getPlugin().getLogger().warning(e.getMessage());
            return null;
        }
    }
}
