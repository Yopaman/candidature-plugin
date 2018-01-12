/*
 * @author Yopaman
 */

package fr.yopaman.goccandid;

import fr.yopaman.goccandid.files.Candidatures;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatListener implements Listener {

    private String[] infos = new String[6];
    public static String executor = "";
    private int status = 0;
    private String questions[] = {"Quel est votre age ?", "Depuis combien de temps jouez vous à minecraft ?", "Quelle est votre spécialité dans minecraft ? (redstone, build, pvp, etc...)", "Merci, votre candidature est terminée, vous pouvez attendre la validation d'un modérateur."};

    public static Boolean enabledChat = true;

    public static void setChat(boolean chat) {
        enabledChat = chat;
    }

    public static boolean getChat() {
        return enabledChat;
    }

    @EventHandler
    public void OnChatMessage(AsyncPlayerChatEvent event) {
        if(getChat()) {
            if (event.getPlayer().getName().equals(executor)) {
                if (status == 0) {
                    infos[0] = event.getMessage();
                    event.getPlayer().sendMessage("");
                    event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.UNDERLINE + questions[0]);
                    status = 1;
                    event.setCancelled(true);
                } else if (status == 1) {
                    infos[1] = event.getMessage();
                    event.getPlayer().sendMessage("");
                    event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.UNDERLINE + questions[1]);
                    status = 2;
                    event.setCancelled(true);
                } else if (status == 2) {
                    infos[2] = event.getMessage();
                    event.getPlayer().sendMessage("");
                    event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.UNDERLINE + questions[2]);
                    status = 3;
                    event.setCancelled(true);
                } else if (status == 3) {
                    infos[3] = event.getMessage();
                    infos[4] = event.getPlayer().getUniqueId().toString();
                    infos[5] = event.getPlayer().getName();
                    event.getPlayer().sendMessage("");
                    event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + questions[3]);
                    enabledChat = false;
                    GocCandid.getCandidature().newCandidature(infos[0], infos[1], infos[2], infos[3], infos[4], infos[5]);
                    GocCandid.getCandidature().save();
                    status = 0;
                    executor = "";
                    event.setCancelled(true);
                }
            }
        }
    }
}
