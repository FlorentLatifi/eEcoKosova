package eco.kosova.domain.models.valueobjects;

import java.util.Objects;

/**
 * Value Object për koordinatat gjeografike.
 * Immutable dhe self-validating.
 * 
 * Validon që koordinatat janë brenda kufijve të validë:
 * - Latitude: -90 deri 90
 * - Longitude: -180 deri 180
 */
public final class Coordinates {
    
    private final double latitude;
    private final double longitude;
    
    // Kufij validimi
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;
    
    public Coordinates(double latitude, double longitude) {
        validateCoordinates(latitude, longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    private void validateCoordinates(double lat, double lon) {
        if (lat < MIN_LATITUDE || lat > MAX_LATITUDE) {
            throw new IllegalArgumentException(
                String.format("Latitude must be between %.1f and %.1f. Received: %.6f", 
                    MIN_LATITUDE, MAX_LATITUDE, lat)
            );
        }
        
        if (lon < MIN_LONGITUDE || lon > MAX_LONGITUDE) {
            throw new IllegalArgumentException(
                String.format("Longitude must be between %.1f and %.1f. Received: %.6f", 
                    MIN_LONGITUDE, MAX_LONGITUDE, lon)
            );
        }
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * Llogarit distancën në metra deri te koordinata të tjera
     * Duke përdorur Haversine formula
     */
    public double distanceTo(Coordinates other) {
        final int EARTH_RADIUS_KM = 6371;
        
        double lat1Rad = Math.toRadians(this.latitude);
        double lat2Rad = Math.toRadians(other.latitude);
        double deltaLat = Math.toRadians(other.latitude - this.latitude);
        double deltaLon = Math.toRadians(other.longitude - this.longitude);
        
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS_KM * c * 1000; // Convert to meters
    }
    
    /**
     * Kontrollon nëse koordinatat janë në Kosovë
     * Kosovo bounds: Lat 41.8-43.3, Lon 19.9-21.8
     */
    public boolean isInKosovo() {
        return latitude >= 41.8 && latitude <= 43.3 &&
               longitude >= 19.9 && longitude <= 21.8;
    }
    
    /**
     * Formaton koordinatat për display
     */
    public String toDisplayString() {
        return String.format("%.6f°N, %.6f°E", latitude, longitude);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.latitude, latitude) == 0 &&
               Double.compare(that.longitude, longitude) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
    
    @Override
    public String toString() {
        return String.format("Coordinates{lat=%.6f, lon=%.6f}", latitude, longitude);
    }
}