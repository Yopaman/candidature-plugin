/*
 * @author Yopaman
 */

package fr.yopaman.goccandid.files;

import fr.yopaman.goccandid.GocCandid;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.swing.text.StyledEditorKit;
import java.util.*;

public class Candidatures extends AbstractFile {
    public Candidatures(GocCandid main) {
        super(main, "candidatures.yml");
    }

    public void newCandidature(HashMap responses, String pseudo, String uuid) {
        config.set(pseudo.toLowerCase() + ".uuid", uuid);
        for (int i = 0; i < responses.size(); i++) {
            config.set(pseudo.toLowerCase() + "." + GocCandid.getMyConfig().getQuestions()[i], responses.get(GocCandid.getMyConfig().getQuestions()[i]));
        }
        config.set(pseudo.toLowerCase() + ".validation", "en attente");
    }

    public String getUnaccepted() {
        String result = "";
        for (String username : config.getKeys(false)) {
            if (config.get(username + ".validation").equals("en attente")) {
                result = result + (ChatColor.RESET + "====================================" + "\n" + ChatColor.GOLD + "" + ChatColor.UNDERLINE + username + "\n");
                for (int i = 0; i < GocCandid.getMyConfig().getQuestions().length; i++) {
                    result = result + (ChatColor.BLUE + "" + ChatColor.BOLD + GocCandid.getMyConfig().getQuestions()[i] + " : " + ChatColor.RESET + "" + ChatColor.AQUA + config.getString(username + "." + GocCandid.getMyConfig().getQuestions()[i]) + "\n");
                }
            }
        }

        if (result.equals("")) {
            result = ChatColor.GOLD + "" + ChatColor.BOLD + "Toutes les candidatures sont déjà validées :)";
        }

        return result;
    }

    public int getRefused() {
        int refusedNumber = 0;
        for (String username : config.getKeys(false)) {
            if (config.get(username + ".validation").equals("en attente")) {
                refusedNumber++;
            }
        }
        return refusedNumber;
    }

    public String getOne(String pseudo) {
        ConfigurationSection candid = config.getConfigurationSection(pseudo.toLowerCase());
        String candidResult = ChatColor.GOLD + "" + ChatColor.UNDERLINE + pseudo + ChatColor.RESET + "\n";
        if (config.get(pseudo.toLowerCase()) != null) {
            for (String key : candid.getKeys(false)) {
                candidResult = candidResult + ChatColor.BLUE + "" + ChatColor.BOLD + key + " : " + ChatColor.RESET + "" + ChatColor.AQUA + candid.get(key) + "\n";
            }
        } else {
            candidResult = ChatColor.RED + "Ce joueur n'a pas fait sa candidature ou n'existe pas.";
        }

        return candidResult;
    }

    public Boolean checkIfExist(CommandSender playerSender) {
        if (playerSender instanceof Player) {
            if (config.get(playerSender.getName().toLowerCase()) != null) {
                if (config.get(playerSender.getName().toLowerCase() + ".uuid").equals(((Player)playerSender).getUniqueId().toString())) {
                    if (config.get(playerSender.getName().toLowerCase() + ".validation").equals("accepté")) {
                        return true;
                    } else {
                        playerSender.sendMessage("pas validé");
                        return false;
                    }
                } else {
                    playerSender.sendMessage("uuid correspond pas");
                    return false;
                }
            } else {
                playerSender.sendMessage("candid existe pas");
                return false;
            }
        } else {
            return false;
        }
    }

    public Boolean isRefused(Player player) {
        if (config.get(player.getName().toLowerCase()) != null) {
            if (config.get(player.getName().toLowerCase() + ".validation").equals("refusé")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public String accept(String pseudo) {
        pseudo = pseudo.toLowerCase();
        if (config.get(pseudo) != null) {
            config.set(pseudo + ".validation", "accepté");
            return ChatColor.GREEN + "La candidature de " + pseudo + " a été acceptée.";
        } else {
            return ChatColor.RED + "Ce joueur n'a pas fait de candidature ou n'existe pas.";
        }
    }

    public String refuse(String pseudo) {
        pseudo = pseudo.toLowerCase();
        if (config.get(pseudo) != null) {
            config.set(pseudo + ".validation", "refusé");
            return ChatColor.GREEN + "La candidature de " + pseudo + " a été refusée.";
        } else {
            return ChatColor.RED + "Ce joueur n'a pas fait de candidature ou n'existe pas.";
        }
    }
}
