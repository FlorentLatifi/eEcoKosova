package eco.kosova.infrastructure.persistence;

import com.google.gson.reflect.TypeToken;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.models.valueobjects.ZoneStatus;
import eco.kosova.domain.repositories.ZoneRepository;
import eco.kosova.infrastructure.persistence.dto.ZoneDTO;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation e ZoneRepository që përdor JSON për persistence.
 */
@Repository
public class ZoneRepositoryImpl implements ZoneRepository {
    
    private static final String ZONES_FILE = "zones.json";
    private final JsonDataManager dataManager;
    private final Map<String, Zone> cache;
    
    public ZoneRepositoryImpl(JsonDataManager dataManager) {
        this.dataManager = dataManager;
        this.cache = new ConcurrentHashMap<>();
        loadFromFile();
    }
    
    @Override
    public Optional<Zone> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }
    
    @Override
    public List<Zone> findAll() {
        return new ArrayList<>(cache.values());
    }
    
    @Override
    public List<Zone> findByMunicipality(String municipality) {
        return cache.values().stream()
            .filter(z -> z.getMunicipality().equalsIgnoreCase(municipality))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Zone> findByStatus(ZoneStatus status) {
        return cache.values().stream()
            .filter(z -> z.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Zone> findCriticalZones() {
        return cache.values().stream()
            .filter(z -> z.getStatus() == ZoneStatus.CRITICAL)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Zone> findActiveZones() {
        return cache.values().stream()
            .filter(Zone::isActive)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Zone> findByContainerId(String containerId) {
        return cache.values().stream()
            .filter(z -> z.containsContainer(containerId))
            .findFirst();
    }
    
    @Override
    public Zone save(Zone zone) {
        cache.put(zone.getId(), zone);
        saveToFile();
        return zone;
    }
    
    @Override
    public List<Zone> saveAll(List<Zone> zones) {
        zones.forEach(z -> cache.put(z.getId(), z));
        saveToFile();
        return zones;
    }
    
    @Override
    public boolean deleteById(String id) {
        boolean existed = cache.remove(id) != null;
        if (existed) {
            saveToFile();
        }
        return existed;
    }
    
    @Override
    public boolean existsById(String id) {
        return cache.containsKey(id);
    }
    
    @Override
    public long count() {
        return cache.size();
    }
    
    @Override
    public long countActive() {
        return cache.values().stream()
            .filter(Zone::isActive)
            .count();
    }
    
    @Override
    public long countCritical() {
        return cache.values().stream()
            .filter(z -> z.getStatus() == ZoneStatus.CRITICAL)
            .count();
    }
    
    // ========== PRIVATE HELPER METHODS ==========
    
    private void loadFromFile() {
        Type listType = new TypeToken<List<ZoneDTO>>(){}.getType();
        List<ZoneDTO> dtos = dataManager.readList(ZONES_FILE, listType);
        
        dtos.forEach(dto -> {
            Zone zone = fromDTO(dto);
            cache.put(zone.getId(), zone);
        });
    }
    
    private void saveToFile() {
        List<ZoneDTO> dtos = cache.values().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        dataManager.writeList(ZONES_FILE, dtos);
    }
    
    private Zone fromDTO(ZoneDTO dto) {
        Coordinates centerPoint = new Coordinates(dto.getLatitude(), dto.getLongitude());
        ZoneStatus status = ZoneStatus.valueOf(dto.getStatus());
        Instant createdAt = Instant.parse(dto.getCreatedAt());
        
        return new Zone(
            dto.getId(),
            dto.getName(),
            centerPoint,
            dto.getMunicipality(),
            status,
            dto.getContainerIds(),
            dto.getCriticalThreshold(),
            dto.getDescription(),
            createdAt
        );
    }
    
    private ZoneDTO toDTO(Zone zone) {
        ZoneDTO dto = new ZoneDTO();
        dto.setId(zone.getId());
        dto.setName(zone.getName());
        dto.setStatus(zone.getStatus().name());
        dto.setMunicipality(zone.getMunicipality());
        dto.setDescription(zone.getDescription());
        dto.setCriticalThreshold(zone.getCriticalThreshold());
        dto.setLatitude(zone.getCenterPoint().getLatitude());
        dto.setLongitude(zone.getCenterPoint().getLongitude());
        dto.setContainerIds(zone.getContainerIds());
        dto.setCreatedAt(zone.getCreatedAt().toString());
        dto.setModifiedAt(zone.getModifiedAt().toString());
        return dto;
    }
}