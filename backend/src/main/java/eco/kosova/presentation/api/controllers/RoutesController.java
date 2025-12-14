package eco.kosova.presentation.api.controllers;

import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.repositories.ZoneRepository;
import eco.kosova.domain.services.RouteOptimizationService;
import eco.kosova.presentation.dtos.ContainerResponseDTO;
import eco.kosova.presentation.dtos.RouteResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller për optimizimin e rrugëve.
 */
@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RoutesController {
    
    @Autowired
    private RouteOptimizationService routeOptimizationService;
    
    @Autowired
    private ZoneRepository zoneRepository;
    
    /**
     * GET /api/routes/zone/{zoneId} - Merr rrugën optimale për një zonë
     */
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<RouteResponseDTO> getOptimalRouteForZone(
            @PathVariable String zoneId,
            @RequestParam(required = false, defaultValue = "42.6629") double startLat,
            @RequestParam(required = false, defaultValue = "21.1655") double startLon,
            @RequestParam(required = false, defaultValue = "OPTIMAL") String strategy
    ) {
        try {
            // Gjen zonën
            Optional<Zone> zoneOpt = zoneRepository.findById(zoneId);
            if (zoneOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Zone zone = zoneOpt.get();
            Coordinates startPoint = new Coordinates(startLat, startLon);
            
            // Llogarit rrugën bazuar në strategji (përdor Strategy Pattern)
            String strategyName = "OPTIMAL".equalsIgnoreCase(strategy) 
                ? "NEAREST_NEIGHBOR" 
                : strategy.toUpperCase();
            
            List<Kontenier> route = routeOptimizationService.calculateOptimalRoute(
                zoneId, 
                startPoint, 
                strategyName
            );
            
            String routeType = strategyName;
            
            // Krijon RouteInfo për statistika
            RouteOptimizationService.RouteInfo routeInfo = 
                routeOptimizationService.getRouteInfo(route, startPoint);
            
            // Konverton në DTO
            RouteResponseDTO dto = new RouteResponseDTO();
            dto.setZoneId(zoneId);
            dto.setZoneName(zone.getName());
            dto.setContainerCount(routeInfo.getContainerCount());
            dto.setTotalDistanceKm(routeInfo.getTotalDistanceKm());
            dto.setEstimatedTimeMinutes(routeInfo.getEstimatedTimeMinutes());
            dto.setTotalCapacityLiters(routeInfo.getTotalCapacityLiters());
            dto.setRouteType(routeType);
            dto.setContainers(route.stream()
                .map(this::containerToDTO)
                .collect(Collectors.toList()));
            
            return ResponseEntity.ok(dto);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * GET /api/routes/all - Merr rrugët për të gjitha zonat
     */
    @GetMapping("/all")
    public ResponseEntity<List<RouteResponseDTO>> getAllRoutes(
            @RequestParam(required = false, defaultValue = "42.6629") double startLat,
            @RequestParam(required = false, defaultValue = "21.1655") double startLon,
            @RequestParam(required = false, defaultValue = "OPTIMAL") String strategy
    ) {
        try {
            List<Zone> zones = zoneRepository.findAll();
            Coordinates startPoint = new Coordinates(startLat, startLon);
            
            List<RouteResponseDTO> routes = zones.stream()
                .map(zone -> {
                    try {
                        // Përdor Strategy Pattern
                        String strategyName = "OPTIMAL".equalsIgnoreCase(strategy) 
                            ? "NEAREST_NEIGHBOR" 
                            : strategy.toUpperCase();
                        
                        List<Kontenier> route = routeOptimizationService.calculateOptimalRoute(
                            zone.getId(), 
                            startPoint, 
                            strategyName
                        );
                        
                        String routeType = strategyName;
                        
                        if (route.isEmpty()) {
                            return null;
                        }
                        
                        RouteOptimizationService.RouteInfo routeInfo = 
                            routeOptimizationService.getRouteInfo(route, startPoint);
                        
                        RouteResponseDTO dto = new RouteResponseDTO();
                        dto.setZoneId(zone.getId());
                        dto.setZoneName(zone.getName());
                        dto.setContainerCount(routeInfo.getContainerCount());
                        dto.setTotalDistanceKm(routeInfo.getTotalDistanceKm());
                        dto.setEstimatedTimeMinutes(routeInfo.getEstimatedTimeMinutes());
                        dto.setTotalCapacityLiters(routeInfo.getTotalCapacityLiters());
                        dto.setRouteType(routeType);
                        dto.setContainers(route.stream()
                            .map(this::containerToDTO)
                            .collect(Collectors.toList()));
                        
                        return dto;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(routes);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Helper method për të konvertuar Kontenier në ContainerResponseDTO
    private ContainerResponseDTO containerToDTO(Kontenier k) {
        ContainerResponseDTO dto = new ContainerResponseDTO();
        dto.setId(k.getId());
        dto.setZoneId(k.getZoneId());
        dto.setType(k.getType().getDisplayName());
        dto.setFillLevel(k.getFillLevel().getValue());
        dto.setStatus(k.getStatus().getDisplayName());
        dto.setCapacity(k.getCapacity());
        dto.setOperational(k.isOperational());
        dto.setLatitude(k.getLocation().getLatitude());
        dto.setLongitude(k.getLocation().getLongitude());
        dto.setAddress(k.getAddress().toFullAddress());
        dto.setNeedsCollection(k.needsUrgentCollection());
        return dto;
    }
}

