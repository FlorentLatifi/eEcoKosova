package eco.kosova.domain.events;

import java.time.Instant;

/**
 * Interface bazë për të gjitha Domain Events.
 * Domain Events përdoren për të komunikuar ndryshime të rëndësishme
 * që ndodhin në domain, pa krijuar coupling të fortë midis komponentëve.
 */
public interface DomainEvent {
    
    /**
     * Identifikues unik i eventit
     */
    String getEventId();
    
    /**
     * Koha kur ndodhi eventi
     */
    Instant occurredOn();
    
    /**
     * ID e aggregate që gjeneroi eventin
     */
    String getAggregateId();
    
    /**
     * Lloji i eventit (për logging dhe debugging)
     */
    default String getEventType() {
        return this.getClass().getSimpleName();
    }
}