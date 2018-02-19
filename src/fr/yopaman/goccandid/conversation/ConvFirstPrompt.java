package fr.yopaman.goccandid.conversation;

import fr.yopaman.goccandid.GocCandid;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class ConvFirstPrompt extends StringPrompt {

    public Boolean withFirestMessage;

    public String messageDebut = GocCandid.getMyConfig().getMessageDebut();
    public String questions[] = GocCandid.getMyConfig().getQuestions();
    public CharSequence insultes[] = GocCandid.getMyConfig().getInsultes();

    public ConvFirstPrompt(Boolean wfm) {
        super();
        this.withFirestMessage = wfm;
    }

    @Override
    public String getPromptText(ConversationContext c) {
        if (withFirestMessage == true) {
            c.getForWhom().sendRawMessage(messageDebut + "\n");
        }
        return ChatColor.BOLD + "" + ChatColor.BLUE + "" + questions[0] + ChatColor.RESET + "" + ChatColor.GRAY + " (Répondez directement dans le chat)";
    }

    @Override
    public Prompt acceptInput(ConversationContext c, String s) {
        for (int i = 0; i < insultes.length; i++) {
            if (s.contains(insultes[i])) {
                c.getForWhom().sendRawMessage(ChatColor.RED + "Votre message continent le mot " + ChatColor.GOLD + ChatColor.ITALIC + insultes[i] + ChatColor.RESET + ChatColor.RED + " qui est interdit. Veuillez le réécrire en supprimant ce mot.");
                return new ConvFirstPrompt(false);
            }
        }
        c.setSessionData(questions[0], s);
        return new ConvPrompt();
    }
}
