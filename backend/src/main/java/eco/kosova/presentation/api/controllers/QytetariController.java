package eco.kosova.presentation.api.controllers;

import eco.kosova.domain.models.Qytetari;
import eco.kosova.domain.repositories.QytetariRepository;
import eco.kosova.presentation.dtos.CreateQytetariRequest;
import eco.kosova.presentation.dtos.PagedResponse;
import eco.kosova.presentation.dtos.QytetariDTO;
import eco.kosova.presentation.dtos.UpdateQytetariRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller për menaxhimin e qytetarëve.
 */
@RestController
@RequestMapping("/api/qytetaret")
@CrossOrigin(origins = "*")
public class QytetariController {
    
    private static final Logger logger = LoggerFactory.getLogger(QytetariController.class);
    
    @Autowired
    private QytetariRepository qytetariRepository;
    
    @GetMapping
    public ResponseEntity<PagedResponse<QytetariDTO>> getAllQytetaret(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        logger.info("GET /api/qytetaret - page={}, size={}", page, size);
        
        List<Qytetari> allQytetaret = qytetariRepository.findAll();
        long total = allQytetaret.size();
        
        int fromIndex = Math.max(page * size, 0);
        int toIndex = Math.min(fromIndex + size, allQytetaret.size());
        if (fromIndex > toIndex) {
            fromIndex = toIndex;
        }
        
        List<QytetariDTO> dtos = allQytetaret.subList(fromIndex, toIndex).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        PagedResponse<QytetariDTO> response = PagedResponse.of(dtos, page, size, total);
        logger.debug("Returning {} qytetaret (page {}, total {})", dtos.size(), page, total);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<QytetariDTO> getQytetariById(@PathVariable String id) {
        logger.info("GET /api/qytetaret/{}", id);
        
        return qytetariRepository.findById(id)
            .map(qytetari -> {
                logger.debug("Found qytetari: {}", id);
                return ResponseEntity.ok(toDTO(qytetari));
            })
            .orElseGet(() -> {
                logger.warn("Qytetari not found: {}", id);
                return ResponseEntity.notFound().build();
            });
    }
    
    @PostMapping
    public ResponseEntity<QytetariDTO> createQytetari(@RequestBody @Valid CreateQytetariRequest request) {
        logger.info("POST /api/qytetaret - Creating qytetari with id: {}", request.getId());
        
        try {
            Qytetari qytetari = new Qytetari(
                request.getId(),
                request.getName(),
                request.getAddress()
            );
            
            qytetari = qytetariRepository.save(qytetari);
            logger.info("Successfully created qytetari: {}", qytetari.getId());
            
            return ResponseEntity.ok(toDTO(qytetari));
        } catch (IllegalArgumentException e) {
            logger.error("Validation error creating qytetari: {}", e.getMessage());
            throw e; // Will be handled by GlobalExceptionHandler
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<QytetariDTO> updateQytetari(
            @PathVariable String id,
            @RequestBody @Valid UpdateQytetariRequest request
    ) {
        logger.info("PUT /api/qytetaret/{}", id);
        
        Qytetari qytetari = qytetariRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Qytetari not found for update: {}", id);
                return new IllegalArgumentException("Qytetari not found: " + id);
            });
        
        try {
            qytetari.updateInfo(request.getName(), request.getAddress());
            qytetari = qytetariRepository.save(qytetari);
            logger.info("Successfully updated qytetari: {}", id);
            
            return ResponseEntity.ok(toDTO(qytetari));
        } catch (IllegalArgumentException e) {
            logger.error("Error updating qytetari {}: {}", id, e.getMessage());
            throw e;
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQytetari(@PathVariable String id) {
        logger.info("DELETE /api/qytetaret/{}", id);
        
        boolean deleted = qytetariRepository.deleteById(id);
        if (deleted) {
            logger.info("Successfully deleted qytetari: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Qytetari not found for deletion: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
    
    private QytetariDTO toDTO(Qytetari q) {
        QytetariDTO dto = new QytetariDTO();
        dto.setId(q.getId());
        dto.setName(q.getName());
        dto.setAddress(q.getAddress());
        dto.setCreatedAt(q.getCreatedAt());
        dto.setLastUpdated(q.getLastUpdated());
        return dto;
    }
}
