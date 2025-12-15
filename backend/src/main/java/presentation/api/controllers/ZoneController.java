package eco.kosova.presentation.api.controllers;

import eco.kosova.application.handlers.GetZoneStatisticsHandler;
import eco.kosova.application.queries.GetZoneStatisticsQuery;
import eco.kosova.domain.services.WasteMonitoringService;
import eco.kosova.presentation.dtos.ZoneStatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller për zonat.
 */
@RestController
@RequestMapping("/api/zones")
@CrossOrigin(origins = "*")
public class ZoneController {
    
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
        List<WasteMonitoringService.ZoneStatistics> stats = 
            statisticsHandler.handle(GetZoneStatisticsQuery.getInstance());
        
        List<ZoneStatisticsDTO> dtos = stats.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * GET /api/zones - Merr të gjitha zonat
     */
    @GetMapping
    public ResponseEntity<List<eco.kosova.presentation.dtos.ZoneDTO>> getAllZones() {
        List<eco.kosova.domain.models.Zone> zones = zoneRepository.findAll();
        List<eco.kosova.presentation.dtos.ZoneDTO> dtos = zones.stream()
            .map(this::zoneToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * POST /api/zones - Krijon një zonë të re
     */
    @PostMapping
    public ResponseEntity<String> createZone(@RequestBody Map<String, Object> request) {
        try {
            eco.kosova.application.commands.CreateZoneCommand command = 
                new eco.kosova.application.commands.CreateZoneCommand(
                    (String) request.get("zoneId"),
                    (String) request.get("name"),
                    ((Number) request.getOrDefault("latitude", 42.6629)).doubleValue(),
                    ((Number) request.getOrDefault("longitude", 21.1655)).doubleValue(),
                    (String) request.get("municipality"),
                    (String) request.getOrDefault("description", "")
                );
            
            createZoneHandler.handle(command);
            return ResponseEntity.ok("Zona u krijua me sukses");
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Gabim i brendshëm: " + e.getMessage());
        }
    }
    
    /**
     * PUT /api/zones/{id} - Përditëson një zonë ekzistuese
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateZone(
            @PathVariable String id,
            @RequestBody Map<String, Object> request
    ) {
        try {
            eco.kosova.application.commands.UpdateZoneCommand command = 
                new eco.kosova.application.commands.UpdateZoneCommand(
                    id,
                    (String) request.get("name"),
                    request.get("latitude") != null ? ((Number) request.get("latitude")).doubleValue() : null,
                    request.get("longitude") != null ? ((Number) request.get("longitude")).doubleValue() : null,
                    (String) request.get("municipality"),
                    (String) request.get("description")
                );
            
            updateZoneHandler.handle(command);
            return ResponseEntity.ok("Zona u përditësua me sukses");
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Gabim i brendshëm: " + e.getMessage());
        }
    }
    
    /**
     * DELETE /api/zones/{id} - Fshin një zonë
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteZone(@PathVariable String id) {
        try {
            eco.kosova.application.commands.DeleteZoneCommand command = 
                new eco.kosova.application.commands.DeleteZoneCommand(id);
            deleteZoneHandler.handle(command);
            return ResponseEntity.ok("Zona u fshi me sukses");
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Gabim i brendshëm: " + e.getMessage());
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
    
    private eco.kosova.presentation.dtos.ZoneDTO zoneToDTO(eco.kosova.domain.models.Zone zone) {
        eco.kosova.presentation.dtos.ZoneDTO dto = new eco.kosova.presentation.dtos.ZoneDTO();
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