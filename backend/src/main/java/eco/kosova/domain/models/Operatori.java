package eco.kosova.domain.models;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain entity: Operatori (operator/worker)
 */
public class Operatori {
    private final String operatoriID;
    private String emri;
    private String phone;
    private final Instant createdAt;

    public Operatori(String operatoriID, String emri, String phone) {
        if (operatoriID == null || operatoriID.isBlank()) throw new IllegalArgumentException("operatoriID cannot be empty");
        this.operatoriID = operatoriID;
        this.emri = Objects.requireNonNull(emri, "emri cannot be null");
        this.phone = phone;
        this.createdAt = Instant.now();
    }

    public String getOperatoriID() { return operatoriID; }
    public String getEmri() { return emri; }
    public void setEmri(String emri) { this.emri = Objects.requireNonNull(emri); }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Instant getCreatedAt() { return createdAt; }
}
