package eco.kosova.presentation.api.controllers;

import eco.kosova.application.commands.UpdateContainerFillLevelCommand;
import eco.kosova.application.handlers.GetAllContainersHandler;
import eco.kosova.application.handlers.GetContainerByIdHandler;
import eco.kosova.application.handlers.GetContainersByZoneHandler;
import eco.kosova.application.handlers.GetCriticalContainersHandler;
import eco.kosova.application.handlers.UpdateContainerFillLevelHandler;
import eco.kosova.application.queries.GetAllContainersQuery;
import eco.kosova.application.queries.GetContainerByIdQuery;
import eco.kosova.application.queries.GetContainersByZoneQuery;
import eco.kosova.application.queries.GetCriticalContainersQuery;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.presentation.dtos.ContainerResponseDTO;
import eco.kosova.presentation.dtos.PagedResponse;
import eco.kosova.presentation.dtos.UpdateFillLevelRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller për monitorimin e kontejnerëve.
 */
@RestController
@RequestMapping("/api/monitoring")
@CrossOrigin(origins = "*")
public class MonitoringController {
    
    private static final Logger logger = LoggerFactory.getLogger(MonitoringController.class);
    
    @Autowired
    private UpdateContainerFillLevelHandler updateHandler;
    
    @Autowired
    private GetAllContainersHandler getAllHandler;
    
    @Autowired
    private GetCriticalContainersHandler getCriticalHandler;
    
    @Autowired
    private GetContainerByIdHandler getByIdHandler;
    
    @Autowired
    private GetContainersByZoneHandler getByZoneHandler;
    
    /**
     * GET /api/monitoring/containers - Merr të gjitha kontejnerët
     */
    @GetMapping("/containers")
    public ResponseEntity<PagedResponse<ContainerResponseDTO>> getAllContainers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        logger.info("GET /api/monitoring/containers - page={}, size={}", page, size);
        
        List<Kontenier> containers = getAllHandler.handle(
            GetAllContainersQuery.getInstance()
        );

        long total = containers.size();
        int fromIndex = Math.max(page * size, 0);
        int toIndex = Math.min(fromIndex + size, containers.size());
        if (fromIndex > toIndex) {
            fromIndex = toIndex;
        }

        List<ContainerResponseDTO> dtos = containers.subList(fromIndex, toIndex).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());

        PagedResponse<ContainerResponseDTO> response = PagedResponse.of(dtos, page, size, total);
        logger.debug("Returning {} containers (page {}, total {})", dtos.size(), page, total);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/monitoring/containers/critical - Merr kontejnerët kritikë
     */
    @GetMapping("/containers/critical")
    public ResponseEntity<List<ContainerResponseDTO>> getCriticalContainers() {
        logger.info("GET /api/monitoring/containers/critical");
        
        List<Kontenier> containers = getCriticalHandler.handle(
            GetCriticalContainersQuery.getInstance()
        );
        
        List<ContainerResponseDTO> dtos = containers.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        logger.debug("Found {} critical containers", dtos.size());
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * PUT /api/monitoring/containers/{id}/fill-level - Përditëson nivelin
     */
    @PutMapping("/containers/{containerId}/fill-level")
    public ResponseEntity<String> updateFillLevel(
            @PathVariable String containerId,
            @RequestBody @Valid UpdateFillLevelRequest request
    ) {
        logger.info("PUT /api/monitoring/containers/{}/fill-level - level={}", containerId, request.getFillLevel());
        
        try {
            UpdateContainerFillLevelCommand command = 
                new UpdateContainerFillLevelCommand(containerId, request.getFillLevel());
            
            updateHandler.handle(command);
            
            logger.info("Successfully updated fill level for container {} to {}%", containerId, request.getFillLevel());
            return ResponseEntity.ok(
                String.format("Niveli i mbushjes u përditësua: %d%%", request.getFillLevel())
            );
            
        } catch (IllegalArgumentException e) {
            logger.error("Error updating fill level for container {}: {}", containerId, e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    /**
     * GET /api/monitoring/containers/{containerId} - Merr një kontejner specifik
     */
    @GetMapping("/containers/{containerId}")
    public ResponseEntity<ContainerResponseDTO> getContainerById(
            @PathVariable String containerId
    ) {
        logger.info("GET /api/monitoring/containers/{}", containerId);
        
        return getByIdHandler.handle(GetContainerByIdQuery.of(containerId))
            .map(container -> {
                logger.debug("Found container: {}", containerId);
                return ResponseEntity.ok(toDTO(container));
            })
            .orElseGet(() -> {
                logger.warn("Container not found: {}", containerId);
                return ResponseEntity.notFound().build();
            });
    }
    
    /**
     * GET /api/monitoring/containers/zone/{zoneId} - Merr të gjitha kontejnerët e një zone
     */
    @GetMapping("/containers/zone/{zoneId}")
    public ResponseEntity<List<ContainerResponseDTO>> getContainersByZone(
            @PathVariable String zoneId
    ) {
        logger.info("GET /api/monitoring/containers/zone/{}", zoneId);
        
        List<Kontenier> containers = getByZoneHandler.handle(
            GetContainersByZoneQuery.of(zoneId)
        );
        
        List<ContainerResponseDTO> dtos = containers.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        logger.debug("Found {} containers for zone {}", dtos.size(), zoneId);
        return ResponseEntity.ok(dtos);
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