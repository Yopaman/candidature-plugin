/*
 * @author Yopaman
 */

package fr.yopaman.goccandid.files;

import fr.yopaman.goccandid.ChatListener;
import fr.yopaman.goccandid.GocCandid;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.*;

public class Candidatures extends AbstractFile {
    public Candidatures(GocCandid main) {
        super(main, "candidatures.yml");
    }

    public void newCandidature(HashMap responses, String pseudo, String uuid) {
        config.set(pseudo.toLowerCase() + ".uuid", uuid);
        for (int i = 0; i < responses.size(); i++) {
            config.set(pseudo + "." + GocCandid.getMyConfig().getQuestions()[i], responses.get(GocCandid.getMyConfig().getQuestions()[i]));
        }
        config.set(pseudo + ".validation", "false");
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
                if (config.get(player.getName().toLowerCase() + ".validation") == "true") {
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

    public String getOne(String pseudo) {
        ConfigurationSection candid = config.getConfigurationSection(pseudo.toLowerCase());
        String candidResult = ChatColor.GOLD + "" + ChatColor.UNDERLINE + pseudo + ChatColor.RESET + "\n";
        if (config.get(pseudo) != null) {
            for (String key : candid.getKeys(false)) {
                candidResult = candidResult + ChatColor.BLUE + "" + ChatColor.BOLD + key + " : " + ChatColor.RESET + "" + ChatColor.AQUA + candid.get(key) + "\n";
            }
        } else {
            candidResult = ChatColor.RED + "Ce joueur n'a pas fait sa candidature ou n'existe pas.";
        }

        return candidResult;
    }
}
