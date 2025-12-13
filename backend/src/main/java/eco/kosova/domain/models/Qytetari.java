package eco.kosova.domain.models;

import eco.kosova.domain.models.valueobjects.Address;
import java.time.Instant;
import java.util.Objects;

/**
 * Domain entity: Qytetari (Citizen)
 */
public class Qytetari {
    private final String qytetariID;
    private String emri;
    private Address adresa;
    private final Instant createdAt;

    public Qytetari(String qytetariID, String emri, Address adresa) {
        if (qytetariID == null || qytetariID.isBlank()) throw new IllegalArgumentException("qytetariID cannot be empty");
        this.qytetariID = qytetariID;
        this.emri = Objects.requireNonNull(emri, "emri cannot be null");
        this.adresa = Objects.requireNonNull(adresa, "adresa cannot be null");
        this.createdAt = Instant.now();
    }

    public String getQytetariID() { return qytetariID; }
    public String getEmri() { return emri; }
    public void setEmri(String emri) { this.emri = Objects.requireNonNull(emri); }
    public Address getAdresa() { return adresa; }
    public void setAdresa(Address adresa) { this.adresa = Objects.requireNonNull(adresa); }
    public Instant getCreatedAt() { return createdAt; }
}
