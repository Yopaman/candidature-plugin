package fr.yopaman.goccandid.conversation;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class ConvPrefix implements ConversationPrefix {
    @Override
    public String getPrefix(ConversationContext conversationContext) {
        return ChatColor.RED + "[" + ChatColor.GOLD + "Candidature" + ChatColor.RED + "] " + ChatColor.RESET;
    }
}
