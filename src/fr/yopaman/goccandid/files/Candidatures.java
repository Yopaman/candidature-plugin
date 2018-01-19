/*
 * @author Yopaman
 */

package fr.yopaman.goccandid.files;

import fr.yopaman.goccandid.ChatListener;
import fr.yopaman.goccandid.GocCandid;
import org.bukkit.ChatColor;
import org.bukkit.configuration.MemorySection;

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
            config.set(responses[responses.length - 1] + "." + ChatListener.questions[i], responses[i]);
        }
        config.set(responses[responses.length - 1] + ".status", "en attente de validation");
    }

    public String getUnaccepted() {
        String result = "";
        for (String username : config.getKeys(false)) {
            if (config.getString(username + ".status") == "en attente de validation") {
                for (int i = 0; i < GocCandid.getMyConfig().getQuestions().length; i++) {
                    result += ChatColor.UNDERLINE + "" + ChatColor.GOLD + config.getString(username) + "\n\n";
                    result += ChatColor.RESET + "" + ChatColor.BLUE + GocCandid.getMyConfig().getQuestions()[i] + " : " + config.getString(username + "." + GocCandid.getMyConfig().getQuestions()[i] + "\n");
                }
            }
        }

        return result;
    }
}
