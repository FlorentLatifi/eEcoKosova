package eco.kosova.domain.services;

import eco.kosova.domain.events.DomainEventPublisher;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.ZoneRepository;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Domain Service për monitorimin e sistemit të mbeturinave.
 * 
 * Domain Services përdoren kur:
 * - Logjika përfshin shumë aggregate roots
 * - Operacioni nuk i takon natyrshëm asnjë aggregate
 * - Kërkohet koordinim midis agregativëve
 * 
 * Ky service është përgjegjës për:
 * - Monitorimin e nivelit të mbushjes së kontejnerëve
 * - Publikimin e domain events
 * - Përditësimin e statusit të zonave
 */
public class WasteMonitoringService {
    
    private static final Logger logger = Logger.getLogger(WasteMonitoringService.class.getName());
    
    private final KontenierRepository kontenierRepository;
    private final ZoneRepository zoneRepository;
    private final DomainEventPublisher eventPublisher;
    
    public WasteMonitoringService(
            KontenierRepository kontenierRepository,
            ZoneRepository zoneRepository,
            DomainEventPublisher eventPublisher
    ) {
        this.kontenierRepository = kontenierRepository;
        this.zoneRepository = zoneRepository;
        this.eventPublisher = eventPublisher;
    }
    
    /**
     * Monitoron të gjitha kontejnerët dhe gjeneron events për ato kritikë.
     * Ky method thirret periodikisht (çdo 30 sekonda) nga një scheduler.
     */
    public void monitorAllContainers() {
        logger.info("Starting container monitoring cycle");
        
        List<Kontenier> containers = kontenierRepository.findAll();
        
        int criticalCount = 0;
        
        for (Kontenier container : containers) {
            // Kontrollo nëse kontejneri është kritik
            if (container.getFillLevel().isCritical() && container.isOperational()) {
                criticalCount++;
                
                // Publiko domain events që janë gjeneruar nga aggregate
                eventPublisher.publishAll(container.getDomainEvents());
                container.clearDomainEvents();
                
                // Ruan kontejnerin me statusin e përditësuar
                kontenierRepository.save(container);
            }
        }
        
        logger.info(String.format(
            "Monitoring cycle completed. Total containers: %d, Critical: %d",
            containers.size(), criticalCount
        ));
        
        // Përditëso statusin e zonave bazuar në kontejnerët kritikë
        updateZoneStatuses();
    }
    
    /**
     * Përditëson nivelin e mbushjes së një kontejneri dhe publikon events.
     * 
     * @param containerId ID-ja e kontejnerit
     * @param fillLevel Niveli i ri i mbushjes (0-100)
     */
    public void updateContainerFillLevel(String containerId, int fillLevel) {
        Kontenier container = kontenierRepository.findById(containerId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Container not found: %s", containerId)
            ));
        
        // Përditëso fill level (kjo mund të gjenerojë domain events)
        container.updateFillLevel(fillLevel);
        
        // Publiko domain events
        eventPublisher.publishAll(container.getDomainEvents());
        container.clearDomainEvents();
        
        // Ruan kontejnerin
        kontenierRepository.save(container);
        
        logger.info(String.format(
            "Updated fill level for container %s: %d%%",
            containerId, fillLevel
        ));
        
        // Përditëso statusin e zonës së kontejnerit
        updateZoneStatus(container.getZoneId());
    }
    
    /**
     * Përditëson statusin e të gjitha zonave bazuar në gjendjen e kontejnerëve.
     */
    public void updateZoneStatuses() {
        List<Zone> zones = zoneRepository.findAll();
        
        for (Zone zone : zones) {
            updateZoneStatus(zone.getId());
        }
    }
    
    /**
     * Përditëson statusin e një zone specifike.
     * 
     * @param zoneId ID-ja e zonës
     */
    public void updateZoneStatus(String zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Zone not found: %s", zoneId)
            ));
        
        // Merr të gjitha kontejnerët e zonës
        List<Kontenier> containers = kontenierRepository.findByZoneId(zoneId);
        
        if (containers.isEmpty()) {
            zone.deactivate();
            zoneRepository.save(zone);
            return;
        }
        
        // Numëro kontejnerët kritikë
        long criticalCount = containers.stream()
            .filter(Kontenier::needsUrgentCollection)
            .count();
        
        // Përditëso statusin e zonës
        zone.updateStatusBasedOnContainers((int) criticalCount, containers.size());
        
        zoneRepository.save(zone);
        
        logger.fine(String.format(
            "Updated zone %s status: %s (critical containers: %d/%d)",
            zoneId, zone.getStatus(), criticalCount, containers.size()
        ));
    }
    
    /**
     * Merr të gjitha kontejnerët që duhen mbledhur urgjentisht.
     * 
     * @return Lista e kontejnerëve që duhen mbledhur
     */
    public List<Kontenier> getContainersNeedingUrgentCollection() {
        return kontenierRepository.findAll().stream()
            .filter(Kontenier::needsUrgentCollection)
            .collect(Collectors.toList());
    }
    
    /**
     * Merr statistika për të gjitha zonat.
     * 
     * @return Lista e statistikave për çdo zonë
     */
    public List<ZoneStatistics> getZoneStatistics() {
        List<Zone> zones = zoneRepository.findAll();
        
        return zones.stream()
            .map(zone -> {
                List<Kontenier> containers = kontenierRepository.findByZoneId(zone.getId());
                
                long criticalCount = containers.stream()
                    .filter(Kontenier::needsUrgentCollection)
                    .count();
                
                long operationalCount = containers.stream()
                    .filter(Kontenier::isOperational)
                    .count();
                
                double averageFillLevel = containers.stream()
                    .mapToInt(c -> c.getFillLevel().getValue())
                    .average()
                    .orElse(0.0);
                
                return new ZoneStatistics(
                    zone.getId(),
                    zone.getName(),
                    containers.size(),
                    (int) criticalCount,
                    (int) operationalCount,
                    averageFillLevel,
                    zone.getStatus()
                );
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Klasa e brendshme për statistikat e zonës
     */
    public static class ZoneStatistics {
        private final String zoneId;
        private final String zoneName;
        private final int totalContainers;
        private final int criticalContainers;
        private final int operationalContainers;
        private final double averageFillLevel;
        private final eco.kosova.domain.models.valueobjects.ZoneStatus status;
        
        public ZoneStatistics(
                String zoneId,
                String zoneName,
                int totalContainers,
                int criticalContainers,
                int operationalContainers,
                double averageFillLevel,
                eco.kosova.domain.models.valueobjects.ZoneStatus status
        ) {
            this.zoneId = zoneId;
            this.zoneName = zoneName;
            this.totalContainers = totalContainers;
            this.criticalContainers = criticalContainers;
            this.operationalContainers = operationalContainers;
            this.averageFillLevel = averageFillLevel;
            this.status = status;
        }
        
        // Getters
        public String getZoneId() { return zoneId; }
        public String getZoneName() { return zoneName; }
        public int getTotalContainers() { return totalContainers; }
        public int getCriticalContainers() { return criticalContainers; }
        public int getOperationalContainers() { return operationalContainers; }
        public double getAverageFillLevel() { return averageFillLevel; }
        public eco.kosova.domain.models.valueobjects.ZoneStatus getStatus() { return status; }
    }
}