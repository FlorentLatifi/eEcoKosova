package eco.kosova.infrastructure.events;

import eco.kosova.domain.events.DomainEvent;
import eco.kosova.domain.events.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Implementation e DomainEventPublisher që përdor Spring's ApplicationEventPublisher.
 * 
 * Kjo është bridge midis domain events dhe Spring event system.
 */
@Component
public class DomainEventPublisherImpl implements DomainEventPublisher {
    
    private static final Logger logger = Logger.getLogger(
        DomainEventPublisherImpl.class.getName()
    );
    
    private final ApplicationEventPublisher springEventPublisher;
    
    public DomainEventPublisherImpl(ApplicationEventPublisher springEventPublisher) {
        this.springEventPublisher = springEventPublisher;
    }
    
    @Override
    public void publish(DomainEvent event) {
        logger.info(String.format(
            "Publishing domain event: %s [ID: %s, Aggregate: %s]",
            event.getEventType(),
            event.getEventId(),
            event.getAggregateId()
        ));
        
        // Publiko event në Spring event system
        springEventPublisher.publishEvent(event);
        
        logger.fine(String.format(
            "Domain event published successfully: %s",
            event.getEventId()
        ));
    }
}