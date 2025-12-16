package eco.kosova.presentation.api.controllers;

import eco.kosova.domain.models.CikliMbledhjes;
import eco.kosova.domain.repositories.CikliMbledhjesRepository;
import eco.kosova.presentation.dtos.CikliMbledhjesDTO;
import eco.kosova.presentation.dtos.CreateCikliRequest;
import eco.kosova.presentation.dtos.PagedResponse;
import eco.kosova.presentation.dtos.UpdateCikliRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller për menaxhimin e cikleve të mbledhjes.
 */
@RestController
@RequestMapping("/api/ciklet")
@CrossOrigin(origins = "*")
public class CikliMbledhjesController {
    
    private static final Logger logger = LoggerFactory.getLogger(CikliMbledhjesController.class);
    
    @Autowired
    private CikliMbledhjesRepository cikliRepository;
    
    @GetMapping
    public ResponseEntity<PagedResponse<CikliMbledhjesDTO>> getAllCiklet(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        logger.info("GET /api/ciklet - page={}, size={}", page, size);
        
        List<CikliMbledhjes> allCiklet = cikliRepository.findAll();
        long total = allCiklet.size();
        
        int fromIndex = Math.max(page * size, 0);
        int toIndex = Math.min(fromIndex + size, allCiklet.size());
        if (fromIndex > toIndex) {
            fromIndex = toIndex;
        }
        
        List<CikliMbledhjesDTO> dtos = allCiklet.subList(fromIndex, toIndex).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        PagedResponse<CikliMbledhjesDTO> response = PagedResponse.of(dtos, page, size, total);
        logger.debug("Returning {} ciklet (page {}, total {})", dtos.size(), page, total);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CikliMbledhjesDTO> getCikliById(@PathVariable String id) {
        logger.info("GET /api/ciklet/{}", id);
        
        return cikliRepository.findById(id)
            .map(cikli -> {
                logger.debug("Found cikli: {}", id);
                return ResponseEntity.ok(toDTO(cikli));
            })
            .orElseGet(() -> {
                logger.warn("Cikli not found: {}", id);
                return ResponseEntity.notFound().build();
            });
    }
    
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<CikliMbledhjesDTO>> getCikletByZone(@PathVariable String zoneId) {
        logger.info("GET /api/ciklet/zone/{}", zoneId);
        
        List<CikliMbledhjesDTO> dtos = cikliRepository.findByZoneId(zoneId).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        logger.debug("Found {} ciklet for zone {}", dtos.size(), zoneId);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<CikliMbledhjesDTO>> getActiveCiklet() {
        logger.info("GET /api/ciklet/active");
        
        List<CikliMbledhjesDTO> dtos = cikliRepository.findActive().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        logger.debug("Found {} active ciklet", dtos.size());
        return ResponseEntity.ok(dtos);
    }
    
    @PostMapping
    public ResponseEntity<CikliMbledhjesDTO> createCikli(@RequestBody @Valid CreateCikliRequest request) {
        logger.info("POST /api/ciklet - Creating cikli with id: {}", request.getId());
        
        try {
            CikliMbledhjes cikli = new CikliMbledhjes(
                request.getId(),
                request.getScheduleTime(),
                request.getMaxCapacity(),
                request.getCollectionDays(),
                request.getZoneId()
            );
            
            if (request.getKamioniId() != null && !request.getKamioniId().isBlank()) {
                cikli.assignKamioni(request.getKamioniId());
            }
            
            cikli = cikliRepository.save(cikli);
            logger.info("Successfully created cikli: {}", cikli.getId());
            
            return ResponseEntity.ok(toDTO(cikli));
        } catch (IllegalArgumentException e) {
            logger.error("Validation error creating cikli: {}", e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CikliMbledhjesDTO> updateCikli(
            @PathVariable String id,
            @RequestBody @Valid UpdateCikliRequest request
    ) {
        logger.info("PUT /api/ciklet/{}", id);
        
        CikliMbledhjes cikli = cikliRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Cikli not found for update: {}", id);
                return new IllegalArgumentException("Cikli not found: " + id);
            });
        
        try {
            if (request.getScheduleTime() != null) {
                cikli.setScheduleTime(request.getScheduleTime());
            }
            if (request.getMaxCapacity() != null) {
                cikli.setMaxCapacity(request.getMaxCapacity());
            }
            if (request.getCollectionDays() != null && !request.getCollectionDays().isEmpty()) {
                // Note: Domain model doesn't have a setter for collectionDays
                // This would require domain logic to update the set
            }
            if (request.getKamioniId() != null) {
                cikli.assignKamioni(request.getKamioniId());
            }
            
            cikli = cikliRepository.save(cikli);
            logger.info("Successfully updated cikli: {}", id);
            
            return ResponseEntity.ok(toDTO(cikli));
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("Error updating cikli {}: {}", id, e.getMessage());
            throw e;
        }
    }
    
    @PostMapping("/{id}/activate")
    public ResponseEntity<CikliMbledhjesDTO> activateCikli(@PathVariable String id) {
        logger.info("POST /api/ciklet/{}/activate", id);
        
        CikliMbledhjes cikli = cikliRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Cikli not found for activation: {}", id);
                return new IllegalArgumentException("Cikli not found: " + id);
            });
        
        try {
            cikli.activate();
            cikli = cikliRepository.save(cikli);
            logger.info("Successfully activated cikli: {}", id);
            
            return ResponseEntity.ok(toDTO(cikli));
        } catch (IllegalStateException e) {
            logger.error("Error activating cikli {}: {}", id, e.getMessage());
            throw e;
        }
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<CikliMbledhjesDTO> completeCikli(@PathVariable String id) {
        logger.info("POST /api/ciklet/{}/complete", id);
        
        CikliMbledhjes cikli = cikliRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Cikli not found for completion: {}", id);
                return new IllegalArgumentException("Cikli not found: " + id);
            });
        
        cikli.complete();
        cikli = cikliRepository.save(cikli);
        logger.info("Successfully completed cikli: {}", id);
        
        return ResponseEntity.ok(toDTO(cikli));
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<CikliMbledhjesDTO> cancelCikli(@PathVariable String id) {
        logger.info("POST /api/ciklet/{}/cancel", id);
        
        CikliMbledhjes cikli = cikliRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Cikli not found for cancellation: {}", id);
                return new IllegalArgumentException("Cikli not found: " + id);
            });
        
        cikli.cancel();
        cikli = cikliRepository.save(cikli);
        logger.info("Successfully cancelled cikli: {}", id);
        
        return ResponseEntity.ok(toDTO(cikli));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCikli(@PathVariable String id) {
        logger.info("DELETE /api/ciklet/{}", id);
        
        boolean deleted = cikliRepository.deleteById(id);
        if (deleted) {
            logger.info("Successfully deleted cikli: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Cikli not found for deletion: {}", id);
            return ResponseEntity.notFound().build();
        }
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
}
