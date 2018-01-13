/*
 * @author Yopaman
 */

package fr.yopaman.goccandid.files;

import fr.yopaman.goccandid.GocCandid;

public class Config extends AbstractFile {
    public Config(GocCandid main) {
        super(main, "config.yml");
    }

    public String[] getConfig() {
        return config.getStringList("questions").toArray(new String[config.getStringList("questions").size()]);
    }
}
