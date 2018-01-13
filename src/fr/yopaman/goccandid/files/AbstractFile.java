/*
 * @author Yopaman
 */

package fr.yopaman.goccandid.files;

import fr.yopaman.goccandid.GocCandid;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AbstractFile {

    protected GocCandid main;
    private File file;
    protected FileConfiguration config;

    public AbstractFile(GocCandid main, String fileName) {
        this.main = main;
        this.file = new File(main.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                main.getLogger().info("Creation d'un fichier");
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        main.getLogger().info("Chargement d'un fichier");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
