package eco.kosova.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO për raportet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private String id;
    private String title;
    private String description;
    private String type;
    private String generatedAt;
    private Map<String, Object> data;
}

