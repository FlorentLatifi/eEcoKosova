package eco.kosova.application.handlers;

import eco.kosova.application.queries.GetZoneStatisticsQuery;
import eco.kosova.domain.services.WasteMonitoringService;

import java.util.List;

/**
 * Handler për GetZoneStatisticsQuery.
 */
public class GetZoneStatisticsHandler {
    
    private final WasteMonitoringService monitoringService;
    
    public GetZoneStatisticsHandler(WasteMonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }
    
    public List<WasteMonitoringService.ZoneStatistics> handle(GetZoneStatisticsQuery query) {
        return monitoringService.getZoneStatistics();
    }
}