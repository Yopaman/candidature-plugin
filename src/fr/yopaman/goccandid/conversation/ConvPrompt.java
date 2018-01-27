package fr.yopaman.goccandid.conversation;

import fr.yopaman.goccandid.GocCandid;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ConvPrompt extends StringPrompt {

    public String questions[] = GocCandid.getMyConfig().getQuestions();
    public String messageFin = GocCandid.getMyConfig().getMessageFin();
    public CharSequence insultes[] = GocCandid.getMyConfig().getInsultes();

    @Override
    public String getPromptText(ConversationContext c) {
        return ChatColor.BLUE + "" + ChatColor.BOLD + questions[c.getAllSessionData().size()];
    }

    @Override
    public Prompt acceptInput(ConversationContext c, String s) {
        for (int i = 0; i < insultes.length; i++) {
            if (s.contains(insultes[i])) {
                c.getForWhom().sendRawMessage(ChatColor.RED + "Votre message continent le mot " + ChatColor.GOLD + ChatColor.ITALIC + insultes[i] + ChatColor.RESET + ChatColor.RED + " qui est interdit. Veuillez le réécrire en supprimant ce mot.");
                return new ConvPrompt();
            }
        }
        c.setSessionData(questions[c.getAllSessionData().size()], s);
        if (c.getAllSessionData().size() == questions.length) {
            c.getForWhom().sendRawMessage(messageFin);
            GocCandid.getCandidature().newCandidature((HashMap) c.getAllSessionData(), ((Player) c.getForWhom()).getName(), ((Player) c.getForWhom()).getUniqueId().toString());
            GocCandid.getCandidature().save();
            Bukkit.broadcast(ChatColor.RED + "[" + ChatColor.GOLD +
            "Candidatures" + ChatColor.RED + "] " + ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GOLD + ((Player) c.getForWhom()).getPlayer().getName() + ChatColor.RESET + "" + ChatColor.AQUA + " a terminé sa candidature.", "goccandid.staff");
            return END_OF_CONVERSATION;
        } else {
            return new ConvPrompt();
        }
    }
}
