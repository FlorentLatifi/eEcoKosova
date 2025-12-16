package eco.kosova.presentation.api.controllers;

import eco.kosova.application.commands.CreateContainerCommand;
import eco.kosova.application.commands.DeleteContainerCommand;
import eco.kosova.application.commands.EmptyContainerCommand;
import eco.kosova.application.commands.ScheduleCollectionCommand;
import eco.kosova.application.commands.UpdateContainerCommand;
import eco.kosova.application.handlers.CreateContainerHandler;
import eco.kosova.application.handlers.DeleteContainerHandler;
import eco.kosova.application.handlers.EmptyContainerHandler;
import eco.kosova.application.handlers.GetContainerByIdHandler;
import eco.kosova.application.handlers.GetContainersByZoneHandler;
import eco.kosova.application.handlers.ScheduleCollectionHandler;
import eco.kosova.application.handlers.UpdateContainerHandler;
import eco.kosova.application.queries.GetContainerByIdQuery;
import eco.kosova.application.queries.GetContainersByZoneQuery;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.presentation.dtos.ContainerResponseDTO;
import eco.kosova.presentation.dtos.CreateContainerRequest;
import eco.kosova.presentation.dtos.UpdateContainerRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller për menaxhimin e kontejnerëve (CRUD operations).
 */
@RestController
@RequestMapping("/api/containers")
@CrossOrigin(origins = "*")
public class ContainerManagementController {
    
    private static final Logger logger = LoggerFactory.getLogger(ContainerManagementController.class);
    
    @Autowired
    private CreateContainerHandler createHandler;
    
    @Autowired
    private UpdateContainerHandler updateHandler;
    
    @Autowired
    private DeleteContainerHandler deleteHandler;
    
    @Autowired
    private GetContainerByIdHandler getByIdHandler;
    
    @Autowired
    private GetContainersByZoneHandler getByZoneHandler;
    
    @Autowired
    private ScheduleCollectionHandler scheduleCollectionHandler;
    
    @Autowired
    private EmptyContainerHandler emptyContainerHandler;
    
    /**
     * POST /api/containers - Krijon një kontejner të ri
     */
    @PostMapping
    public ResponseEntity<ContainerResponseDTO> createContainer(
            @RequestBody @Valid CreateContainerRequest request
    ) {
        logger.info("POST /api/containers - Creating container with id: {}", request.getId());
        
        CreateContainerCommand command = new CreateContainerCommand(
            request.getId(),
            request.getZoneId(),
            request.getType(),
            request.getCapacity(),
            request.getLatitude(),
            request.getLongitude(),
            request.getStreet(),
            request.getCity(),
            request.getMunicipality(),
            request.getPostalCode(),
            request.isOperational()
        );

        Kontenier container = createHandler.handle(command);
        ContainerResponseDTO dto = toDTO(container);
        
        logger.info("Successfully created container: {}", container.getId());
        return ResponseEntity.ok(dto);
    }
    
