package eco.kosova.infrastructure.persistence;

import com.google.gson.reflect.TypeToken;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.valueobjects.*;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.infrastructure.persistence.dto.ContainerDTO;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class KontenierRepositoryImpl implements KontenierRepository {
    
    private static final String CONTAINERS_FILE = "containers.json";
    private final JsonDataManager dataManager;
    private final Map<String, Kontenier> cache;
    
    public KontenierRepositoryImpl(JsonDataManager dataManager) {
        this.dataManager = dataManager;
        this.cache = new ConcurrentHashMap<>();
        loadFromFile();
    }
    
    @Override
    public Optional<Kontenier> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }
    
    @Override
    public List<Kontenier> findAll() {
        return new ArrayList<>(cache.values());
    }
    
    @Override
    public List<Kontenier> findByZoneId(String zoneId) {
        return cache.values().stream()
            .filter(k -> k.getZoneId().equals(zoneId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findByStatus(ContainerStatus status) {
        return cache.values().stream()
            .filter(k -> k.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findByType(ContainerType type) {
        return cache.values().stream()
            .filter(k -> k.getType() == type)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findCriticalContainers() {
        return cache.values().stream()
            .filter(k -> k.getFillLevel().isCritical())
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findContainersNeedingCollection() {
        return cache.values().stream()
            .filter(Kontenier::isReadyForCollection)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findOperationalByZoneId(String zoneId) {
        return cache.values().stream()
            .filter(k -> k.getZoneId().equals(zoneId) && k.isOperational())
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Kontenier> findNonOperational() {
        return cache.values().stream()
            .filter(k -> !k.isOperational())
            .collect(Collectors.toList());
    }
    
    @Override
    public Kontenier save(Kontenier kontenier) {
        cache.put(kontenier.getId(), kontenier);
        saveToFile();
        return kontenier;
    }
    
    @Override
    public List<Kontenier> saveAll(List<Kontenier> kontejner) {
        kontejner.forEach(k -> cache.put(k.getId(), k));
        saveToFile();
        return kontejner;
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
    public long countByZoneId(String zoneId) {
        return cache.values().stream()
            .filter(k -> k.getZoneId().equals(zoneId))
            .count();
    }
    
    // ========== PRIVATE HELPER METHODS ==========
    
    private void loadFromFile() {
        Type listType = new TypeToken<List<ContainerDTO>>(){}.getType();
        List<ContainerDTO> dtos = dataManager.readList(CONTAINERS_FILE, listType);
        
        dtos.forEach(dto -> {
            Kontenier kontenier = fromDTO(dto);
            cache.put(kontenier.getId(), kontenier);
        });
    }
    
    private void saveToFile() {
        List<ContainerDTO> dtos = cache.values().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        dataManager.writeList(CONTAINERS_FILE, dtos);
    }
    
    private Kontenier fromDTO(ContainerDTO dto) {
        Coordinates coords = new Coordinates(dto.getLatitude(), dto.getLongitude());
        Address address = new Address(dto.getStreet(), dto.getCity(), 
            dto.getMunicipality(), dto.getPostalCode());
        FillLevel fillLevel = new FillLevel(dto.getFillLevel());
        ContainerType type = ContainerType.valueOf(dto.getType());
        ContainerStatus status = ContainerStatus.valueOf(dto.getStatus());
        
        Instant lastEmptied = dto.getLastEmptied() != null ? 
            Instant.parse(dto.getLastEmptied()) : null;
        Instant createdAt = Instant.parse(dto.getCreatedAt());
        
        return new Kontenier(dto.getId(), dto.getZoneId(), type, dto.getCapacity(),
            coords, address, fillLevel, status, dto.isOperational(), lastEmptied, createdAt);
    }
    
    private ContainerDTO toDTO(Kontenier k) {
        ContainerDTO dto = new ContainerDTO();
        dto.setId(k.getId());
        dto.setZoneId(k.getZoneId());
        dto.setType(k.getType().name());
        dto.setFillLevel(k.getFillLevel().getValue());
        dto.setStatus(k.getStatus().name());
        dto.setCapacity(k.getCapacity());
        dto.setOperational(k.isOperational());
        dto.setLatitude(k.getLocation().getLatitude());
        dto.setLongitude(k.getLocation().getLongitude());
        dto.setStreet(k.getAddress().getStreet());
        dto.setCity(k.getAddress().getCity());
        dto.setMunicipality(k.getAddress().getMunicipality());
        dto.setPostalCode(k.getAddress().getPostalCode());
        dto.setLastEmptied(k.getLastEmptied() != null ? k.getLastEmptied().toString() : null);
        dto.setLastUpdated(k.getLastUpdated().toString());
        dto.setCreatedAt(k.getCreatedAt().toString());
        dto.setModifiedAt(k.getModifiedAt().toString());
        return dto;
    }
}

