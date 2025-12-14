package eco.kosova.presentation.api.controllers;

import eco.kosova.domain.models.KontrollPanel;
import eco.kosova.presentation.dtos.KontrollPanelDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * REST Controller për menaxhimin e paneleve të kontrollit.
 */
@RestController
@RequestMapping("/api/kontroll-panel")
@CrossOrigin(origins = "*")
public class KontrollPanelController {
    
    private final Map<String, KontrollPanel> panels = new ConcurrentHashMap<>();
    
    public KontrollPanelController() {
        initializeDemoData();
    }
    
    @GetMapping
    public ResponseEntity<List<KontrollPanelDTO>> getAllPanels() {
        List<KontrollPanelDTO> dtos = panels.values().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<KontrollPanelDTO> getPanelById(@PathVariable String id) {
        KontrollPanel panel = panels.get(id);
        if (panel == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(panel));
    }
    
    @GetMapping("/qytetari/{qytetariId}")
    public ResponseEntity<KontrollPanelDTO> getPanelByQytetariId(@PathVariable String qytetariId) {
        KontrollPanel panel = panels.values().stream()
            .filter(p -> p.getQytetariId().equals(qytetariId))
            .findFirst()
            .orElse(null);
        
        if (panel == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(panel));
    }
    
    @PostMapping
    public ResponseEntity<KontrollPanelDTO> createPanel(@RequestBody Map<String, Object> request) {
        try {
            String id = (String) request.get("id");
            String qytetariId = (String) request.get("qytetariId");
            
            KontrollPanel panel = new KontrollPanel(id, qytetariId);
            panels.put(id, panel);
            
            return ResponseEntity.ok(toDTO(panel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<KontrollPanelDTO> updatePanel(
            @PathVariable String id,
            @RequestBody Map<String, Object> request
    ) {
        KontrollPanel panel = panels.get(id);
        if (panel == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            if (request.containsKey("language")) {
                panel.setLanguage((String) request.get("language"));
            }
            if (request.containsKey("theme")) {
                panel.setTheme((String) request.get("theme"));
            }
            if (request.containsKey("screenState")) {
                String stateStr = (String) request.get("screenState");
                panel.setScreenState(KontrollPanel.ScreenState.valueOf(stateStr));
            }
            
            return ResponseEntity.ok(toDTO(panel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePanel(@PathVariable String id) {
        if (panels.remove(id) != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    private KontrollPanelDTO toDTO(KontrollPanel p) {
        KontrollPanelDTO dto = new KontrollPanelDTO();
        dto.setId(p.getId());
        dto.setLanguage(p.getLanguage());
        dto.setTheme(p.getTheme());
        dto.setScreenState(p.getScreenState().name());
        dto.setQytetariId(p.getQytetariId());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setLastUpdated(p.getLastUpdated());
        return dto;
    }
    
    private void initializeDemoData() {
        KontrollPanel p1 = new KontrollPanel("PANEL-001", "QYT-001");
        KontrollPanel p2 = new KontrollPanel("PANEL-002", "QYT-002");
        panels.put(p1.getId(), p1);
        panels.put(p2.getId(), p2);
    }
}

