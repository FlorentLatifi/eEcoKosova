package eco.kosova.application.handlers;

import eco.kosova.application.commands.UpdateContainerFillLevelCommand;
import eco.kosova.domain.services.WasteMonitoringService;

import java.util.logging.Logger;

/**
 * Handler për UpdateContainerFillLevelCommand.
 * 
 * Command Handlers:
 * - Koordinojnë domain services dhe repositories
 * - Nuk përmbajnë business logic (që është në Domain Layer)
 * - Publikojnë domain events
 */
public class UpdateContainerFillLevelHandler {
    
    private static final Logger logger = Logger.getLogger(
        UpdateContainerFillLevelHandler.class.getName()
    );
    
    private final WasteMonitoringService monitoringService;
    
    public UpdateContainerFillLevelHandler(WasteMonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }
    
    /**
     * Ekzekuton komandën për përditësimin e fill level.
     * 
     * @param command Komanda që do të ekzekutohet
     */
    public void handle(UpdateContainerFillLevelCommand command) {
        logger.info(String.format(
            "Handling UpdateContainerFillLevelCommand for container %s: %d%%",
            command.getContainerId(), command.getFillLevel()
        ));
        
        try {
            // Delego logjikën në Domain Service
            monitoringService.updateContainerFillLevel(
                command.getContainerId(),
                command.getFillLevel()
            );
            
            logger.info(String.format(
                "Successfully updated fill level for container %s",
                command.getContainerId()
            ));
            
        } catch (IllegalArgumentException e) {
            logger.warning(String.format(
                "Failed to update container %s: %s",
                command.getContainerId(), e.getMessage()
            ));
            throw e;
        }
    }
}