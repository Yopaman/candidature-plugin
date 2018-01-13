/*
 * @author Yopaman
 */

package fr.yopaman.goccandid.files;

import fr.yopaman.goccandid.GocCandid;

public class Candidatures extends AbstractFile {
    public Candidatures(GocCandid main) {
        super(main, "candidatures.yml");
    }

    public void newCandidature(String[] responses) {
        config.set(responses[responses.length - 2] + ".Pseudo", responses[responses.length - 1]);

        for (int i = 0; i < responses.length - 2; i++) {
            config.set(responses[responses.length - 2] + "." + "Question " + (i+1), responses[i]);
        }
    }
}
