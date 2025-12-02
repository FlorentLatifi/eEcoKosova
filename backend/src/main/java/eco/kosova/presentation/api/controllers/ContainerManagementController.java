package eco.kosova.presentation.api.controllers;

import eco.kosova.application.commands.CreateContainerCommand;
import eco.kosova.application.commands.DeleteContainerCommand;
import eco.kosova.application.commands.UpdateContainerCommand;
import eco.kosova.application.handlers.CreateContainerHandler;
import eco.kosova.application.handlers.DeleteContainerHandler;
import eco.kosova.application.handlers.UpdateContainerHandler;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.presentation.dtos.ContainerResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller për menaxhimin e kontejnerëve (CRUD operations).
 */
@RestController
@RequestMapping("/api/containers")
@CrossOrigin(origins = "*")
public class ContainerManagementController {
    
    @Autowired
    private CreateContainerHandler createHandler;
    
    @Autowired
    private UpdateContainerHandler updateHandler;
    
    @Autowired
    private DeleteContainerHandler deleteHandler;
    
    /**
     * POST /api/containers - Krijon një kontejner të ri
     */
    @PostMapping
    public ResponseEntity<ContainerResponseDTO> createContainer(
            @RequestBody Map<String, Object> request
    ) {
        try {
            CreateContainerCommand command = new CreateContainerCommand(
                (String) request.get("id"),
                (String) request.get("zoneId"),
                (String) request.get("type"),
                ((Number) request.getOrDefault("capacity", 1000)).intValue(),
                ((Number) request.getOrDefault("latitude", 42.6629)).doubleValue(),
                ((Number) request.getOrDefault("longitude", 21.1655)).doubleValue(),
                (String) request.get("street"),
                (String) request.get("city"),
                (String) request.get("municipality"),
                (String) request.getOrDefault("postalCode", ""),
                (Boolean) request.getOrDefault("operational", true)
            );
            
            createHandler.handle(command);
            
            // Kthen kontejnerin e krijuar
            return ResponseEntity.ok().build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * PUT /api/containers/{id} - Përditëson një kontejner ekzistues
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateContainer(
            @PathVariable String id,
            @RequestBody Map<String, Object> request
    ) {
        try {
            UpdateContainerCommand command = new UpdateContainerCommand(
                id,
                (String) request.get("zoneId"),
                (String) request.get("type"),
                request.get("capacity") != null ? ((Number) request.get("capacity")).intValue() : null,
                request.get("latitude") != null ? ((Number) request.get("latitude")).doubleValue() : null,
                request.get("longitude") != null ? ((Number) request.get("longitude")).doubleValue() : null,
                (String) request.get("street"),
                (String) request.get("city"),
                (String) request.get("municipality"),
                (String) request.get("postalCode"),
                request.get("operational") != null ? (Boolean) request.get("operational") : null,
                request.get("fillLevel") != null ? ((Number) request.get("fillLevel")).intValue() : null
            );
            
            updateHandler.handle(command);
            
            return ResponseEntity.ok("Kontejneri u përditësua me sukses");
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Gabim i brendshëm: " + e.getMessage());
        }
    }
    
    /**
     * DELETE /api/containers/{id} - Fshin një kontejner
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContainer(@PathVariable String id) {
        try {
            DeleteContainerCommand command = new DeleteContainerCommand(id);
            deleteHandler.handle(command);
            
            return ResponseEntity.ok("Kontejneri u fshi me sukses");
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Gabim: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Gabim i brendshëm: " + e.getMessage());
        }
    }
}

