package eco.kosova.infrastructure.persistence;

import eco.kosova.domain.models.Zone;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.models.valueobjects.ZoneStatus;
import eco.kosova.domain.repositories.ZoneRepository;
import eco.kosova.infrastructure.persistence.jpa.ZoneEntity;
import eco.kosova.infrastructure.persistence.jpa.ZoneEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation e ZoneRepository që përdor MSSQL (JPA) për persistence.
 */
@Repository
public class ZoneRepositoryImpl implements ZoneRepository {
    
    private final ZoneEntityRepository zoneEntityRepository;
    
    public ZoneRepositoryImpl(ZoneEntityRepository zoneEntityRepository) {
        this.zoneEntityRepository = zoneEntityRepository;
    }
    
    @Override
    public Optional<Zone> findById(String id) {
        return zoneEntityRepository.findById(id).map(this::toDomain);
    }
    
    @Override
    public List<Zone> findAll() {
        return zoneEntityRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Zone> findByMunicipality(String municipality) {
        return zoneEntityRepository.findByMunicipalityIgnoreCase(municipality).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Zone> findByStatus(ZoneStatus status) {
        return zoneEntityRepository.findByStatus(status.name()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Zone> findCriticalZones() {
        return zoneEntityRepository.findCriticalZones().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Zone> findActiveZones() {
        return zoneEntityRepository.findActiveZones().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Zone> findByContainerId(String containerId) {
        return zoneEntityRepository.findByContainerId(containerId).map(this::toDomain);
    }
    
    @Override
    public Zone save(Zone zone) {
        ZoneEntity entity = toEntity(zone);
        zoneEntityRepository.save(entity);
        return zone;
    }
    
    @Override
    public List<Zone> saveAll(List<Zone> zones) {
        List<ZoneEntity> entities = zones.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
        zoneEntityRepository.saveAll(entities);
        return zones;
    }
    
    @Override
    public boolean deleteById(String id) {
        boolean existed = zoneEntityRepository.existsById(id);
        if (existed) {
            zoneEntityRepository.deleteById(id);
        }
        return existed;
    }
    
    @Override
    public boolean existsById(String id) {
        return zoneEntityRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return zoneEntityRepository.count();
    }
    
    @Override
    public long countActive() {
        return zoneEntityRepository.countActive();
    }
    
    @Override
    public long countCritical() {
        return zoneEntityRepository.countCritical();
    }
    
    // ========== PRIVATE HELPER METHODS ==========
    
    private Zone toDomain(ZoneEntity entity) {
        Coordinates centerPoint = new Coordinates(entity.getLatitude(), entity.getLongitude());
        ZoneStatus status = ZoneStatus.valueOf(entity.getStatus());
        
        Set<String> containerIds = entity.getContainers().stream()
            .map(c -> c.getId())
            .collect(Collectors.toSet());
        
        return new Zone(
            entity.getId(),
            entity.getName(),
            centerPoint,
            entity.getMunicipality(),
            status,
            containerIds,
            entity.getCriticalThreshold(),
            entity.getDescription(),
            entity.getCreatedAt()
        );
    }
    
    private ZoneEntity toEntity(Zone zone) {
        ZoneEntity entity = new ZoneEntity();
        entity.setId(zone.getId());
        entity.setName(zone.getName());
        entity.setStatus(zone.getStatus().name());
        entity.setMunicipality(zone.getMunicipality());
        entity.setDescription(zone.getDescription());
        entity.setCriticalThreshold(zone.getCriticalThreshold());
        entity.setLatitude(zone.getCenterPoint().getLatitude());
        entity.setLongitude(zone.getCenterPoint().getLongitude());
        entity.setCreatedAt(zone.getCreatedAt());
        entity.setModifiedAt(zone.getModifiedAt());
        return entity;
    }
}