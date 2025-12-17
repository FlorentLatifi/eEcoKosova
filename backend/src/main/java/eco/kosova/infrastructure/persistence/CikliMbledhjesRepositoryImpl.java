package eco.kosova.infrastructure.persistence;

import eco.kosova.domain.models.CikliMbledhjes;
import eco.kosova.domain.repositories.CikliMbledhjesRepository;
import eco.kosova.infrastructure.persistence.jpa.CikliMbledhjesEntity;
import eco.kosova.infrastructure.persistence.jpa.CikliMbledhjesEntityRepository;
import eco.kosova.infrastructure.persistence.jpa.ZoneEntity;
import eco.kosova.infrastructure.persistence.jpa.ZoneEntityRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CikliMbledhjesRepositoryImpl implements CikliMbledhjesRepository {
    
    private final CikliMbledhjesEntityRepository cikliEntityRepository;
    private final ZoneEntityRepository zoneEntityRepository;
    
    public CikliMbledhjesRepositoryImpl(
            CikliMbledhjesEntityRepository cikliEntityRepository,
            ZoneEntityRepository zoneEntityRepository) {
        this.cikliEntityRepository = cikliEntityRepository;
        this.zoneEntityRepository = zoneEntityRepository;
    }
    
    @Override
    public Optional<CikliMbledhjes> findById(String id) {
        return cikliEntityRepository.findById(id).map(this::toDomain);
    }
    
    @Override
    public List<CikliMbledhjes> findAll() {
        return cikliEntityRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CikliMbledhjes> findByZoneId(String zoneId) {
        return cikliEntityRepository.findByZone_Id(zoneId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CikliMbledhjes> findByKamioniId(String kamioniId) {
        return cikliEntityRepository.findByKamioniId(kamioniId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CikliMbledhjes> findByStatus(CikliMbledhjes.CikliStatus status) {
        return cikliEntityRepository.findByStatus(status.name()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CikliMbledhjes> findActive() {
        return cikliEntityRepository.findActiveCiklet().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CikliMbledhjes> findByCollectionDay(DayOfWeek day) {
        // This requires a custom query - for now, filter in memory
        return cikliEntityRepository.findAll().stream()
            .map(this::toDomain)
            .filter(c -> c.getCollectionDays().contains(day))
            .collect(Collectors.toList());
    }
    
    @Override
    public CikliMbledhjes save(CikliMbledhjes cikli) {
        CikliMbledhjesEntity entity = toEntity(cikli);
        cikliEntityRepository.save(entity);
        return cikli;
    }
    
    @Override
    public List<CikliMbledhjes> saveAll(List<CikliMbledhjes> ciklet) {
        List<CikliMbledhjesEntity> entities = ciklet.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
        cikliEntityRepository.saveAll(entities);
        return ciklet;
    }
    
    @Override
    public boolean deleteById(String id) {
        boolean existed = cikliEntityRepository.existsById(id);
        if (existed) {
            cikliEntityRepository.deleteById(id);
        }
        return existed;
    }
    
    @Override
    public boolean existsById(String id) {
        return cikliEntityRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return cikliEntityRepository.count();
    }
    
    @Override
    public long countByZoneId(String zoneId) {
        return cikliEntityRepository.findByZone_Id(zoneId).size();
    }
    
    // ========== PRIVATE HELPER METHODS ==========
    
    private CikliMbledhjes toDomain(CikliMbledhjesEntity entity) {
        Set<DayOfWeek> days = entity.getCollectionDays().stream()
            .map(DayOfWeek::valueOf)
            .collect(Collectors.toSet());
        
        CikliMbledhjes cikli = new CikliMbledhjes(
            entity.getId(),
            entity.getScheduleTime(),
            entity.getMaxCapacity(),
            days,
            entity.getZone() != null ? entity.getZone().getId() : null
        );
        
        if (entity.getKamioniId() != null) {
            cikli.assignKamioni(entity.getKamioniId());
        }
        
        // Set status
        CikliMbledhjes.CikliStatus status = CikliMbledhjes.CikliStatus.valueOf(entity.getStatus());
        switch (status) {
            case ACTIVE:
                if (cikli.getStatus() != CikliMbledhjes.CikliStatus.ACTIVE) {
                    cikli.activate();
                }
                break;
            case COMPLETED:
                cikli.complete();
                break;
            case CANCELLED:
                cikli.cancel();
                break;
            default:
                // SCHEDULED is default
                break;
        }
        
        return cikli;
    }
    
    private CikliMbledhjesEntity toEntity(CikliMbledhjes c) {
        CikliMbledhjesEntity entity = new CikliMbledhjesEntity();
        entity.setId(c.getId());
        entity.setScheduleTime(c.getScheduleTime());
        entity.setMaxCapacity(c.getMaxCapacity());
        entity.setKamioniId(c.getKamioniId());
        entity.setStatus(c.getStatus().name());
        
        ZoneEntity zone = zoneEntityRepository.findById(c.getZoneId())
            .orElseThrow(() -> new IllegalStateException("Zone not found: " + c.getZoneId()));
        entity.setZone(zone);
        
        Set<String> days = c.getCollectionDays().stream()
            .map(Enum::name)
            .collect(Collectors.toSet());
        entity.setCollectionDays(days);
        
        entity.setCreatedAt(c.getCreatedAt());
        entity.setLastUpdated(c.getLastUpdated());
        
        return entity;
    }
}

