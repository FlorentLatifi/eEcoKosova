package eco.kosova.application.handlers;

import eco.kosova.application.commands.EmptyContainerCommand;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.services.WasteMonitoringService;

import java.util.logging.Logger;

/**
 * Handler për EmptyContainerCommand.
 */
public class EmptyContainerHandler {
    
    private static final Logger logger = Logger.getLogger(
        EmptyContainerHandler.class.getName()
    );
    
    private final KontenierRepository kontenierRepository;
    private final WasteMonitoringService monitoringService;
    
    public EmptyContainerHandler(
            KontenierRepository kontenierRepository,
            WasteMonitoringService monitoringService
    ) {
        this.kontenierRepository = kontenierRepository;
        this.monitoringService = monitoringService;
    }
    
    public void handle(EmptyContainerCommand command) {
        logger.info(String.format(
            "Emptying container %s",
            command.getContainerId()
        ));
        
        Kontenier container = kontenierRepository.findById(command.getContainerId())
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Container not found: %s", command.getContainerId())
            ));
        
        // Zbraz kontejnerin
        container.empty();
        
        // Ruan
        kontenierRepository.save(container);
        
        // Përditëso zone status
        monitoringService.updateZoneStatus(container.getZoneId());
        
        logger.info(String.format(
            "Container %s emptied successfully",
            command.getContainerId()
        ));
    }
}