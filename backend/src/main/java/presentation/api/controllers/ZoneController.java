package eco.kosova.presentation.api.controllers;

import eco.kosova.application.handlers.GetZoneStatisticsHandler;
import eco.kosova.application.queries.GetZoneStatisticsQuery;
import eco.kosova.domain.services.WasteMonitoringService;
import eco.kosova.presentation.dtos.CreateZoneRequest;
import eco.kosova.presentation.dtos.PagedResponse;
import eco.kosova.presentation.dtos.UpdateZoneRequest;
import eco.kosova.presentation.dtos.ZoneDTO;
import eco.kosova.presentation.dtos.ZoneStatisticsDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller për zonat.
 */
@RestController
@RequestMapping("/api/zones")
@CrossOrigin(origins = "*")
public class ZoneController {
    
    private static final Logger logger = LoggerFactory.getLogger(ZoneController.class);
    
    @Autowired
    private GetZoneStatisticsHandler statisticsHandler;
    
    @Autowired
    private eco.kosova.application.handlers.CreateZoneHandler createZoneHandler;
    
    @Autowired
    private eco.kosova.application.handlers.UpdateZoneHandler updateZoneHandler;
    
    @Autowired
    private eco.kosova.application.handlers.DeleteZoneHandler deleteZoneHandler;
    
    @Autowired
    private eco.kosova.domain.repositories.ZoneRepository zoneRepository;
    
    /**
     * GET /api/zones/statistics - Merr statistika për të gjitha zonat
     */
    @GetMapping("/statistics")
    public ResponseEntity<List<ZoneStatisticsDTO>> getZoneStatistics() {
        logger.info("GET /api/zones/statistics");
        
        List<WasteMonitoringService.ZoneStatistics> stats = 
            statisticsHandler.handle(GetZoneStatisticsQuery.getInstance());
        
        List<ZoneStatisticsDTO> dtos = stats.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        logger.debug("Returning statistics for {} zones", dtos.size());
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * GET /api/zones - Merr të gjitha zonat
     */
    @GetMapping
    public ResponseEntity<PagedResponse<ZoneDTO>> getAllZones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        logger.info("GET /api/zones - page={}, size={}", page, size);
        
        List<eco.kosova.domain.models.Zone> zones = zoneRepository.findAll();
        long total = zones.size();
        
        int fromIndex = Math.max(page * size, 0);
        int toIndex = Math.min(fromIndex + size, zones.size());
        if (fromIndex > toIndex) {
            fromIndex = toIndex;
        }
        
        List<ZoneDTO> dtos = zones.subList(fromIndex, toIndex).stream()
            .map(this::zoneToDTO)
            .collect(Collectors.toList());
        
        PagedResponse<ZoneDTO> response = PagedResponse.of(dtos, page, size, total);
        logger.debug("Returning {} zones (page {}, total {})", dtos.size(), page, total);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/zones - Krijon një zonë të re
     */
    @PostMapping
    public ResponseEntity<String> createZone(@RequestBody @Valid CreateZoneRequest request) {
        logger.info("POST /api/zones - Creating zone with id: {}", request.getZoneId());
        
        try {
            eco.kosova.application.commands.CreateZoneCommand command = 
                new eco.kosova.application.commands.CreateZoneCommand(
                    request.getZoneId(),
                    request.getName(),
                    request.getLatitude(),
                    request.getLongitude(),
                    request.getMunicipality(),
                    request.getDescription()
                );
            
            createZoneHandler.handle(command);
            logger.info("Successfully created zone: {}", request.getZoneId());
            return ResponseEntity.ok("Zona u krijua me sukses");
            
        } catch (IllegalArgumentException e) {
            logger.error("Error creating zone: {}", e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    /**
     * PUT /api/zones/{id} - Përditëson një zonë ekzistuese
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateZone(
            @PathVariable String id,
            @RequestBody @Valid UpdateZoneRequest request
    ) {
        logger.info("PUT /api/zones/{}", id);
        
        try {
            eco.kosova.application.commands.UpdateZoneCommand command = 
                new eco.kosova.application.commands.UpdateZoneCommand(
                    id,
                    request.getName(),
                    request.getLatitude(),
                    request.getLongitude(),
                    request.getMunicipality(),
                    request.getDescription()
                );
            
            updateZoneHandler.handle(command);
            logger.info("Successfully updated zone: {}", id);
            return ResponseEntity.ok("Zona u përditësua me sukses");
            
        } catch (IllegalArgumentException e) {
            logger.error("Error updating zone {}: {}", id, e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    /**
     * DELETE /api/zones/{id} - Fshin një zonë
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteZone(@PathVariable String id) {
        logger.info("DELETE /api/zones/{}", id);
        
        try {
            eco.kosova.application.commands.DeleteZoneCommand command = 
                new eco.kosova.application.commands.DeleteZoneCommand(id);
            deleteZoneHandler.handle(command);
            logger.info("Successfully deleted zone: {}", id);
            return ResponseEntity.ok("Zona u fshi me sukses");
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Error deleting zone {}: {}", id, e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    private ZoneStatisticsDTO toDTO(WasteMonitoringService.ZoneStatistics stats) {
        ZoneStatisticsDTO dto = new ZoneStatisticsDTO();
        dto.setZoneId(stats.getZoneId());
        dto.setZoneName(stats.getZoneName());
        dto.setTotalContainers(stats.getTotalContainers());
        dto.setCriticalContainers(stats.getCriticalContainers());
        dto.setOperationalContainers(stats.getOperationalContainers());
        dto.setAverageFillLevel(stats.getAverageFillLevel());
        dto.setStatus(stats.getStatus().getDisplayName());
        return dto;
    }
    
    private ZoneDTO zoneToDTO(eco.kosova.domain.models.Zone zone) {
        ZoneDTO dto = new ZoneDTO();
        dto.setId(zone.getId());
        dto.setName(zone.getName());
        dto.setStatus(zone.getStatus().getDisplayName());
        dto.setCriticalThreshold(zone.getCriticalThreshold());
        dto.setContainerIds(new java.util.ArrayList<>(zone.getContainerIds()));
        eco.kosova.presentation.dtos.LocationDTO location = new eco.kosova.presentation.dtos.LocationDTO();
        location.setLatitude(zone.getCenterPoint().getLatitude());
        location.setLongitude(zone.getCenterPoint().getLongitude());
        dto.setCenterPoint(location);
        return dto;
    }
}