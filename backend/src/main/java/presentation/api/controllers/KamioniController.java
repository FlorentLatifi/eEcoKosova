package eco.kosova.presentation.api.controllers;

import eco.kosova.domain.models.Kamioni;
import eco.kosova.domain.models.Paisje;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.presentation.dtos.KamioniDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * REST Controller për menaxhimin e kamionëve.
 * Përdor in-memory storage për demonstrim.
 */
@RestController
@RequestMapping("/api/kamionet")
@CrossOrigin(origins = "*")
public class KamioniController {
    
    // In-memory storage për demonstrim
    private final Map<String, Kamioni> kamionet = new ConcurrentHashMap<>();
    
    public KamioniController() {
        // Initialize me disa kamionë demo
        initializeDemoData();
    }
    
    /**
     * GET /api/kamionet - Merr të gjitha kamionët
     */
    @GetMapping
    public ResponseEntity<List<KamioniDTO>> getAllKamionet() {
        List<KamioniDTO> dtos = kamionet.values().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * GET /api/kamionet/{id} - Merr një kamion specifik
     */
    @GetMapping("/{id}")
    public ResponseEntity<KamioniDTO> getKamioniById(@PathVariable String id) {
        Kamioni kamioni = kamionet.get(id);
        if (kamioni == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(kamioni));
    }
    
    /**
     * GET /api/kamionet/available - Merr kamionët e disponueshëm
     */
    @GetMapping("/available")
    public ResponseEntity<List<KamioniDTO>> getAvailableKamionet() {
        List<KamioniDTO> dtos = kamionet.values().stream()
            .filter(Kamioni::isAvailable)
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * POST /api/kamionet - Krijon një kamion të ri
     */
    @PostMapping
    public ResponseEntity<KamioniDTO> createKamioni(@RequestBody Map<String, Object> request) {
        try {
            String id = (String) request.get("id");
            String name = (String) request.get("name");
            String licensePlate = (String) request.get("licensePlate");
            Integer capacity = ((Number) request.get("capacity")).intValue();
            String operatorId = (String) request.get("operatorId");
            Double lat = ((Number) request.get("latitude")).doubleValue();
            Double lon = ((Number) request.get("longitude")).doubleValue();
            
            Coordinates location = new Coordinates(lat, lon);
            Kamioni kamioni = new Kamioni(id, name, licensePlate, capacity, location, operatorId, Instant.now());
            kamionet.put(id, kamioni);
            
            return ResponseEntity.ok(toDTO(kamioni));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PUT /api/kamionet/{id} - Përditëson një kamion
     */
    @PutMapping("/{id}")
    public ResponseEntity<KamioniDTO> updateKamioni(
            @PathVariable String id,
            @RequestBody Map<String, Object> request
    ) {
        Kamioni kamioni = kamionet.get(id);
        if (kamioni == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            if (request.containsKey("name")) {
                kamioni.setName((String) request.get("name"));
            }
            if (request.containsKey("licensePlate")) {
                kamioni.setLicensePlate((String) request.get("licensePlate"));
            }
            if (request.containsKey("capacity")) {
                kamioni.setCapacity(((Number) request.get("capacity")).intValue());
            }
            if (request.containsKey("operatorId")) {
                kamioni.setOperatorId((String) request.get("operatorId"));
            }
            if (request.containsKey("status")) {
                String statusStr = (String) request.get("status");
                kamioni.updateStatus(Paisje.PaisjeStatus.valueOf(statusStr));
            }
            
            return ResponseEntity.ok(toDTO(kamioni));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * DELETE /api/kamionet/{id} - Fshin një kamion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKamioni(@PathVariable String id) {
        if (kamionet.remove(id) != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * POST /api/kamionet/{id}/assign-route - Cakton kamionin në një rrugë
     */
    @PostMapping("/{id}/assign-route")
    public ResponseEntity<KamioniDTO> assignRoute(
            @PathVariable String id,
            @RequestBody Map<String, Object> request
    ) {
        Kamioni kamioni = kamionet.get(id);
        if (kamioni == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            String routeId = (String) request.get("routeId");
            @SuppressWarnings("unchecked")
            List<String> containerIds = (List<String>) request.get("containerIds");
            
            kamioni.assignToRoute(routeId, containerIds);
            return ResponseEntity.ok(toDTO(kamioni));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * POST /api/kamionet/{id}/release-route - Lëshon kamionin nga rruga
     */
    @PostMapping("/{id}/release-route")
    public ResponseEntity<KamioniDTO> releaseRoute(@PathVariable String id) {
        Kamioni kamioni = kamionet.get(id);
        if (kamioni == null) {
            return ResponseEntity.notFound().build();
        }
        
        kamioni.releaseFromRoute();
        return ResponseEntity.ok(toDTO(kamioni));
    }
    
    // Helper methods
    private KamioniDTO toDTO(Kamioni k) {
        KamioniDTO dto = new KamioniDTO();
        dto.setId(k.getId());
        dto.setName(k.getName());
        dto.setLicensePlate(k.getLicensePlate());
        dto.setCapacity(k.getCapacity());
        dto.setOperatorId(k.getOperatorId());
        dto.setStatus(k.getStatus().name());
        dto.setLatitude(k.getLocation().getLatitude());
        dto.setLongitude(k.getLocation().getLongitude());
        dto.setCurrentRouteId(k.getCurrentRouteId());
        dto.setAssignedContainerCount(k.getAssignedContainerIds().size());
        dto.setInstallationDate(k.getInstallationDate());
        dto.setLastUpdated(k.getLastUpdated());
        dto.setAvailable(k.isAvailable());
        return dto;
    }
    
    private void initializeDemoData() {
        // Krijo disa kamionë demo
        Kamioni k1 = new Kamioni(
            "KAM-001",
            "Kamion Mbledhje 1",
            "KS-123-AB",
            5000,
            new Coordinates(42.6629, 21.1655),
            "OP-001",
            Instant.now()
        );
        kamionet.put(k1.getId(), k1);
        
        Kamioni k2 = new Kamioni(
            "KAM-002",
            "Kamion Mbledhje 2",
            "KS-456-CD",
            8000,
            new Coordinates(42.6729, 21.1755),
            "OP-002",
            Instant.now()
        );
        kamionet.put(k2.getId(), k2);
    }
}

