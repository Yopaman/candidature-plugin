package fr.yopaman.goccandid;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectionListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (GocCandid.getCandidature().isRefused(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.RED + "[" + ChatColor.GOLD +
            "Candidatures" + ChatColor.RED + "] " + ChatColor.RESET + "" +
            ChatColor.RED + "Votre candidature a été refusée, vous devez la refaire avec la commande /candidature.");
        }

        if (event.getPlayer().hasPermission("goccandid.staff")) {
            int refused = GocCandid.getCandidature().getRefused();
            if (refused > 0) {
                event.getPlayer().sendMessage(ChatColor.RED + "[" + ChatColor.GOLD +
                "Candidatures" + ChatColor.RED + "] " + ChatColor.RESET + "" + ChatColor.BLUE +
                "Il y a " + refused + " candidature(s) en attente de validation.");
            }
        }


    }
}
