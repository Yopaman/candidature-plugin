/*
 * @author Yopaman
 */

package fr.yopaman.goccandid.files;

import fr.yopaman.goccandid.GocCandid;

public class Config extends AbstractFile {
    public Config(GocCandid main) {
        super(main, "config.yml");
    }

    public String[] getQuestions() {
        return config.getStringList("questions").toArray(new String[config.getStringList("questions").size()]);
    }

    public String getMessageDebut() {
        return config.getString("messages.debut");
    }

    public String getMessageFin() {
        return config.getString("messages.fin");
    }

    public String[] getInsultes() {
        return config.getStringList("insultes").toArray(new String[config.getStringList("insultes").size()]);
    }

    public String getMysqlUrl() {
        return config.getString("mysql.url");
    }

    public String getMysqlUser() {
        return config.getString("mysql.user");
    }

    public String getMysqlPassword() {
        return config.getString("mysql.password");
    }
}
