package fr.yopaman.goccandid;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Goccandid extends JavaPlugin {

    @Override
    public void onLoad() {
        getLogger().info("Plugin de candidatures démarré !");
    }

    @Override
    public void onDisable() {
    	
    }

    @Override
    public void onEnable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("candidature")) {
            return true;
        }
        return false;
    }
}
