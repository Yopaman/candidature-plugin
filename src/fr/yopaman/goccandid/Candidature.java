package fr.yopaman.goccandid;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
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

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = GocCandid.getMyConfig().getEncryptionPassword().getBytes();

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }

    private static String encrypt(String strClearText) throws Exception{
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(strClearText.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encValue);
        return encryptedValue;
    }

    private static String decrypt(String strEncrypted) throws Exception{
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(strEncrypted);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Connection myConnect() {
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
            String query = "SELECT uuid FROM candidatures WHERE uuid ='" + uuid + "'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Boolean needUpdate;
            if (rs.isBeforeFirst()) {
                needUpdate = true;
            } else {
                needUpdate = false;
            }
            if (needUpdate == false) {
                query = "INSERT INTO candidatures (pseudo, uuid, status) values (?, ?, ?)";
            } else {
                query = "UPDATE candidatures SET pseudo = ?, uuid = ?, status = ? WHERE uuid = '" + uuid + "'";
            }
            //Insert candidature
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, sender.getName());
            preparedStmt.setString(2, uuid);
            preparedStmt.setString(3, "en attente");
            preparedStmt.execute();
            preparedStmt.close();

            //Select inserted candidature id
            query = "SELECT id FROM candidatures WHERE uuid = '" + uuid + "'";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            int candid_id = rs.getInt("id");
            if (needUpdate == false) {
                query = "INSERT INTO reponses (candid_id, question, reponse) values(?, ?, ?)";
            } else {
                query = "UPDATE reponses SET candid_id = ?, question = ?, reponse = ? WHERE question = ?";
            }
            //Insert questions and responses
            for (int i = 0; i < responses.size(); i++) {
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1, candid_id);
                preparedStmt.setString(2, GocCandid.getMyConfig().getQuestions()[i]);
                preparedStmt.setString(3, encrypt((String) responses.get(GocCandid.getMyConfig().getQuestions()[i])));
                if (needUpdate == true)
                    preparedStmt.setString(4, GocCandid.getMyConfig().getQuestions()[i]);
                preparedStmt.execute();
                preparedStmt.close();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUnaccepted() {
        try {
            String result = "";
            Connection conn = myConnect();
            String query = "SELECT id, pseudo FROM candidatures WHERE status = 'en attente'";
            Statement candidSt = conn.createStatement();
            ResultSet candidRs = candidSt.executeQuery(query);
            if (!candidRs.isBeforeFirst()) {
                result = ChatColor.GOLD + "" + ChatColor.BOLD + "Toutes les candidatures sont déjà validées :)";
                return result;
            }
            Statement responsesSt = conn.createStatement();
            while (candidRs.next()) {
                result = result + ChatColor.RESET + "====================================" + "\n" + ChatColor.GOLD + "" + ChatColor.UNDERLINE + candidRs.getString("pseudo") + "\n";
                query = "SELECT question, reponse FROM reponses WHERE candid_id = '" + candidRs.getString("id") + "'";
                ResultSet responsesRs = responsesSt.executeQuery(query);
                while (responsesRs.next()) {
                    result = result + (ChatColor.BLUE + "" + ChatColor.BOLD + responsesRs.getString("question") + " : " + ChatColor.RESET + "" + ChatColor.AQUA + decrypt(responsesRs.getString("reponse")) + "\n");
                }
            }
            responsesSt.close();
            candidSt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getWaitingNumber() {
        try {
            int count = 0;
            Connection conn = myConnect();
            String query = "SELECT * FROM candidatures WHERE status = 'en attente'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                count++;
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getOne(String pseudo) {
        try {
            Connection conn = myConnect();
            String query = "SELECT * FROM candidatures WHERE pseudo = '" + pseudo + "'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) {
                return ChatColor.RED + "Ce joueur n'a pas fait sa candidature ou n'existe pas.";
            }
            rs.next();
            String result = ChatColor.GOLD + "" + ChatColor.UNDERLINE + rs.getString("pseudo") + ChatColor.RESET + " : " + ChatColor.BLUE + rs.getString("status") + "\n";
            String candid_id = rs.getString("id");
            query = "SELECT question, reponse FROM reponses WHERE candid_id = '" + candid_id + "'";
            rs = st.executeQuery(query);
            while (rs.next()) {
                result = result + (ChatColor.BLUE + "" + ChatColor.BOLD + rs.getString("question") + " : " + ChatColor.RESET + "" + ChatColor.AQUA + decrypt(rs.getString("reponse")) + "\n");
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean checkIfExist(CommandSender cs) {
        try {
            if (cs instanceof Player) {
                Connection conn = myConnect();
                String query = "SELECT * FROM candidatures WHERE uuid = '" + ((Player) cs).getUniqueId() + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (!rs.isBeforeFirst()) {
                    return false;
                } else {
                    rs.next();
                    if (rs.getString("status").equals("accepté")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isRefused (Player player) {
        try {
            Connection conn = myConnect();
            String query = "SELECT status, uuid FROM candidatures WHERE uuid = '" + player.getUniqueId() + "'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) {
                return false;
            } else {
                rs.next();
                if (rs.getString("status").equals("refusé")) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String accept(String pseudo) {
        try {
            Connection conn = myConnect();
            String query = "UPDATE candidatures SET status = 'accepté' WHERE pseudo = '" + pseudo + "'";
            Statement st = conn.createStatement();
            int i = st.executeUpdate(query);
            if (i <= 0) {
                return ChatColor.RED + "Ce joueur n'a pas fait de candidature ou n'existe pas.";
            } else {
                Player player = Bukkit.getServer().getPlayer(pseudo);
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.RED + "[" + ChatColor.GOLD +
                            "Candidatures" + ChatColor.RED + "] " + ChatColor.RESET + "" + ChatColor.GREEN + "Votre candidature a été acceptée :)");
                }
                return ChatColor.GREEN + "La candidature de " + pseudo + " a bien été acceptée.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erreur interne !";
        }
    }

    public static String refuse(String pseudo) {
        try {
            Connection conn = myConnect();
            String query = "UPDATE candidatures SET status = 'refusé' WHERE pseudo = '" + pseudo + "'";
            Statement st = conn.createStatement();
            int i = st.executeUpdate(query);
            if (i <= 0) {
                return ChatColor.RED + "Ce joueur n'a pas fait de candidature ou n'existe pas.";
            } else {
                Player player = Bukkit.getServer().getPlayer(pseudo);
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.RED + "[" + ChatColor.GOLD +
                            "Candidatures" + ChatColor.RED + "] " + ChatColor.RESET + "" + ChatColor.RED + "Votre candidature a été refusée, vous devez la refaire en utilisant la commande /candidature.");
                }
                return ChatColor.GREEN + "La candidature de " + pseudo + " a bien été refusée.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erreur interne !";
        }
    }
}
