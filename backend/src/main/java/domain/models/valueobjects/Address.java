package eco.kosova.domain.models.valueobjects;

import java.util.Objects;

/**
 * Value Object për adresën fizike.
 * Immutable dhe self-validating.
 */
public final class Address {
    
    private final String street;
    private final String city;
    private final String municipality;
    private final String postalCode;
    
    public Address(String street, String city, String municipality, String postalCode) {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        if (municipality == null || municipality.isBlank()) {
            throw new IllegalArgumentException("Municipality cannot be empty");
        }
        
        this.street = street.trim();
        this.city = city.trim();
        this.municipality = municipality.trim();
        this.postalCode = postalCode != null ? postalCode.trim() : "";
    }
    
    public String getStreet() {
        return street;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getMunicipality() {
        return municipality;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    /**
     * Formaton adresën për display
     */
    public String toFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(street);
        sb.append(", ").append(city);
        sb.append(", ").append(municipality);
        if (!postalCode.isEmpty()) {
            sb.append(" ").append(postalCode);
        }
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
               Objects.equals(city, address.city) &&
               Objects.equals(municipality, address.municipality) &&
               Objects.equals(postalCode, address.postalCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(street, city, municipality, postalCode);
    }
    
    @Override
    public String toString() {
        return toFullAddress();
    }
}