/*
 * @author Yopaman
 */

package fr.yopaman.goccandid;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatListener implements Listener {

    private String[] infos = new String[4];
    public static String executor = "";
    private int status = 0;
    private String questions[] = {"Quel est votre age ? ", "Depuis combien de temps jouez vous à minecraft ?", "Quelle est votre spécialité dans minecraft ? (redstone, build, pvp, etc...)", "Merci, votre candidature est terminée, vous pouvez attendre la validation d'un modérateur."};

    @EventHandler
    public void OnChatMessage(AsyncPlayerChatEvent event) {
        if (event.getPlayer().getName().equals(executor)) {
            if (status == 0) {
                infos[0] = event.getMessage();
                event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.UNDERLINE + questions[0]);
                status = 1;
            } else if (status == 1) {
                infos[1] = event.getMessage();
                event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.UNDERLINE + questions[1]);
                status = 2;
            } else if (status == 2) {
                infos[2] = event.getMessage();
                event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.UNDERLINE + questions[2]);
                status = 3;
            } else if (status == 3) {
                infos[3] = event.getMessage();
                event.getPlayer().sendMessage(questions[3]);
                HandlerList.unregisterAll(this);
                event.getPlayer().sendMessage(infos[0]);
                status = 0;
            }

        }
    }
}
