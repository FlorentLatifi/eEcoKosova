package eco.kosova.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerDTO {
    private String id;
    private String zoneId;
    private String type;
    private int fillLevel;
    private int capacity;
    private String status;
    private LocationDTO location;
    private String lastUpdated;
}