/*
 * @author Yopaman
 */

package fr.yopaman.goccandid;

import fr.yopaman.goccandid.fr.yopaman.goccandid.conversation.ConvFirstPrompt;
import fr.yopaman.goccandid.fr.yopaman.goccandid.conversation.ConvPrefix;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.conversations.Conversable;

import java.util.Arrays;
import java.util.List;


public class CommandCandidature implements CommandExecutor, TabCompleter {

    GocCandid plugin;

    public CommandCandidature(GocCandid instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!GocCandid.getCandidature().checkIfExist(commandSender)) {

                plugin.factory.withFirstPrompt(new ConvFirstPrompt(true)).thatExcludesNonPlayersWithMessage("Seul les joueurs peuvent faire leur candidature !")
                        .withPrefix(new ConvPrefix()).buildConversation((Conversable) commandSender).begin();
                return false;
            } else {
                commandSender.sendMessage(ChatColor.RED + "Votre candidature a déjà été acceptée.");
            }
        } else if (args[0].equals("listnew")) {
            commandSender.sendMessage(GocCandid.getCandidature().getUnaccepted());
        } else if(args[0].equals("get")) {
            if (args.length > 1) {
                commandSender.sendMessage(GocCandid.getCandidature().getOne(args[1]));
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command commmand, String alias, String[] args) {
        List<String> completions = Arrays.asList("listnew", "accept", "refuse", "get");

        if (args.length == 1) {
            for (int i = 0; i < completions.size(); i++) {
                if (completions.get(i).startsWith(args[i])) {
                    return completions;
                }
            }
            return completions;
        }
        return null;
    }



}
