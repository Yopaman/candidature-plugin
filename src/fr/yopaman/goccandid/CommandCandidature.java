/*
 * @author Yopaman
 */

package fr.yopaman.goccandid;

import fr.yopaman.goccandid.conversation.ConvFirstPrompt;
import fr.yopaman.goccandid.conversation.ConvPrefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import javax.swing.text.html.HTMLDocument;
import java.util.*;


public class CommandCandidature implements CommandExecutor, TabCompleter {

    GocCandid plugin;

    public CommandCandidature(GocCandid instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (commandSender.hasPermission(new Permission("goccandid.candidature"))) {
                if (!GocCandid.getCandidature().checkIfExist(commandSender)) {
                    plugin.factory.withFirstPrompt(new ConvFirstPrompt(true)).thatExcludesNonPlayersWithMessage("Seul les joueurs peuvent faire leur candidature !")
                    .withPrefix(new ConvPrefix()).buildConversation((Conversable) commandSender).begin();
                    Bukkit.broadcast(ChatColor.RED + "[" + ChatColor.GOLD +
                    "Candidatures" + ChatColor.RED + "] " + ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GOLD + commandSender.getName() + ChatColor.RESET + "" + ChatColor.AQUA + " commence sa candidature.", "goccandid.staff");
                    return true;
                } else {
                    commandSender.sendMessage(ChatColor.RED + "Votre candidature a déjà été acceptée.");
                    return true;
                }
            }
        } else if(args[0].equalsIgnoreCase("listnew")) {
            if (commandSender.hasPermission(new Permission("goccandid.candidature.listnew"))) {
                commandSender.sendMessage(GocCandid.getCandid().getUnaccepted());
            } else {
                commandSender.sendMessage("Vous n'avez pas la permission d'executer cette commande !");
            }
        } else if(args[0].equalsIgnoreCase("get")) {
            if (commandSender.hasPermission(new Permission("goccandid.candidature.get"))) {
                if (args.length > 1) {
                    commandSender.sendMessage(GocCandid.getCandidature().getOne(args[1]));
                } else {
                    return false;
                }
            } else {
                commandSender.sendMessage("Vous n'avez pas la permission d'executer cette commande !");
            }
        } else if(args[0].equalsIgnoreCase("accept")) {
            if (commandSender.hasPermission(new Permission("goccandid.candidature.accept"))) {
                if (args.length > 1) {
                    commandSender.sendMessage(GocCandid.getCandidature().accept(args[1]));
                    GocCandid.getCandidature().save();
                    Player player = Bukkit.getServer().getPlayer(args[1]);
                    if (player.isOnline()) {
                        player.sendMessage(ChatColor.RED + "[" + ChatColor.GOLD +
                        "Candidatures" + ChatColor.RED + "] " + ChatColor.RESET + "" + ChatColor.GREEN + "Votre candidature a été acceptée :)");
                    }
                } else {
                    return false;
                }
            } else {
                commandSender.sendMessage("Vous n'avez pas la permission d'executer cette commande !");
            }
        } else if(args[0].equalsIgnoreCase("refuse")) {
            if (commandSender.hasPermission(new Permission("goccandid.candidature.refuse"))) {
                if (args.length > 1) {
                    commandSender.sendMessage(GocCandid.getCandidature().refuse(args[1]));
                    GocCandid.getCandidature().save();
                    Player player = Bukkit.getServer().getPlayer(args[1]);
                    if (player.isOnline()) {
                        player.sendMessage(ChatColor.RED + "[" + ChatColor.GOLD +
                        "Candidatures" + ChatColor.RED + "] " + ChatColor.RESET + "" + ChatColor.RED + "Votre candidature a été refusée, vous devez la refaire avec la commande /candidature.");
                    }
                } else {
                    return false;
                }
            } else {
                commandSender.sendMessage("Vous n'avez pas la permission d'executer cette commande !");
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("listnew");
        list.add("accept");
        list.add("refuse");
        list.add("get");
        ArrayList<String> finalCompletion = new ArrayList<String>();
        if (command.getName().equalsIgnoreCase("candidature")) {
            if(args.length == 0) {
                return Arrays.asList("listnew", "accept", "refuse", "get");
            } else if (args.length == 1) {
                List<String> suggestions = new ArrayList<String>();
                if ("listnew".startsWith(args[0])) {
                    suggestions.add("listnew");
                }
                if ("accept".startsWith(args[0])) {
                    suggestions.add("accept");
                }
                if ("refuse".startsWith(args[0])) {
                    suggestions.add("refuse");
                }
                if ("get".startsWith(args[0])) {
                    suggestions.add("get");
                }
                return suggestions;
            }
        }
        return null;
    }



}
