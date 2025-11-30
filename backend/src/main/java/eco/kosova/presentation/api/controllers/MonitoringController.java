package eco.kosova.presentation.api.controllers;

import eco.kosova.application.commands.UpdateContainerFillLevelCommand;
import eco.kosova.application.handlers.UpdateContainerFillLevelHandler;
import eco.kosova.application.handlers.GetAllContainersHandler;
import eco.kosova.application.handlers.GetCriticalContainersHandler;
import eco.kosova.application.queries.GetAllContainersQuery;
import eco.kosova.application.queries.GetCriticalContainersQuery;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.presentation.dtos.ContainerResponseDTO;
import eco.kosova.presentation.dtos.UpdateFillLevelRequest;
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
    
    @Autowired
    private UpdateContainerFillLevelHandler updateHandler;
    
    @Autowired
    private GetAllContainersHandler getAllHandler;
    
    @Autowired
    private GetCriticalContainersHandler getCriticalHandler;
    
    /**
     * GET /api/monitoring/containers - Merr të gjitha kontejnerët
     */
    @GetMapping("/containers")
    public ResponseEntity<List<ContainerResponseDTO>> getAllContainers() {
        List<Kontenier> containers = getAllHandler.handle(
            GetAllContainersQuery.getInstance()
        );
        
        List<ContainerResponseDTO> dtos = containers.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * GET /api/monitoring/containers/critical - Merr kontejnerët kritikë
     */
    @GetMapping("/containers/critical")
    public ResponseEntity<List<ContainerResponseDTO>> getCriticalContainers() {
        List<Kontenier> containers = getCriticalHandler.handle(
            GetCriticalContainersQuery.getInstance()
        );
        
        List<ContainerResponseDTO> dtos = containers.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * PUT /api/monitoring/containers/{id}/fill-level - Përditëson nivelin
     */
    @PutMapping("/containers/{containerId}/fill-level")
    public ResponseEntity<String> updateFillLevel(
            @PathVariable String containerId,
            @RequestBody UpdateFillLevelRequest request
    ) {
        try {
            UpdateContainerFillLevelCommand command = 
                new UpdateContainerFillLevelCommand(containerId, request.getFillLevel());
            
            updateHandler.handle(command);
            
            return ResponseEntity.ok(
                String.format("Niveli i mbushjes u përditësua: %d%%", request.getFillLevel())
            );
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Gabim i brendshëm: " + e.getMessage());
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