/*
 * @author Yopaman
 */

package fr.yopaman.goccandid;

import com.google.common.collect.Lists;
import fr.yopaman.goccandid.files.Candidatures;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CommandCandidature implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!GocCandid.getCandidature().checkIfExist(commandSender)) {
                ChatListener listener = new ChatListener(commandSender.getName());
                GocCandid.getPlugin().getServer().getPluginManager().registerEvents(listener, GocCandid.getPlugin());
                commandSender.sendMessage(ChatColor.BOLD + listener.messageDebut);
                commandSender.sendMessage(ChatColor.UNDERLINE + listener.questions[0] + ChatColor.RESET + "" + ChatColor.GRAY + " (Répondez directement dans le chat)");
                listener.setChat(true);
                return true;
            } else {
                commandSender.sendMessage(ChatColor.RED + "Votre candidature a déjà été acceptée.");
            }
        } else if (args[0].equals("listnew")) {
            commandSender.sendMessage(GocCandid.getCandidature().getUnaccepted());
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command commmand, String alias, String[] args) {
        List<String> completions = Arrays.asList("listnew", "accept", "refuse");

        if (args.length == 1) {
            return completions;
        }
        return null;
    }



}
