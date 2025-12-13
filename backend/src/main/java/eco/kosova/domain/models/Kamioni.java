package eco.kosova.domain.models;

import eco.kosova.domain.models.valueobjects.ContainerStatus;
import eco.kosova.domain.models.valueobjects.ContainerType;
import eco.kosova.domain.models.valueobjects.Coordinates;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregated device representing a collection truck.
 */
public class Kamioni extends Paisje {

    private final String targa;
    private final int kapaciteti; // in liters
    private String operatoriID;

    public Kamioni(
            String kamioniID,
            String emri,
            ContainerStatus status,
            Coordinates vendndodhja,
            Instant dataInstalimit,
            String targa,
            int kapaciteti,
            String operatoriID
    ) {
        super(kamioniID, emri, status, vendndodhja, dataInstalimit);
        if (targa == null || targa.isBlank()) throw new IllegalArgumentException("targa cannot be empty");
        if (kapaciteti <= 0) throw new IllegalArgumentException("kapaciteti must be positive");
        this.targa = targa;
        this.kapaciteti = kapaciteti;
        this.operatoriID = operatoriID;
    }

    public String getTarga() { return targa; }

    public int getKapaciteti() { return kapaciteti; }

    public String getOperatoriID() { return operatoriID; }

    public void setOperatoriID(String operatoriID) { this.operatoriID = operatoriID; }

    public boolean isAvailable() {
        // A truck is available if it is operational (reuse ContainerStatus semantics)
        return this.status == ContainerStatus.OPERATIONAL;
    }

    public void assignToRoute(String routeId) {
        if (routeId == null || routeId.isBlank()) throw new IllegalArgumentException("routeId cannot be empty");
        // In this simple implementation we just mark the truck as scheduled for collection-like status
        this.updateStatus(ContainerStatus.SCHEDULED_FOR_COLLECTION);
    }
}
