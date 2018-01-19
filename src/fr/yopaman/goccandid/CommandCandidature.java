/*
 * @author Yopaman
 */

package fr.yopaman.goccandid;

import fr.yopaman.goccandid.files.Candidatures;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class CommandCandidature implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.BOLD + ChatListener.messageDebut);
            commandSender.sendMessage(ChatColor.UNDERLINE + ChatListener.questions[0] + ChatColor.RESET + "" + ChatColor.GRAY + " (Répondez directement dans le chat)");
            ChatListener.executor = commandSender.getName();
            ChatListener.setChat(true);
            return true;
        } else if (args[0].equals("listnew")) {
            commandSender.sendMessage(GocCandid.getCandidature().getUnaccepted());
        } else {
            return false;
        }
        return true;
    }



}
