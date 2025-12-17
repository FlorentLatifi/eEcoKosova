package eco.kosova.presentation.config;

import eco.kosova.application.handlers.*;
import eco.kosova.domain.events.DomainEventPublisher;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.ZoneRepository;
import eco.kosova.domain.services.WasteMonitoringService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration për Application Layer handlers.
 */
@Configuration
public class HandlerConfig {
    
    // ========== COMMAND HANDLERS ==========
    
    @Bean
    public UpdateContainerFillLevelHandler updateContainerFillLevelHandler(
            WasteMonitoringService monitoringService
    ) {
        return new UpdateContainerFillLevelHandler(monitoringService);
    }
    
    @Bean
    public ScheduleCollectionHandler scheduleCollectionHandler(
            KontenierRepository kontenierRepository,
            DomainEventPublisher eventPublisher
    ) {
        return new ScheduleCollectionHandler(kontenierRepository, eventPublisher);
    }
    
    @Bean
    public EmptyContainerHandler emptyContainerHandler(
            KontenierRepository kontenierRepository,
            WasteMonitoringService monitoringService
    ) {
        return new EmptyContainerHandler(kontenierRepository, monitoringService);
    }
    
    @Bean
    public CreateZoneHandler createZoneHandler(
            ZoneRepository zoneRepository
    ) {
        return new CreateZoneHandler(zoneRepository);
    }
    
    @Bean
    public CreateContainerHandler createContainerHandler(
            KontenierRepository kontenierRepository,
            ZoneRepository zoneRepository
    ) {
        return new CreateContainerHandler(kontenierRepository, zoneRepository);
    }
    
    @Bean
    public UpdateContainerHandler updateContainerHandler(
            KontenierRepository kontenierRepository,
            ZoneRepository zoneRepository
    ) {
        return new UpdateContainerHandler(kontenierRepository, zoneRepository);
    }
    
    @Bean
    public DeleteContainerHandler deleteContainerHandler(
            KontenierRepository kontenierRepository,
            ZoneRepository zoneRepository
    ) {
        return new DeleteContainerHandler(kontenierRepository, zoneRepository);
    }
    
    @Bean
    public UpdateZoneHandler updateZoneHandler(
            ZoneRepository zoneRepository
    ) {
        return new UpdateZoneHandler(zoneRepository);
    }
    
    @Bean
    public DeleteZoneHandler deleteZoneHandler(
            ZoneRepository zoneRepository,
            KontenierRepository kontenierRepository
    ) {
        return new DeleteZoneHandler(zoneRepository, kontenierRepository);
    }
    
    // ========== QUERY HANDLERS ==========
    
    @Bean
    public GetContainerByIdHandler getContainerByIdHandler(
            KontenierRepository kontenierRepository
    ) {
        return new GetContainerByIdHandler(kontenierRepository);
    }
    
    @Bean
    public GetAllContainersHandler getAllContainersHandler(
            KontenierRepository kontenierRepository
    ) {
        return new GetAllContainersHandler(kontenierRepository);
    }
    
    @Bean
    public GetContainersByZoneHandler getContainersByZoneHandler(
            KontenierRepository kontenierRepository
    ) {
        return new GetContainersByZoneHandler(kontenierRepository);
    }
    
    @Bean
    public GetCriticalContainersHandler getCriticalContainersHandler(
            KontenierRepository kontenierRepository
    ) {
        return new GetCriticalContainersHandler(kontenierRepository);
    }
    
    @Bean
    public GetZoneStatisticsHandler getZoneStatisticsHandler(
            WasteMonitoringService monitoringService
    ) {
        return new GetZoneStatisticsHandler(monitoringService);
    }
}