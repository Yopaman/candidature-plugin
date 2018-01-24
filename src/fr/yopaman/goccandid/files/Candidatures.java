/*
 * @author Yopaman
 */

package fr.yopaman.goccandid.files;

import fr.yopaman.goccandid.ChatListener;
import fr.yopaman.goccandid.GocCandid;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

public class Candidatures extends AbstractFile {
    public Candidatures(GocCandid main) {
        super(main, "candidatures.yml");
    }

    public void newCandidature(String[] responses) {
        config.set(responses[responses.length - 1] + ".uuid", responses[responses.length - 2]);

        for (int i = 0; i < responses.length - 2; i++) {
            config.set(responses[responses.length - 1] + "." + GocCandid.getMyConfig().getQuestions()[i], responses[i]);
        }
        config.set(responses[responses.length - 1] + ".validation", "false");
    }

    public String getUnaccepted() {
        String result = "";
        for (String username : config.getKeys(false)) {
            if (config.get(username + ".validation").equals("false")) {
                result = result + (ChatColor.RESET + "====================================" + "\n" + ChatColor.GOLD + "" + ChatColor.UNDERLINE + username + "\n");
                for (int i = 0; i < GocCandid.getMyConfig().getQuestions().length; i++) {
                    result = result + (ChatColor.BLUE + "" + ChatColor.BOLD + GocCandid.getMyConfig().getQuestions()[i] + " : " + ChatColor.RESET + "" + ChatColor.AQUA + config.getString(username + "." + GocCandid.getMyConfig().getQuestions()[i]) + "\n");
                }
            }
        }

        if (result.equals("")) {
            result = ChatColor.GOLD + "" + ChatColor.BOLD + "Toutes les candidatures sont déjà validées:)";
        }

        return result;
    }

    public Boolean checkIfExist(CommandSender playerSender) {
        Player player = Bukkit.getServer().getPlayer(playerSender.getName());
        if (config.get(player.getName()) != null) {
            if (player.getUniqueId() == config.get(player.getName() + ".uuid")) {
                if (config.get(player.getName() + ".validation") == "true") {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
