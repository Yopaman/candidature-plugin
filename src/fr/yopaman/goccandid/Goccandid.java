package fr.yopaman.goccandid;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Goccandid extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("candidature").setExecutor(new CommandCandidature());
    }

}
