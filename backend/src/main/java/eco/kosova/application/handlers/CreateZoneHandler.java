package eco.kosova.application.handlers;

import eco.kosova.application.commands.CreateZoneCommand;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.repositories.ZoneRepository;

import java.util.logging.Logger;

/**
 * Handler për CreateZoneCommand.
 */
public class CreateZoneHandler {
    
    private static final Logger logger = Logger.getLogger(
        CreateZoneHandler.class.getName()
    );
    
    private final ZoneRepository zoneRepository;
    
    public CreateZoneHandler(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }
    
    public void handle(CreateZoneCommand command) {
        logger.info(String.format(
            "Creating new zone: %s (%s)",
            command.getName(), command.getZoneId()
        ));
        
        // Kontrollo nëse zona ekziston tashmë
        if (zoneRepository.existsById(command.getZoneId())) {
            throw new IllegalArgumentException(
                String.format("Zone already exists: %s", command.getZoneId())
            );
        }
        
        // Krijo Coordinates value object
        Coordinates centerPoint = new Coordinates(
            command.getLatitude(),
            command.getLongitude()
        );
        
        // Krijo Zone aggregate
        Zone zone = new Zone(
            command.getZoneId(),
            command.getName(),
            centerPoint,
            command.getMunicipality()
        );
        
        // Vendos përshkrim nëse ka
        if (command.getDescription() != null && !command.getDescription().isBlank()) {
            zone.setDescription(command.getDescription());
        }
        
        // Ruan
        zoneRepository.save(zone);
        
        logger.info(String.format(
            "Zone created successfully: %s",
            command.getZoneId()
        ));
    }
}