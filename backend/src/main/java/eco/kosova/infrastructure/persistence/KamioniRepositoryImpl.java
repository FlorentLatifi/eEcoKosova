package eco.kosova.infrastructure.persistence;

import eco.kosova.domain.models.Kamioni;
import eco.kosova.domain.models.Paisje;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.repositories.KamioniRepository;
import eco.kosova.infrastructure.persistence.jpa.KamioniEntity;
import eco.kosova.infrastructure.persistence.jpa.KamioniEntityRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class KamioniRepositoryImpl implements KamioniRepository {

    private final KamioniEntityRepository kamioniEntityRepository;

    public KamioniRepositoryImpl(KamioniEntityRepository kamioniEntityRepository) {
        this.kamioniEntityRepository = kamioniEntityRepository;
    }

    @Override
    public Optional<Kamioni> findById(String id) {
        return kamioniEntityRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Kamioni> findAll() {
        return kamioniEntityRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Kamioni> findByOperatorId(String operatorId) {
        return kamioniEntityRepository.findByOperatorId(operatorId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Kamioni> findAvailable() {
        return kamioniEntityRepository.findAvailable().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Kamioni> findByStatus(Paisje.PaisjeStatus status) {
        return kamioniEntityRepository.findByStatus(status.name()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Kamioni> findByLicensePlate(String licensePlate) {
        return kamioniEntityRepository.findByLicensePlate(licensePlate).map(this::toDomain);
    }

    @Override
    public List<Kamioni> findByRouteId(String routeId) {
        return kamioniEntityRepository.findByRouteId(routeId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Kamioni save(Kamioni kamioni) {
        KamioniEntity entity = toEntity(kamioni);
        kamioniEntityRepository.save(entity);
        return kamioni;
    }

    @Override
    public List<Kamioni> saveAll(List<Kamioni> kamionet) {
        List<KamioniEntity> entities = kamionet.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
        kamioniEntityRepository.saveAll(entities);
        return kamionet;
    }

    @Override
    public boolean deleteById(String id) {
        boolean existed = kamioniEntityRepository.existsById(id);
        if (existed) {
            kamioniEntityRepository.deleteById(id);
        }
        return existed;
    }

    @Override
    public boolean existsById(String id) {
        return kamioniEntityRepository.existsById(id);
    }

    @Override
    public long count() {
        return kamioniEntityRepository.count();
    }

    @Override
    public long countAvailable() {
        return kamioniEntityRepository.countAvailable();
    }

    // ========== PRIVATE HELPER METHODS ==========

    private Kamioni toDomain(KamioniEntity entity) {
        Coordinates coords = new Coordinates(entity.getLatitude(), entity.getLongitude());
        Paisje.PaisjeStatus status = Paisje.PaisjeStatus.valueOf(entity.getStatus());

        Kamioni kamioni = new Kamioni(
            entity.getId(),
            entity.getName(),
            entity.getLicensePlate(),
            entity.getCapacity(),
            coords,
            entity.getOperatorId(),
            entity.getInstallationDate()
        );

        // Set status manually since it's in the parent class
        kamioni.updateStatus(status);

        // Restore route and assigned containers if they exist
        // Note: We restore route only if kamioni is available (routes are temporary)
        if (entity.getCurrentRouteId() != null && !entity.getCurrentRouteId().isBlank() && kamioni.isAvailable()) {
            try {
                kamioni.assignToRoute(entity.getCurrentRouteId(), entity.getAssignedContainerIds());
            } catch (Exception e) {
                // If assignment fails, route won't be restored (this is acceptable)
            }
        }

        return kamioni;
    }

    private KamioniEntity toEntity(Kamioni kamioni) {
        KamioniEntity entity = new KamioniEntity();
        entity.setId(kamioni.getId());
        entity.setName(kamioni.getName());
        entity.setStatus(kamioni.getStatus().name());
        entity.setLatitude(kamioni.getLocation().getLatitude());
        entity.setLongitude(kamioni.getLocation().getLongitude());
        entity.setInstallationDate(kamioni.getInstallationDate());
        entity.setLastUpdated(kamioni.getLastUpdated());
        entity.setLicensePlate(kamioni.getLicensePlate());
        entity.setCapacity(kamioni.getCapacity());
        entity.setOperatorId(kamioni.getOperatorId());
        entity.setCurrentRouteId(kamioni.getCurrentRouteId());
        entity.setAssignedContainerIds(new java.util.ArrayList<>(kamioni.getAssignedContainerIds()));
        entity.setCreatedAt(Instant.now());
        return entity;
    }
}

