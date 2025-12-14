package eco.kosova.presentation.api.controllers;

import eco.kosova.domain.models.Qytetari;
import eco.kosova.presentation.dtos.QytetariDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * REST Controller për menaxhimin e qytetarëve.
 * Përdor in-memory storage për demonstrim.
 */
@RestController
@RequestMapping("/api/qytetaret")
@CrossOrigin(origins = "*")
public class QytetariController {
    
    private final Map<String, Qytetari> qytetaret = new ConcurrentHashMap<>();
    
    public QytetariController() {
        initializeDemoData();
    }
    
    @GetMapping
    public ResponseEntity<List<QytetariDTO>> getAllQytetaret() {
        List<QytetariDTO> dtos = qytetaret.values().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<QytetariDTO> getQytetariById(@PathVariable String id) {
        Qytetari qytetari = qytetaret.get(id);
        if (qytetari == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(qytetari));
    }
    
    @PostMapping
    public ResponseEntity<QytetariDTO> createQytetari(@RequestBody Map<String, Object> request) {
        try {
            String id = (String) request.get("id");
            String name = (String) request.get("name");
            String address = (String) request.get("address");
            
            Qytetari qytetari = new Qytetari(id, name, address);
            qytetaret.put(id, qytetari);
            
            return ResponseEntity.ok(toDTO(qytetari));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<QytetariDTO> updateQytetari(
            @PathVariable String id,
            @RequestBody Map<String, Object> request
    ) {
        Qytetari qytetari = qytetaret.get(id);
        if (qytetari == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            String name = (String) request.get("name");
            String address = (String) request.get("address");
            qytetari.updateInfo(name, address);
            
            return ResponseEntity.ok(toDTO(qytetari));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQytetari(@PathVariable String id) {
        if (qytetaret.remove(id) != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
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
    
    private void initializeDemoData() {
        Qytetari q1 = new Qytetari("QYT-001", "Agron Berisha", "Rruga e Dardanisë, Prishtinë");
        Qytetari q2 = new Qytetari("QYT-002", "Blerta Krasniqi", "Rruga e Pejës, Prishtinë");
        qytetaret.put(q1.getId(), q1);
        qytetaret.put(q2.getId(), q2);
    }
}

