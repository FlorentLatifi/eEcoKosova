package eco.kosova.infrastructure.persistence;

import com.google.gson.reflect.TypeToken;
import eco.kosova.domain.models.Kamioni;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.models.valueobjects.ContainerStatus;
import eco.kosova.domain.repositories.KamioniRepository;
import eco.kosova.infrastructure.persistence.dto.KamioniDTO;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class KamioniRepositoryImpl implements KamioniRepository {
    private static final String TRUCKS_FILE = "trucks.json";
    private final JsonDataManager dataManager;
    private final Map<String, Kamioni> cache;

    public KamioniRepositoryImpl(JsonDataManager dataManager) {
        this.dataManager = dataManager;
        this.cache = new ConcurrentHashMap<>();
        loadFromFile();
    }

    private void loadFromFile() {
        Type listType = new TypeToken<List<KamioniDTO>>(){}.getType();
        List<KamioniDTO> dtos = dataManager.readList(TRUCKS_FILE, listType);
        if (dtos == null) return;
        for (KamioniDTO d : dtos) {
            try {
                Coordinates coords = new Coordinates(d.getLatitude(), d.getLongitude());
                Kamioni k = new Kamioni(
                    d.getId(),
                    d.getEmri(),
                    ContainerStatus.valueOf(d.getStatus()),
                    coords,
                    Instant.now(),
                    d.getTarga(),
                    d.getKapaciteti(),
                    d.getOperatoriID()
                );
                cache.put(k.getPaisjeID(), k);
            } catch (Exception e) {
                // skip invalid entries
            }
        }
    }

    private void saveToFile() {
        List<KamioniDTO> dtos = cache.values().stream().map(k -> {
            KamioniDTO d = new KamioniDTO();
            d.setId(k.getPaisjeID());
            d.setEmri(k.getEmri());
            d.setStatus(k.getStatus().name());
            d.setLatitude(k.getVendndodhja().getLatitude());
            d.setLongitude(k.getVendndodhja().getLongitude());
            d.setTarga(k.getTarga());
            d.setKapaciteti(k.getKapaciteti());
            d.setOperatoriID(k.getOperatoriID());
            return d;
        }).collect(Collectors.toList());
        dataManager.writeList(TRUCKS_FILE, dtos);
    }

    @Override
    public Optional<Kamioni> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public List<Kamioni> findAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public List<Kamioni> findByOperatorId(String operatorId) {
        return cache.values().stream().filter(k -> operatorId.equals(k.getOperatoriID())).collect(Collectors.toList());
    }

    @Override
    public Kamioni save(Kamioni kamioni) {
        cache.put(kamioni.getPaisjeID(), kamioni);
        saveToFile();
        return kamioni;
    }

    @Override
    public boolean deleteById(String id) {
        boolean existed = cache.remove(id) != null;
        if (existed) saveToFile();
        return existed;
    }

    @Override
    public boolean existsById(String id) { return cache.containsKey(id); }

    @Override
    public long count() { return cache.size(); }
}
