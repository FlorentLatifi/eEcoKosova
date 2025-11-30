package eco.kosova.infrastructure.services;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.logging.Logger;

/**
 * Service për dërgimin e njoftimeve.
 */
@Service
public class NotificationService {
    
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());
    
    /**
     * Dërgon një alarm kritik kur një kontejner është plot.
     */
    public void sendCriticalAlert(String containerId, String zoneId, int fillLevel) {
        String message = String.format(
            "🚨 ALERT: Container %s në zonë %s ka arritur %d%% kapacitet!",
            containerId, zoneId, fillLevel
        );
        logger.warning(message);
        // Në realitet, këtu do të dërgohej email/SMS për operatorët
    }
    
    /**
     * Dërgon njoftim kur është planifikuar mbledhje.
     */
    public void sendCollectionScheduledNotification(String containerId, String zoneId, Instant scheduledTime) {
        String message = String.format(
            "📅 Collection scheduled: Container %s në zonë %s do të mbushet më %s",
            containerId, zoneId, scheduledTime
        );
        logger.info(message);
        // Në realitet, këtu do të dërgohej email/SMS për operatorët
    }
}
