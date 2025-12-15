package eco.kosova.application.handlers;

import eco.kosova.application.commands.DeleteContainerCommand;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.ZoneRepository;

import java.util.logging.Logger;

/**
 * Handler për DeleteContainerCommand.
 */
public class DeleteContainerHandler {
    
    private static final Logger logger = Logger.getLogger(
        DeleteContainerHandler.class.getName()
    );
    
    private final KontenierRepository kontenierRepository;
    private final ZoneRepository zoneRepository;
    
    public DeleteContainerHandler(
            KontenierRepository kontenierRepository,
            ZoneRepository zoneRepository
    ) {
        this.kontenierRepository = kontenierRepository;
        this.zoneRepository = zoneRepository;
    }
    
    public void handle(DeleteContainerCommand command) {
        logger.info(String.format("Deleting container: %s", command.getContainerId()));
        
        // Gjen kontejnerin
        if (!kontenierRepository.existsById(command.getContainerId())) {
            throw new IllegalArgumentException(
                String.format("Container not found: %s", command.getContainerId())
            );
        }
        
        // Gjen zonën që përmban këtë kontejner dhe hiq kontejnerin prej saj
        var container = kontenierRepository.findById(command.getContainerId()).orElse(null);
        if (container != null) {
            Zone zone = zoneRepository.findById(container.getZoneId()).orElse(null);
            if (zone != null) {
                zone.removeContainer(command.getContainerId());
                zoneRepository.save(zone);
            }
        }
        
        // Fshin kontejnerin
        boolean deleted = kontenierRepository.deleteById(command.getContainerId());
        
        if (!deleted) {
            throw new IllegalStateException(
                String.format("Failed to delete container: %s", command.getContainerId())
            );
        }
        
        logger.info(String.format(
            "Container deleted successfully: %s",
            command.getContainerId()
        ));
    }
}

