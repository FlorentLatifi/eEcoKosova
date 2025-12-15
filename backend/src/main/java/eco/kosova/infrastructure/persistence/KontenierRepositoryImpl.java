package eco.kosova.infrastructure.persistence;

import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.valueobjects.*;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.infrastructure.persistence.jpa.ContainerEntity;
import eco.kosova.infrastructure.persistence.jpa.ContainerEntityRepository;
import eco.kosova.infrastructure.persistence.jpa.ZoneEntity;
import eco.kosova.infrastructure.persistence.jpa.ZoneEntityRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class KontenierRepositoryImpl implements KontenierRepository {
    
    private final ContainerEntityRepository containerEntityRepository;
    private final ZoneEntityRepository zoneEntityRepository;
    
    public KontenierRepositoryImpl(ContainerEntityRepository containerEntityRepository,
                                   ZoneEntityRepository zoneEntityRepository) {
        this.containerEntityRepository = containerEntityRepository;
        this.zoneEntityRepository = zoneEntityRepository;
    }
    
    @Override
    public Optional<Kontenier> findById(String id) {
        return containerEntityRepository.findById(id).map(this::toDomain);
    }
    
    @Override
    public List<Kontenier> findAll() {
        return containerEntityRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findByZoneId(String zoneId) {
        return containerEntityRepository.findByZone_Id(zoneId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findByStatus(ContainerStatus status) {
        return containerEntityRepository.findByStatus(status.name()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findByType(ContainerType type) {
        return containerEntityRepository.findByType(type.name()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findCriticalContainers() {
        return containerEntityRepository.findCriticalContainers().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findContainersNeedingCollection() {
        return containerEntityRepository.findContainersNeedingCollection().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findOperationalByZoneId(String zoneId) {
        return containerEntityRepository.findOperationalByZoneId(zoneId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findNonOperational() {
        return containerEntityRepository.findNonOperational().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public Kontenier save(Kontenier kontenier) {
        ContainerEntity entity = toEntity(kontenier);
        containerEntityRepository.save(entity);
        return kontenier;
    }
    
    @Override
    public List<Kontenier> saveAll(List<Kontenier> kontejner) {
        List<ContainerEntity> entities = kontejner.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
        containerEntityRepository.saveAll(entities);
        return kontejner;
    }
    
    @Override
    public boolean deleteById(String id) {
        boolean existed = containerEntityRepository.existsById(id);
        if (existed) {
            containerEntityRepository.deleteById(id);
        }
        return existed;
    }
    
    @Override
    public boolean existsById(String id) {
        return containerEntityRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return containerEntityRepository.count();
    }
    
    @Override
    public long countByZoneId(String zoneId) {
        return containerEntityRepository.countByZone_Id(zoneId);
    }
    
    // ========== PRIVATE HELPER METHODS ==========
    
    private Kontenier toDomain(ContainerEntity entity) {
        Coordinates coords = new Coordinates(entity.getLatitude(), entity.getLongitude());
        Address address = new Address(
            entity.getStreet(),
            entity.getCity(),
            entity.getMunicipality(),
            entity.getPostalCode()
        );
        FillLevel fillLevel = new FillLevel(entity.getFillLevel());
        ContainerType type = ContainerType.valueOf(entity.getType());
        ContainerStatus status = ContainerStatus.valueOf(entity.getStatus());
        
        String zoneId = entity.getZone() != null ? entity.getZone().getId() : null;
        
        return new Kontenier(
            entity.getId(),
            zoneId,
            type,
            entity.getCapacity(),
            coords,
            address,
            fillLevel,
            status,
            entity.isOperational(),
            entity.getLastEmptied(),
            entity.getCreatedAt()
        );
    }
    
    private ContainerEntity toEntity(Kontenier k) {
        ContainerEntity entity = new ContainerEntity();
        entity.setId(k.getId());
        
        ZoneEntity zone = zoneEntityRepository.findById(k.getZoneId())
            .orElseThrow(() -> new IllegalStateException("Zone not found: " + k.getZoneId()));
        
        entity.setZone(zone);
        entity.setType(k.getType().name());
        entity.setFillLevel(k.getFillLevel().getValue());
        entity.setStatus(k.getStatus().name());
        entity.setCapacity(k.getCapacity());
        entity.setOperational(k.isOperational());
        entity.setLatitude(k.getLocation().getLatitude());
        entity.setLongitude(k.getLocation().getLongitude());
        entity.setStreet(k.getAddress().getStreet());
        entity.setCity(k.getAddress().getCity());
        entity.setMunicipality(k.getAddress().getMunicipality());
        entity.setPostalCode(k.getAddress().getPostalCode());
        entity.setLastEmptied(k.getLastEmptied());
        entity.setLastUpdated(k.getLastUpdated());
        entity.setCreatedAt(k.getCreatedAt());
        entity.setModifiedAt(k.getModifiedAt());
        return entity;
    }
}