    /**
     * PUT /api/containers/{id} - Përditëson një kontejner ekzistues
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateContainer(
            @PathVariable String id,
            @RequestBody @Valid UpdateContainerRequest request
    ) {
        logger.info("PUT /api/containers/{}", id);
        
        try {
            UpdateContainerCommand command = new UpdateContainerCommand(
                id,
                request.getZoneId(),
                request.getType(),
                request.getCapacity(),
                request.getLatitude(),
                request.getLongitude(),
                request.getStreet(),
                request.getCity(),
                request.getMunicipality(),
                request.getPostalCode(),
                request.getOperational(),
                request.getFillLevel()
            );
            
            updateHandler.handle(command);
            
            logger.info("Successfully updated container: {}", id);
            return ResponseEntity.ok("Kontejneri u përditësua me sukses");
            
        } catch (IllegalArgumentException e) {
            logger.error("Error updating container {}: {}", id, e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    /**
     * DELETE /api/containers/{id} - Fshin një kontejner
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContainer(@PathVariable String id) {
        logger.info("DELETE /api/containers/{}", id);
        
        try {
            DeleteContainerCommand command = new DeleteContainerCommand(id);
            deleteHandler.handle(command);
            
            logger.info("Successfully deleted container: {}", id);
            return ResponseEntity.ok("Kontejneri u fshi me sukses");
            
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting container {}: {}", id, e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    /**
     * GET /api/containers/{id} - Merr një kontejner specifik
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContainerResponseDTO> getContainerById(@PathVariable String id) {
        logger.info("GET /api/containers/{}", id);
        
        return getByIdHandler.handle(GetContainerByIdQuery.of(id))
            .map(container -> {
                logger.debug("Found container: {}", id);
                return ResponseEntity.ok(toDTO(container));
            })
            .orElseGet(() -> {
                logger.warn("Container not found: {}", id);
                return ResponseEntity.notFound().build();
            });
    }
    
    /**
     * GET /api/containers/zone/{zoneId} - Merr të gjitha kontejnerët e një zone
     */
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<ContainerResponseDTO>> getContainersByZone(
            @PathVariable String zoneId
    ) {
        logger.info("GET /api/containers/zone/{}", zoneId);
        
        List<Kontenier> containers = getByZoneHandler.handle(
            GetContainersByZoneQuery.of(zoneId)
        );
        
        List<ContainerResponseDTO> dtos = containers.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        logger.debug("Found {} containers for zone {}", dtos.size(), zoneId);
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * POST /api/containers/{id}/schedule-collection - Cakton kontejnerin për mbledhje
     */
    @PostMapping("/{id}/schedule-collection")
    public ResponseEntity<String> scheduleCollection(
            @PathVariable String id,
            @RequestBody java.util.Map<String, Object> request
    ) {
        logger.info("POST /api/containers/{}/schedule-collection", id);
        
        try {
            String scheduledTimeStr = (String) request.get("scheduledTime");
            Instant scheduledTime = scheduledTimeStr != null 
                ? Instant.parse(scheduledTimeStr)
                : Instant.now().plusSeconds(3600); // Default: 1 orë nga tani
            
            ScheduleCollectionCommand command = new ScheduleCollectionCommand(
                id, scheduledTime
            );
            
            scheduleCollectionHandler.handle(command);
            
            logger.info("Successfully scheduled collection for container {} at {}", id, scheduledTime);
            return ResponseEntity.ok("Mbledhja u caktua me sukses për " + scheduledTime);
        } catch (IllegalArgumentException e) {
            logger.error("Error scheduling collection for container {}: {}", id, e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    /**
     * POST /api/containers/{id}/empty - Zbraz kontejnerin
     */
    @PostMapping("/{id}/empty")
    public ResponseEntity<String> emptyContainer(@PathVariable String id) {
        logger.info("POST /api/containers/{}/empty", id);
        
        try {
            EmptyContainerCommand command = new EmptyContainerCommand(id);
            emptyContainerHandler.handle(command);
            
            logger.info("Successfully emptied container: {}", id);
            return ResponseEntity.ok("Kontejneri u zbraz me sukses");
        } catch (IllegalArgumentException e) {
            logger.error("Error emptying container {}: {}", id, e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    // ========== HELPER METHODS ==========
    
    private ContainerResponseDTO toDTO(Kontenier k) {
        ContainerResponseDTO dto = new ContainerResponseDTO();
        dto.setId(k.getId());
        dto.setZoneId(k.getZoneId());
        dto.setType(k.getType().getDisplayName());
        dto.setFillLevel(k.getFillLevel().getValue());
        dto.setStatus(k.getStatus().getDisplayName());
        dto.setCapacity(k.getCapacity());
        dto.setOperational(k.isOperational());
        dto.setLatitude(k.getLocation().getLatitude());
        dto.setLongitude(k.getLocation().getLongitude());
        dto.setAddress(k.getAddress().toFullAddress());
        dto.setNeedsCollection(k.needsUrgentCollection());
        return dto;
    }
}

