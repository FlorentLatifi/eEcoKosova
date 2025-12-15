package eco.kosova.application.handlers;

import eco.kosova.application.commands.ScheduleCollectionCommand;
import eco.kosova.domain.events.DomainEventPublisher;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.repositories.KontenierRepository;

import java.util.logging.Logger;

/**
 * Handler për ScheduleCollectionCommand.
 */
public class ScheduleCollectionHandler {
    
    private static final Logger logger = Logger.getLogger(
        ScheduleCollectionHandler.class.getName()
    );
    
    private final KontenierRepository kontenierRepository;
    private final DomainEventPublisher eventPublisher;
    
    public ScheduleCollectionHandler(
            KontenierRepository kontenierRepository,
            DomainEventPublisher eventPublisher
    ) {
        this.kontenierRepository = kontenierRepository;
        this.eventPublisher = eventPublisher;
    }
    
    public void handle(ScheduleCollectionCommand command) {
        logger.info(String.format(
            "Scheduling collection for container %s at %s",
            command.getContainerId(), command.getScheduledTime()
        ));
        
        Kontenier container = kontenierRepository.findById(command.getContainerId())
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Container not found: %s", command.getContainerId())
            ));
        
        // Thirr domain method (që gjeneron events)
        container.scheduleCollection(command.getScheduledTime());
        
        // Publiko domain events
        eventPublisher.publishAll(container.getDomainEvents());
        container.clearDomainEvents();
        
        // Ruan aggregate
        kontenierRepository.save(container);
        
        logger.info(String.format(
            "Collection scheduled successfully for container %s",
            command.getContainerId()
        ));
    }
}