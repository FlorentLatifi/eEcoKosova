package eco.kosova.application.handlers;

import eco.kosova.application.commands.UpdateZoneCommand;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.repositories.ZoneRepository;

import java.util.logging.Logger;

/**
 * Handler për UpdateZoneCommand.
 */
public class UpdateZoneHandler {
    
    private static final Logger logger = Logger.getLogger(
        UpdateZoneHandler.class.getName()
    );
    
    private final ZoneRepository zoneRepository;
    
    public UpdateZoneHandler(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }
    
    public void handle(UpdateZoneCommand command) {
        logger.info(String.format("Updating zone: %s", command.getZoneId()));
        
        // Gjen zonën
        Zone zone = zoneRepository.findById(command.getZoneId())
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Zone not found: %s", command.getZoneId())
            ));
        
        // Përditëso name
        if (command.getName() != null && !command.getName().isBlank()) {
            zone.rename(command.getName());
        }
        
        // Përditëso center point
        if (command.getLatitude() != null && command.getLongitude() != null) {
            Coordinates newCenterPoint = new Coordinates(
                command.getLatitude(),
                command.getLongitude()
            );
            zone.updateCenterPoint(newCenterPoint);
        }
        
        // Përditëso description
        if (command.getDescription() != null) {
            zone.setDescription(command.getDescription());
        }
        
        // Ruan
        zoneRepository.save(zone);
        
        logger.info(String.format(
            "Zone updated successfully: %s",
            command.getZoneId()
        ));
    }
}

