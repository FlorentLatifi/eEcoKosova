package eco.kosova.application.handlers;

import eco.kosova.application.commands.DeleteZoneCommand;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.ZoneRepository;

import java.util.logging.Logger;

/**
 * Handler për DeleteZoneCommand.
 */
public class DeleteZoneHandler {
    
    private static final Logger logger = Logger.getLogger(
        DeleteZoneHandler.class.getName()
    );
    
    private final ZoneRepository zoneRepository;
    private final KontenierRepository kontenierRepository;
    
    public DeleteZoneHandler(
            ZoneRepository zoneRepository,
            KontenierRepository kontenierRepository
    ) {
        this.zoneRepository = zoneRepository;
        this.kontenierRepository = kontenierRepository;
    }
    
    public void handle(DeleteZoneCommand command) {
        logger.info(String.format("Deleting zone: %s", command.getZoneId()));
        
        // Gjen zonën
        Zone zone = zoneRepository.findById(command.getZoneId())
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Zone not found: %s", command.getZoneId())
            ));
        
        // Kontrollo nëse ka kontejnerë në këtë zonë
        long containerCount = kontenierRepository.countByZoneId(command.getZoneId());
        if (containerCount > 0) {
            throw new IllegalStateException(
                String.format(
                    "Cannot delete zone %s: it contains %d containers. Please remove containers first.",
                    command.getZoneId(), containerCount
                )
            );
        }
        
        // Fshin zonën
        boolean deleted = zoneRepository.deleteById(command.getZoneId());
        
        if (!deleted) {
            throw new IllegalStateException(
                String.format("Failed to delete zone: %s", command.getZoneId())
            );
        }
        
        logger.info(String.format(
            "Zone deleted successfully: %s",
            command.getZoneId()
        ));
    }
}

