package eco.kosova.infrastructure.persistence;

import eco.kosova.domain.models.Qytetari;
import eco.kosova.domain.repositories.QytetariRepository;
import eco.kosova.infrastructure.persistence.jpa.QytetariEntity;
import eco.kosova.infrastructure.persistence.jpa.QytetariEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class QytetariRepositoryImpl implements QytetariRepository {
    
    private final QytetariEntityRepository qytetariEntityRepository;
    
    public QytetariRepositoryImpl(QytetariEntityRepository qytetariEntityRepository) {
        this.qytetariEntityRepository = qytetariEntityRepository;
    }
    
    @Override
    public Optional<Qytetari> findById(String id) {
        return qytetariEntityRepository.findById(id).map(this::toDomain);
    }
    
    @Override
    public List<Qytetari> findAll() {
        return qytetariEntityRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Qytetari> findByName(String name) {
        return qytetariEntityRepository.findByNameIgnoreCase(name).map(this::toDomain);
    }
    
    @Override
    public Qytetari save(Qytetari qytetari) {
        QytetariEntity entity = toEntity(qytetari);
        qytetariEntityRepository.save(entity);
        return qytetari;
    }
    
    @Override
    public List<Qytetari> saveAll(List<Qytetari> qytetaret) {
        List<QytetariEntity> entities = qytetaret.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
        qytetariEntityRepository.saveAll(entities);
        return qytetaret;
    }
    
    @Override
    public boolean deleteById(String id) {
        boolean existed = qytetariEntityRepository.existsById(id);
        if (existed) {
            qytetariEntityRepository.deleteById(id);
        }
        return existed;
    }
    
    @Override
    public boolean existsById(String id) {
        return qytetariEntityRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return qytetariEntityRepository.count();
    }
    
    // ========== PRIVATE HELPER METHODS ==========
    
    private Qytetari toDomain(QytetariEntity entity) {
        Qytetari qytetari = new Qytetari(entity.getId(), entity.getName(), entity.getAddress());
        // Note: Domain model doesn't expose setters for timestamps, so we can't set them
        // If needed, we could add a constructor that accepts timestamps
        return qytetari;
    }
    
    private QytetariEntity toEntity(Qytetari q) {
        QytetariEntity entity = new QytetariEntity();
        entity.setId(q.getId());
        entity.setName(q.getName());
        entity.setAddress(q.getAddress());
        entity.setCreatedAt(q.getCreatedAt());
        entity.setLastUpdated(q.getLastUpdated());
        return entity;
    }
}

