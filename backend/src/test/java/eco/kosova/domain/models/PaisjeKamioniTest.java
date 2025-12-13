package eco.kosova.domain.models;

import eco.kosova.domain.models.valueobjects.ContainerStatus;
import eco.kosova.domain.models.valueobjects.Coordinates;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class PaisjeKamioniTest {

    @Test
    public void kamioniInheritanceAndAvailability() {
        Coordinates coords = new Coordinates(42.66, 21.16);
        Kamioni kamioni = new Kamioni(
                "kam-1",
                "Kamioni-Model",
                ContainerStatus.OPERATIONAL,
                coords,
                Instant.now(),
                "AA-123-BB",
                5000,
                "oper-1"
        );

        assertEquals("kam-1", kamioni.getPaisjeID());
        assertTrue(kamioni.isAvailable());

        // assign to route changes status to SCHEDULED_FOR_COLLECTION
        kamioni.assignToRoute("route-1");
        assertEquals(ContainerStatus.SCHEDULED_FOR_COLLECTION, kamioni.getStatus());
        assertFalse(kamioni.isAvailable());
    }
}
