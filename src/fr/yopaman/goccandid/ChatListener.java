/*
 * @author Yopaman
 */

package fr.yopaman.goccandid;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatListener implements Listener {

    public String executor;

    public ChatListener(String executor) {
        super();
        this.executor = executor;
    }

    public ChatListener() {
        super();
    }

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
                    event.getPlayer().sendMessage(ChatColor.BOLD + messageFin);
                    enabledChat = false;
                    GocCandid.getCandidature().newCandidature(infos);
                    GocCandid.getCandidature().save();
                    executor = "";
                    event.setCancelled(true);
                } else {
                    for (int i = 0; i < insultes.length; i++) {
                        if (event.getMessage().contains(insultes[i])) {
                            event.getPlayer().sendMessage(ChatColor.RED + "Votre message continent le mot " + ChatColor.GOLD + ChatColor.ITALIC + insultes[i] + ChatColor.RESET + ChatColor.RED + " qui est interdit. Veuillez le réécrire en supprimant ce mot.");
                            status--;
                            event.setCancelled(true);
                        }
                    }
                    infos[status - 1] = event.getMessage();
                    event.getPlayer().sendMessage("");
                    event.getPlayer().sendMessage(ChatColor.UNDERLINE + questions[status]);
                    status++;
                    event.setCancelled(true);
                }
            }
        }
    }

    private int status = 1;
    public CharSequence insultes[] = GocCandid.getMyConfig().getInsultes();
    public String messageDebut = GocCandid.getMyConfig().getMessageDebut();
    public String messageFin = GocCandid.getMyConfig().getMessageFin();
    public String questions[] = GocCandid.getMyConfig().getQuestions();
    public String infos[] = new String[questions.length + 2];
    public Boolean enabledChat = true;

    public void setChat(boolean chat) {
        enabledChat = chat;
    }
    public boolean getChat() {
        return enabledChat;
    }
}
