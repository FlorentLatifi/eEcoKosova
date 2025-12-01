package eco.kosova.presentation.api.controllers;

import eco.kosova.application.handlers.GetAllContainersHandler;
import eco.kosova.application.handlers.GetZoneStatisticsHandler;
import eco.kosova.application.queries.GetAllContainersQuery;
import eco.kosova.application.queries.GetZoneStatisticsQuery;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.services.WasteMonitoringService;
import eco.kosova.presentation.dtos.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Controller për raportet.
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportsController {
    
    @Autowired
    private GetAllContainersHandler getAllContainersHandler;
    
    @Autowired
    private GetZoneStatisticsHandler getZoneStatisticsHandler;
    
    /**
     * GET /api/reports - Merr të gjitha raportet e disponueshme
     */
    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAvailableReports() {
        List<ReportDTO> reports = new ArrayList<>();
        
        // Raporti i përgjithshëm
        ReportDTO generalReport = new ReportDTO();
        generalReport.setId("GENERAL");
        generalReport.setTitle("Raport i Përgjithshëm");
        generalReport.setDescription("Statistika të përgjithshme për sistemin");
        generalReport.setType("GENERAL");
        generalReport.setGeneratedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        reports.add(generalReport);
        
        // Raporti për kontejnerët kritikë
        ReportDTO criticalReport = new ReportDTO();
        criticalReport.setId("CRITICAL");
        criticalReport.setTitle("Kontejnerët Kritikë");
        criticalReport.setDescription("Lista e kontejnerëve që kanë nevojë për mbledhje urgjente");
        criticalReport.setType("CRITICAL");
        criticalReport.setGeneratedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        reports.add(criticalReport);
        
        // Raporti për zonat
        ReportDTO zonesReport = new ReportDTO();
        zonesReport.setId("ZONES");
        zonesReport.setTitle("Raport i Zonave");
        zonesReport.setDescription("Statistika për të gjitha zonat");
        zonesReport.setType("ZONES");
        zonesReport.setGeneratedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        reports.add(zonesReport);
        
        // Raporti i performancës
        ReportDTO performanceReport = new ReportDTO();
        performanceReport.setId("PERFORMANCE");
        performanceReport.setTitle("Raport i Performancës");
        performanceReport.setDescription("Analizë e performancës së sistemit");
        performanceReport.setType("PERFORMANCE");
        performanceReport.setGeneratedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        reports.add(performanceReport);
        
        return ResponseEntity.ok(reports);
    }
    
    /**
     * POST /api/reports/generate - Gjeneron një raport të ri
     */
    @PostMapping("/generate")
    public ResponseEntity<ReportDTO> generateReport(@RequestBody Map<String, String> request) {
        String reportType = request.getOrDefault("type", "GENERAL");
        
        try {
            ReportDTO report = generateReportData(reportType);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * GET /api/reports/{reportId} - Merr një raport specifik
     */
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDTO> getReport(@PathVariable String reportId) {
        try {
            ReportDTO report = generateReportData(reportId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private ReportDTO generateReportData(String reportType) {
        List<Kontenier> allContainers = getAllContainersHandler.handle(
            GetAllContainersQuery.getInstance()
        );
        
        List<WasteMonitoringService.ZoneStatistics> zoneStats = 
            getZoneStatisticsHandler.handle(GetZoneStatisticsQuery.getInstance());
        
        ReportDTO report = new ReportDTO();
        report.setId(reportType);
        report.setGeneratedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        report.setType(reportType);
        
        Map<String, Object> data = new HashMap<>();
        
        switch (reportType.toUpperCase()) {
            case "GENERAL":
                report.setTitle("Raport i Përgjithshëm");
                report.setDescription("Statistika të përgjithshme për sistemin");
                data.put("totalContainers", allContainers.size());
                data.put("criticalContainers", allContainers.stream()
                    .filter(c -> c.getFillLevel().getValue() >= 90)
                    .count());
                data.put("warningContainers", allContainers.stream()
                    .filter(c -> c.getFillLevel().getValue() >= 70 && c.getFillLevel().getValue() < 90)
                    .count());
                data.put("normalContainers", allContainers.stream()
                    .filter(c -> c.getFillLevel().getValue() < 70)
                    .count());
                data.put("totalZones", zoneStats.size());
                data.put("criticalZones", zoneStats.stream()
                    .filter(z -> z.getStatus().needsAttention())
                    .count());
                data.put("averageFillLevel", allContainers.stream()
                    .mapToInt(c -> c.getFillLevel().getValue())
                    .average()
                    .orElse(0.0));
                break;
                
            case "CRITICAL":
                report.setTitle("Kontejnerët Kritikë");
                report.setDescription("Lista e kontejnerëve që kanë nevojë për mbledhje urgjente");
                List<Map<String, Object>> criticalContainers = allContainers.stream()
                    .filter(c -> c.getFillLevel().getValue() >= 90)
                    .map(c -> {
                        Map<String, Object> containerData = new HashMap<>();
                        containerData.put("id", c.getId());
                        containerData.put("zoneId", c.getZoneId());
                        containerData.put("fillLevel", c.getFillLevel().getValue());
                        containerData.put("status", c.getStatus().getDisplayName());
                        containerData.put("address", c.getAddress().toFullAddress());
                        return containerData;
                    })
                    .collect(Collectors.toList());
                data.put("criticalContainers", criticalContainers);
                data.put("count", criticalContainers.size());
                break;
                
            case "ZONES":
                report.setTitle("Raport i Zonave");
                report.setDescription("Statistika për të gjitha zonat");
                List<Map<String, Object>> zonesData = zoneStats.stream()
                    .map(z -> {
                        Map<String, Object> zoneData = new HashMap<>();
                        zoneData.put("zoneId", z.getZoneId());
                        zoneData.put("zoneName", z.getZoneName());
                        zoneData.put("totalContainers", z.getTotalContainers());
                        zoneData.put("criticalContainers", z.getCriticalContainers());
                        zoneData.put("averageFillLevel", z.getAverageFillLevel());
                        zoneData.put("status", z.getStatus().getDisplayName());
                        return zoneData;
                    })
                    .collect(Collectors.toList());
                data.put("zones", zonesData);
                data.put("totalZones", zonesData.size());
                break;
                
            case "PERFORMANCE":
                report.setTitle("Raport i Performancës");
                report.setDescription("Analizë e performancës së sistemit");
                long operationalContainers = allContainers.stream()
                    .filter(Kontenier::isOperational)
                    .count();
                data.put("operationalContainers", operationalContainers);
                data.put("nonOperationalContainers", allContainers.size() - operationalContainers);
                data.put("operationalRate", allContainers.isEmpty() ? 0.0 : 
                    (double) operationalContainers / allContainers.size() * 100);
                data.put("averageFillLevel", allContainers.stream()
                    .mapToInt(c -> c.getFillLevel().getValue())
                    .average()
                    .orElse(0.0));
                data.put("totalCapacity", allContainers.stream()
                    .mapToInt(Kontenier::getCapacity)
                    .sum());
                break;
                
            default:
                throw new IllegalArgumentException("Invalid report type: " + reportType);
        }
        
        report.setData(data);
        return report;
    }
}

