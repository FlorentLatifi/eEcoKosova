package eco.kosova.presentation.api.controllers;

import eco.kosova.domain.models.CikliMbledhjes;
import eco.kosova.presentation.dtos.CikliMbledhjesDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * REST Controller për menaxhimin e cikleve të mbledhjes.
 */
@RestController
@RequestMapping("/api/ciklet")
@CrossOrigin(origins = "*")
public class CikliMbledhjesController {
    
    private final Map<String, CikliMbledhjes> ciklet = new ConcurrentHashMap<>();
    
    public CikliMbledhjesController() {
        initializeDemoData();
    }
    
    @GetMapping
    public ResponseEntity<List<CikliMbledhjesDTO>> getAllCiklet() {
        List<CikliMbledhjesDTO> dtos = ciklet.values().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CikliMbledhjesDTO> getCikliById(@PathVariable String id) {
        CikliMbledhjes cikli = ciklet.get(id);
        if (cikli == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(cikli));
    }
    
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<CikliMbledhjesDTO>> getCikletByZone(@PathVariable String zoneId) {
        List<CikliMbledhjesDTO> dtos = ciklet.values().stream()
            .filter(c -> c.getZoneId().equals(zoneId))
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<CikliMbledhjesDTO>> getActiveCiklet() {
        List<CikliMbledhjesDTO> dtos = ciklet.values().stream()
            .filter(c -> c.getStatus() == CikliMbledhjes.CikliStatus.ACTIVE || 
                        c.getStatus() == CikliMbledhjes.CikliStatus.SCHEDULED)
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @PostMapping
    public ResponseEntity<CikliMbledhjesDTO> createCikli(@RequestBody Map<String, Object> request) {
        try {
            String id = (String) request.get("id");
            String scheduleTimeStr = (String) request.get("scheduleTime");
            Integer maxCapacity = ((Number) request.get("maxCapacity")).intValue();
            String zoneId = (String) request.get("zoneId");
            
            @SuppressWarnings("unchecked")
            List<String> daysStr = (List<String>) request.get("collectionDays");
            Set<DayOfWeek> days = daysStr.stream()
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());
            
            LocalDateTime scheduleTime = LocalDateTime.parse(scheduleTimeStr);
            
            CikliMbledhjes cikli = new CikliMbledhjes(id, scheduleTime, maxCapacity, days, zoneId);
            ciklet.put(id, cikli);
            
            return ResponseEntity.ok(toDTO(cikli));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CikliMbledhjesDTO> updateCikli(
            @PathVariable String id,
            @RequestBody Map<String, Object> request
    ) {
        CikliMbledhjes cikli = ciklet.get(id);
        if (cikli == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            if (request.containsKey("scheduleTime")) {
                String scheduleTimeStr = (String) request.get("scheduleTime");
                cikli.setScheduleTime(LocalDateTime.parse(scheduleTimeStr));
            }
            if (request.containsKey("maxCapacity")) {
                cikli.setMaxCapacity(((Number) request.get("maxCapacity")).intValue());
            }
            if (request.containsKey("kamioniId")) {
                cikli.assignKamioni((String) request.get("kamioniId"));
            }
            
            return ResponseEntity.ok(toDTO(cikli));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/activate")
    public ResponseEntity<CikliMbledhjesDTO> activateCikli(@PathVariable String id) {
        CikliMbledhjes cikli = ciklet.get(id);
        if (cikli == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            cikli.activate();
            return ResponseEntity.ok(toDTO(cikli));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<CikliMbledhjesDTO> completeCikli(@PathVariable String id) {
        CikliMbledhjes cikli = ciklet.get(id);
        if (cikli == null) {
            return ResponseEntity.notFound().build();
        }
        
        cikli.complete();
        return ResponseEntity.ok(toDTO(cikli));
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<CikliMbledhjesDTO> cancelCikli(@PathVariable String id) {
        CikliMbledhjes cikli = ciklet.get(id);
        if (cikli == null) {
            return ResponseEntity.notFound().build();
        }
        
        cikli.cancel();
        return ResponseEntity.ok(toDTO(cikli));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCikli(@PathVariable String id) {
        if (ciklet.remove(id) != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    private CikliMbledhjesDTO toDTO(CikliMbledhjes c) {
        CikliMbledhjesDTO dto = new CikliMbledhjesDTO();
        dto.setId(c.getId());
        dto.setScheduleTime(c.getScheduleTime());
        dto.setMaxCapacity(c.getMaxCapacity());
        dto.setCollectionDays(c.getCollectionDays());
        dto.setZoneId(c.getZoneId());
        dto.setKamioniId(c.getKamioniId());
        dto.setStatus(c.getStatus().name());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setLastUpdated(c.getLastUpdated());
        return dto;
    }
    
    private void initializeDemoData() {
        Set<DayOfWeek> days = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        CikliMbledhjes c1 = new CikliMbledhjes(
            "CIKLI-001",
            LocalDateTime.now().plusDays(1).withHour(8).withMinute(0),
            10000,
            days,
            "ZONE-001"
        );
        c1.assignKamioni("KAM-001");
        ciklet.put(c1.getId(), c1);
    }
}

