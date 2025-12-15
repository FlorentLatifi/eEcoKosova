package eco.kosova.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDTO {
    private String id;
    private String name;
    private int criticalThreshold;
    private List<String> containerIds;
    private String status;
    private LocationDTO centerPoint;
}