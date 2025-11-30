package eco.kosova.domain.models;

import eco.kosova.domain.events.ContainerFullEvent;
import eco.kosova.domain.models.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests për Kontenier Aggregate.
 */
class KontenierTest {
    
    private Kontenier kontenier;
    private Coordinates location;
    private Address address;
    
    @BeforeEach
    void setUp() {
        location = new Coordinates(42.6629, 21.1655);
        address = new Address("Rruga Test", "Prishtinë", "Prishtinë", "10000");
        
        kontenier = new Kontenier(
            "CONT-TEST-001",
            "ZONE-TEST-001",
            ContainerType.PLASTIC,
            1000,
            location,
            address
        );
    }
    
    @Test
    void testContainerCreation() {
        assertEquals("CONT-TEST-001", kontenier.getId());
        assertEquals("ZONE-TEST-001", kontenier.getZoneId());
        assertEquals(ContainerType.PLASTIC, kontenier.getType());
        assertEquals(1000, kontenier.getCapacity());
        assertTrue(kontenier.isOperational());
        assertEquals(0, kontenier.getFillLevel().getValue());
    }
    
    @Test
    void testUpdateFillLevel() {
        kontenier.updateFillLevel(50);
        assertEquals(50, kontenier.getFillLevel().getValue());
    }
    
    @Test
    void testUpdateFillLevel_GeneratesEventWhenCritical() {
        kontenier.updateFillLevel(95);
        
        assertFalse(kontenier.getDomainEvents().isEmpty());
        assertEquals(1, kontenier.getDomainEvents().size());
        
        var event = kontenier.getDomainEvents().iterator().next();
        assertTrue(event instanceof ContainerFullEvent);
    }
    
    @Test
    void testUpdateFillLevel_NoEventWhenNotCritical() {
        kontenier.updateFillLevel(50);
        assertTrue(kontenier.getDomainEvents().isEmpty());
    }
    
    @Test
    void testEmptyContainer() {
        kontenier.updateFillLevel(95);
        kontenier.clearDomainEvents();
        
        kontenier.empty();
        
        assertEquals(0, kontenier.getFillLevel().getValue());
        assertEquals(ContainerStatus.OPERATIONAL, kontenier.getStatus());
        assertNotNull(kontenier.getLastEmptied());
    }
    
    @Test
    void testScheduleCollection_Success() {
        kontenier.updateFillLevel(95);
        kontenier.clearDomainEvents();
        
        Instant scheduledTime = Instant.now().plusSeconds(3600);
        kontenier.scheduleCollection(scheduledTime);
        
        assertEquals(ContainerStatus.SCHEDULED_FOR_COLLECTION, kontenier.getStatus());
        assertFalse(kontenier.getDomainEvents().isEmpty());
    }
    
    @Test
    void testScheduleCollection_FailsWhenUnderMaintenance() {
        kontenier.markUnderMaintenance();
        
        assertThrows(IllegalStateException.class, () -> {
            kontenier.scheduleCollection(Instant.now().plusSeconds(3600));
        });
    }
    
    @Test
    void testMarkUnderMaintenance() {
        kontenier.markUnderMaintenance();
        
        assertEquals(ContainerStatus.UNDER_MAINTENANCE, kontenier.getStatus());
        assertFalse(kontenier.isOperational());
    }
    
    @Test
    void testReactivate() {
        kontenier.markUnderMaintenance();
        kontenier.reactivate();
        
        assertEquals(ContainerStatus.OPERATIONAL, kontenier.getStatus());
        assertTrue(kontenier.isOperational());
    }
    
    @Test
    void testNeedsUrgentCollection() {
        kontenier.updateFillLevel(95);
        assertTrue(kontenier.needsUrgentCollection());
        
        kontenier.empty();
        assertFalse(kontenier.needsUrgentCollection());
    }
}