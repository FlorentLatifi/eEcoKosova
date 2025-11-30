package eco.kosova.presentation.config;

import eco.kosova.domain.events.DomainEventPublisher;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.ZoneRepository;
import eco.kosova.domain.services.RouteOptimizationService;
import eco.kosova.domain.services.WasteMonitoringService;
import eco.kosova.infrastructure.persistence.JsonDataManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Configuration për aplikacionin.
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AppConfig {
    
    /**
     * CORS configuration për të lejuar frontend access
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
    
    /**
     * JsonDataManager bean
     */
    @Bean
    public JsonDataManager jsonDataManager() {
        return new JsonDataManager();
    }
    
    /**
     * Domain Services beans
     */
    @Bean
    public WasteMonitoringService wasteMonitoringService(
            KontenierRepository kontenierRepository,
            ZoneRepository zoneRepository,
            DomainEventPublisher eventPublisher
    ) {
        return new WasteMonitoringService(
            kontenierRepository,
            zoneRepository,
            eventPublisher
        );
    }
    
    @Bean
    public RouteOptimizationService routeOptimizationService(
            KontenierRepository kontenierRepository
    ) {
        return new RouteOptimizationService(kontenierRepository);
    }
}