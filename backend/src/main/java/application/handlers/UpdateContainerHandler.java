package eco.kosova.application.handlers;

import eco.kosova.application.commands.UpdateContainerCommand;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.models.valueobjects.Address;
import eco.kosova.domain.models.valueobjects.ContainerType;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.ZoneRepository;

import java.util.logging.Logger;

/**
 * Handler për UpdateContainerCommand.
 */
public class UpdateContainerHandler {
    
    private static final Logger logger = Logger.getLogger(
        UpdateContainerHandler.class.getName()
    );
    
    private final KontenierRepository kontenierRepository;
    private final ZoneRepository zoneRepository;
    
    public UpdateContainerHandler(
            KontenierRepository kontenierRepository,
            ZoneRepository zoneRepository
    ) {
        this.kontenierRepository = kontenierRepository;
        this.zoneRepository = zoneRepository;
    }
    
    public void handle(UpdateContainerCommand command) {
        logger.info(String.format("Updating container: %s", command.getId()));
        
        // Gjen kontejnerin
        Kontenier kontenier = kontenierRepository.findById(command.getId())
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Container not found: %s", command.getId())
            ));
        
        // Përditëso zoneId nëse ka ndryshuar
        if (command.getZoneId() != null && !command.getZoneId().equals(kontenier.getZoneId())) {
            // Hiq nga zona e vjetër
            Zone oldZone = zoneRepository.findById(kontenier.getZoneId()).orElse(null);
            if (oldZone != null) {
                oldZone.removeContainer(command.getId());
                zoneRepository.save(oldZone);
            }
            
            // Shto në zonën e re
            Zone newZone = zoneRepository.findById(command.getZoneId())
                .orElseThrow(() -> new IllegalArgumentException(
                    String.format("Zone not found: %s", command.getZoneId())
                ));
            newZone.addContainer(command.getId());
            zoneRepository.save(newZone);
            
            // Përditëso zoneId - përdor metodën ekzistuese relocateToZone
            kontenier.relocateToZone(command.getZoneId());
        }
        
        // Përditëso location
        if (command.getLatitude() != null && command.getLongitude() != null) {
            Coordinates newLocation = new Coordinates(
                command.getLatitude(),
                command.getLongitude()
            );
            kontenier.updateLocation(newLocation);
        }
        
        // Përditëso fill level nëse është specifikuar
        if (command.getFillLevel() != null) {
            kontenier.updateFillLevel(command.getFillLevel());
        }
        
        // Përditëso operational status
        if (command.getOperational() != null) {
            if (command.getOperational() && !kontenier.isOperational()) {
                kontenier.reactivate();
            } else if (!command.getOperational() && kontenier.isOperational()) {
                kontenier.markUnderMaintenance();
            }
        }
        
        // Ruan
        kontenierRepository.save(kontenier);
        
        logger.info(String.format(
            "Container updated successfully: %s",
            command.getId()
        ));
    }
    
    private ContainerType getContainerTypeFromString(String type) {
        for (ContainerType ct : ContainerType.values()) {
            if (ct.getDisplayName().equalsIgnoreCase(type) || 
                ct.name().equalsIgnoreCase(type)) {
                return ct;
            }
        }
        throw new IllegalArgumentException(
            String.format("Invalid container type: %s", type)
        );
    }
}

