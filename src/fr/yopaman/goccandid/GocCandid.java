/*
 * @author Yopaman
 */

package fr.yopaman.goccandid;

import fr.yopaman.goccandid.files.Candidatures;
import fr.yopaman.goccandid.files.Config;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class GocCandid extends JavaPlugin {

    private static Plugin plugin;
    private static Candidatures candidatures;
    private static Config config;

    @Override
    public void onEnable() {
        loadDb();
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getCommand("candidature").setExecutor(new CommandCandidature());
    }

    @Override
    public void onDisable() {
        candidatures.save();
    }

    private void loadDb() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            this.candidatures = new Candidatures(this);
            this.config = new Config(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static Candidatures getCandidature() {
        return candidatures;
    }

    public static Config getMyConfig() {
        return config;
    }
}
