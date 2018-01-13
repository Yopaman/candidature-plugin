/*
 * @author Yopaman
 */

package fr.yopaman.goccandid;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatListener implements Listener {

    @EventHandler
    public void OnChatMessage(AsyncPlayerChatEvent event) {
        if(getChat()) {
            if (event.getPlayer().getName().equals(executor)) {
                if (status == questions.length) {
                    infos[status - 1] = event.getMessage();
                    infos[status] = event.getPlayer().getUniqueId().toString();
                    infos[status + 1] = event.getPlayer().getName();
                    status = 1;
                    event.getPlayer().sendMessage("");
                    event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Merci, votre candidature est terminée, vous pouvez attendre la validation d'un modérateur.");
                    enabledChat = false;
                    GocCandid.getCandidature().newCandidature(infos);
                    GocCandid.getCandidature().save();
                    executor = "";
                    event.setCancelled(true);
                } else {
                    infos[status - 1] = event.getMessage();
                    event.getPlayer().sendMessage("");
                    event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.UNDERLINE + questions[status]);
                    status++;
                    event.setCancelled(true);
                }
            }
        }
    }

    static String executor = "";
    private int status = 1;
    static String questions[] = GocCandid.getMyConfig().getConfig();
    static String infos[] = new String[questions.length + 2];
    static Boolean enabledChat = true;

    static void setChat(boolean chat) {
        enabledChat = chat;
    }
    static boolean getChat() {
        return enabledChat;
    }
}
