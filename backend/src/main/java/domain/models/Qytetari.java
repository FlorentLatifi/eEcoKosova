package eco.kosova.domain.models;

import java.time.Instant;

/**
 * Entity: Qytetari
 * 
 * Përfaqëson përdoruesit e sistemit që hedhin mbetjet.
 * Sipas dokumentit konceptual.
 * 
 * Atribute:
 * - qytetariID: Identifikues unik
 * - emri: Emri i plotë i qytetarit
 * - adresa: Vendbanimi i qytetarit
 */
public class Qytetari {
    
    private final String id;
    private String name;
    private String address;
    private Instant createdAt;
    private Instant lastUpdated;
    
    /**
     * Constructor për Qytetari
     */
    public Qytetari(String id, String name, String address) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Qytetari ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        
        this.id = id;
        this.name = name;
        this.address = address; // Mund të jetë null
        this.createdAt = Instant.now();
        this.lastUpdated = Instant.now();
    }
    
    /**
     * Përditëson informacionin e qytetarit
     */
    public void updateInfo(String name, String address) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        this.address = address;
        this.lastUpdated = Instant.now();
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getLastUpdated() {
        return lastUpdated;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qytetari qytetari = (Qytetari) o;
        return id.equals(qytetari.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Qytetari{id='%s', name='%s', address='%s'}", id, name, address);
    }
}

