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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller për menaxhimin e kontejnerëve (CRUD operations).
 */
@RestController
@RequestMapping("/api/containers")
@CrossOrigin(origins = "*")
public class ContainerManagementController {
    
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
            @org.springframework.web.bind.annotation.RequestBody
            @jakarta.validation.Valid CreateContainerRequest request
    ) {
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
        return ResponseEntity.ok(dto);
    }
    
    /**
     * PUT /api/containers/{id} - Përditëson një kontejner ekzistues
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateContainer(
            @PathVariable String id,
            @RequestBody Map<String, Object> request
    ) {
        try {
            UpdateContainerCommand command = new UpdateContainerCommand(
                id,
                (String) request.get("zoneId"),
                (String) request.get("type"),
                request.get("capacity") != null ? ((Number) request.get("capacity")).intValue() : null,
                request.get("latitude") != null ? ((Number) request.get("latitude")).doubleValue() : null,
                request.get("longitude") != null ? ((Number) request.get("longitude")).doubleValue() : null,
                (String) request.get("street"),
                (String) request.get("city"),
                (String) request.get("municipality"),
                (String) request.get("postalCode"),
                request.get("operational") != null ? (Boolean) request.get("operational") : null,
                request.get("fillLevel") != null ? ((Number) request.get("fillLevel")).intValue() : null
            );
            
            updateHandler.handle(command);
            
            return ResponseEntity.ok("Kontejneri u përditësua me sukses");
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Gabim i brendshëm: " + e.getMessage());
        }
    }
    
    /**
     * DELETE /api/containers/{id} - Fshin një kontejner
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContainer(@PathVariable String id) {
        try {
            DeleteContainerCommand command = new DeleteContainerCommand(id);
            deleteHandler.handle(command);
            
            return ResponseEntity.ok("Kontejneri u fshi me sukses");
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Gabim i brendshëm: " + e.getMessage());
        }
    }
    
    /**
     * GET /api/containers/{id} - Merr një kontejner specifik
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContainerResponseDTO> getContainerById(@PathVariable String id) {
        try {
            return getByIdHandler.handle(GetContainerByIdQuery.of(id))
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * GET /api/containers/zone/{zoneId} - Merr të gjitha kontejnerët e një zone
     */
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<ContainerResponseDTO>> getContainersByZone(
            @PathVariable String zoneId
    ) {
        try {
            List<Kontenier> containers = getByZoneHandler.handle(
                GetContainersByZoneQuery.of(zoneId)
            );
            
            List<ContainerResponseDTO> dtos = containers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * POST /api/containers/{id}/schedule-collection - Cakton kontejnerin për mbledhje
     */
    @PostMapping("/{id}/schedule-collection")
    public ResponseEntity<String> scheduleCollection(
            @PathVariable String id,
            @RequestBody Map<String, Object> request
    ) {
        try {
            String scheduledTimeStr = (String) request.get("scheduledTime");
            Instant scheduledTime = scheduledTimeStr != null 
                ? Instant.parse(scheduledTimeStr)
                : Instant.now().plusSeconds(3600); // Default: 1 orë nga tani
            
            ScheduleCollectionCommand command = new ScheduleCollectionCommand(
                id, scheduledTime
            );
            
            scheduleCollectionHandler.handle(command);
            
            return ResponseEntity.ok("Mbledhja u caktua me sukses për " + scheduledTime);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Gabim i brendshëm: " + e.getMessage());
        }
    }
    
    /**
     * POST /api/containers/{id}/empty - Zbraz kontejnerin
     */
    @PostMapping("/{id}/empty")
    public ResponseEntity<String> emptyContainer(@PathVariable String id) {
        try {
            EmptyContainerCommand command = new EmptyContainerCommand(id);
            emptyContainerHandler.handle(command);
            
            return ResponseEntity.ok("Kontejneri u zbraz me sukses");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Gabim i brendshëm: " + e.getMessage());
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

