package eco.kosova.infrastructure.events;

import eco.kosova.domain.events.ContainerFullEvent;
import eco.kosova.domain.services.RouteOptimizationService;
import eco.kosova.infrastructure.services.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Event Handler për ContainerFullEvent.
 * 
 * Kur një kontejner mbushet:
 * 1. Dërgon njoftime
 * 2. Përditëson route optimization
 * 3. Log event për audit
 */
@Component
public class ContainerFullEventHandler {
    
    private static final Logger logger = Logger.getLogger(
        ContainerFullEventHandler.class.getName()
    );
    
    private final NotificationService notificationService;
    private final RouteOptimizationService routeService;
    
    public ContainerFullEventHandler(
            NotificationService notificationService,
            RouteOptimizationService routeService
    ) {
        this.notificationService = notificationService;
        this.routeService = routeService;
    }
    
    /**
     * Handle ContainerFullEvent - ekzekutohet asinkronisht
     */
    @Async
    @EventListener
    public void handle(ContainerFullEvent event) {
        logger.info(String.format(
            "Handling ContainerFullEvent: container=%s, zone=%s, level=%d%%",
            event.getContainerId(),
            event.getZoneId(),
            event.getFillLevel()
        ));
        
        try {
            // 1. Dërgo njoftime të menjëhershme
            notificationService.sendCriticalAlert(
                event.getContainerId(),
                event.getZoneId(),
                event.getFillLevel()
            );
            
            // 2. Log për audit trail
            logEvent(event);
            
            logger.info(String.format(
                "Successfully handled ContainerFullEvent for container %s",
                event.getContainerId()
            ));
            
        } catch (Exception e) {
            logger.severe(String.format(
                "Error handling ContainerFullEvent: %s",
                e.getMessage()
            ));
        }
    }
    
    private void logEvent(ContainerFullEvent event) {
        logger.warning(String.format(
            "📊 AUDIT: Container %s in zone %s reached %d%% capacity at %s [Priority: %s]",
            event.getContainerId(),
            event.getZoneId(),
            event.getFillLevel(),
            event.occurredOn(),
            event.getPriority()
        ));
    }
}