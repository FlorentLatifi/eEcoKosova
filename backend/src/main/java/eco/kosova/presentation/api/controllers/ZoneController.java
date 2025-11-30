package eco.kosova.presentation.api.controllers;

import eco.kosova.application.handlers.GetZoneStatisticsHandler;
import eco.kosova.application.queries.GetZoneStatisticsQuery;
import eco.kosova.domain.services.WasteMonitoringService;
import eco.kosova.presentation.dtos.ZoneStatisticsDTO;
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
    
    @Autowired
    private GetZoneStatisticsHandler statisticsHandler;
    
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
}