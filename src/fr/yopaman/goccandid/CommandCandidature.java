/*
 * Author: Yopaman
 */

package fr.yopaman.goccandid;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCandidature implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        commandSender.sendMessage(ChatColor.AQUA + "Bienvenue ! " + ChatColor.GOLD + "Vous êtes sur le point de faire votre candidature pour rejoindre le serveur. Vous devez répondre à quelques questions très simples.");
        commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Quel est votre prénom ? " + ChatColor.RESET + "" + ChatColor.GRAY + "(Répondez directement dans le chat)");
        return true;
    }
}
