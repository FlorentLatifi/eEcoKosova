package eco.kosova.domain.models;

import java.util.Objects;

/**
 * Simple representation of a control panel / UI terminal that a citizen/operator uses.
 */
public class KontrollPanel {

    private final String panelID;
    private String gjuha;
    private String temat;
    private String gjendjaEkranit;

    public KontrollPanel(String panelID, String gjuha, String temat, String gjendjaEkranit) {
        if (panelID == null || panelID.isBlank()) throw new IllegalArgumentException("panelID cannot be empty");
        this.panelID = panelID;
        this.gjuha = Objects.requireNonNull(gjuha, "gjuha cannot be null");
        this.temat = Objects.requireNonNull(temat, "temat cannot be null");
        this.gjendjaEkranit = Objects.requireNonNull(gjendjaEkranit, "gjendjaEkranit cannot be null");
    }

    public String getPanelID() { return panelID; }
    public String getGjuha() { return gjuha; }
    public void setGjuha(String gjuha) { this.gjuha = gjuha; }
    public String getTemat() { return temat; }
    public void setTemat(String temat) { this.temat = temat; }
    public String getGjendjaEkranit() { return gjendjaEkranit; }
    public void setGjendjaEkranit(String gjendjaEkranit) { this.gjendjaEkranit = gjendjaEkranit; }
}
