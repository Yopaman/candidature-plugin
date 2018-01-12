/*
 * @author Yopaman
 */

package fr.yopaman.goccandid.files;

import fr.yopaman.goccandid.GocCandid;

public class Candidatures extends AbstractFile {
    public Candidatures(GocCandid main) {
        super(main, "candidatures.yml");
    }

    public void newCandidature() {
        config.set("test", "test");
    }
}
