package eco.kosova.infrastructure.events;

import eco.kosova.domain.events.CollectionScheduledEvent;
import eco.kosova.infrastructure.services.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Event Handler për CollectionScheduledEvent.
 */
@Component
public class CollectionScheduledEventHandler {
    
    private static final Logger logger = Logger.getLogger(
        CollectionScheduledEventHandler.class.getName()
    );
    
    private final NotificationService notificationService;
    
    public CollectionScheduledEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @Async
    @EventListener
    public void handle(CollectionScheduledEvent event) {
        logger.info(String.format(
            "Handling CollectionScheduledEvent: container=%s, zone=%s, scheduled=%s",
            event.getContainerId(),
            event.getZoneId(),
            event.getScheduledTime()
        ));
        
        try {
            // Dërgo njoftim për operatorët
            notificationService.sendCollectionScheduledNotification(
                event.getContainerId(),
                event.getZoneId(),
                event.getScheduledTime()
            );
            
            // Log
            logger.info(String.format(
                "📅 Collection scheduled for container %s at %s",
                event.getContainerId(),
                event.getScheduledTime()
            ));
            
        } catch (Exception e) {
            logger.severe(String.format(
                "Error handling CollectionScheduledEvent: %s",
                e.getMessage()
            ));
        }
    }
}
