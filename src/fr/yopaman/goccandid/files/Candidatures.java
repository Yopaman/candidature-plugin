/*
 * @author Yopaman
 */

package fr.yopaman.goccandid.files;

import fr.yopaman.goccandid.GocCandid;

public class Candidatures extends AbstractFile {
    public Candidatures(GocCandid main) {
        super(main, "candidatures.yml");
    }

    public void newCandidature(String prenom, String age, String tempsMc, String specialite, String uuid, String pseudo) {
        config.set(uuid + "." + "pseudo", pseudo);
        config.set(uuid + "." + "prenom", prenom);
        config.set(uuid + "." + "age", age);
        config.set(uuid + "." + "joue depuis", tempsMc);
        config.set(uuid + "." + "specialite", specialite);
    }
}
