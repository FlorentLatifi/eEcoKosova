package eco.kosova.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO për statistika të zonave.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneStatisticsDTO {
    private String zoneId;
    private String zoneName;
    private int totalContainers;
    private int criticalContainers;
    private int operationalContainers;
    private double averageFillLevel;
    private String status;
}

