package eco.kosova.domain.events;

import java.util.Collection;

/**
 * Interface për publikimin e Domain Events.
 * 
 * Ky interface deklarohet në Domain Layer, por implementimi
 * është në Infrastructure Layer. Kjo lejon Domain Layer të
 * mbetet i pavarur nga detajet e infrastrukturës.
 * 
 * Pattern: Dependency Inversion Principle (SOLID)
 */
public interface DomainEventPublisher {
    
    /**
     * Publikon një Domain Event te të gjithë subscriber-at e regjistruar.
     * 
     * @param event Eventi që do të publikohet
     */
    void publish(DomainEvent event);
    
    /**
     * Publikon një koleksion të eventeve.
     * Useful për batch processing kur një aggregate gjeneron shumë events.
     * 
     * @param events Koleksioni i eventeve
     */
    default void publishAll(Collection<DomainEvent> events) {
        if (events != null && !events.isEmpty()) {
            events.forEach(this::publish);
        }
    }
}