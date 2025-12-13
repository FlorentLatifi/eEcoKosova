package eco.kosova.domain.models;

import eco.kosova.domain.models.valueobjects.ContainerStatus;
import eco.kosova.domain.models.valueobjects.Coordinates;

import java.time.Instant;
import java.util.Objects;

/**
 * Abstract base class for all physical devices (Paisje) in the system.
 */
public abstract class Paisje {

    protected final String paisjeID;
    protected String emri;
    protected ContainerStatus status;
    protected Coordinates vendndodhja;
    protected Instant dataInstalimit;

    public Paisje(String paisjeID, String emri, ContainerStatus status, Coordinates vendndodhja, Instant dataInstalimit) {
        if (paisjeID == null || paisjeID.isBlank()) throw new IllegalArgumentException("paisjeID cannot be empty");
        this.paisjeID = paisjeID;
        this.emri = Objects.requireNonNull(emri, "emri cannot be null");
        this.status = Objects.requireNonNull(status, "status cannot be null");
        this.vendndodhja = Objects.requireNonNull(vendndodhja, "vendndodhja cannot be null");
        this.dataInstalimit = dataInstalimit == null ? Instant.now() : dataInstalimit;
    }

    public String getPaisjeID() { return paisjeID; }

    public String getEmri() { return emri; }

    public ContainerStatus getStatus() { return status; }

    public Coordinates getVendndodhja() { return vendndodhja; }

    public Instant getDataInstalimit() { return dataInstalimit; }

    public void updateStatus(ContainerStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus);
        this.dataInstalimit = Instant.now();
    }

}
