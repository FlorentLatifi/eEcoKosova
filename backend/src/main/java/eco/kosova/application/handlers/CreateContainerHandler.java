package eco.kosova.application.handlers;

import eco.kosova.application.commands.CreateContainerCommand;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.models.valueobjects.Address;
import eco.kosova.domain.models.valueobjects.ContainerType;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.ZoneRepository;

import java.util.logging.Logger;

/**
 * Handler për CreateContainerCommand.
 */
public class CreateContainerHandler {
    
    private static final Logger logger = Logger.getLogger(
        CreateContainerHandler.class.getName()
    );
    
    private final KontenierRepository kontenierRepository;
    private final ZoneRepository zoneRepository;
    
    public CreateContainerHandler(
            KontenierRepository kontenierRepository,
            ZoneRepository zoneRepository
    ) {
        this.kontenierRepository = kontenierRepository;
        this.zoneRepository = zoneRepository;
    }
    
    public void handle(CreateContainerCommand command) {
        logger.info(String.format(
            "Creating new container: %s in zone %s",
            command.getId(), command.getZoneId()
        ));
        
        // Kontrollo nëse kontejneri ekziston tashmë
        if (kontenierRepository.existsById(command.getId())) {
            throw new IllegalArgumentException(
                String.format("Container already exists: %s", command.getId())
            );
        }
        
        // Kontrollo nëse zona ekziston
        Zone zone = zoneRepository.findById(command.getZoneId())
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Zone not found: %s", command.getZoneId())
            ));
        
        // Konverto ContainerType nga string në enum
        ContainerType containerType = getContainerTypeFromString(command.getType());
        
        // Krijo value objects
        Coordinates location = new Coordinates(command.getLatitude(), command.getLongitude());
        Address address = new Address(
            command.getStreet(),
            command.getCity(),
            command.getMunicipality(),
            command.getPostalCode()
        );
        
        // Krijo kontejnerin
        Kontenier kontenier = new Kontenier(
            command.getId(),
            command.getZoneId(),
            containerType,
            command.getCapacity(),
            location,
            address
        );
        
        // Vendos operational status
        if (!command.isOperational()) {
            kontenier.markAsNonOperational("Manually set as non-operational");
        }
        
        // Ruan kontejnerin
        kontenierRepository.save(kontenier);
        
        // Shto kontejnerin në zonë
        zone.addContainer(command.getId());
        zoneRepository.save(zone);
        
        logger.info(String.format(
            "Container created successfully: %s",
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

