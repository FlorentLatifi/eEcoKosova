package eco.kosova.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.logging.Logger;

/**
 * Main Application Entry Point për EcoKosova Waste Management System.
 * 
 * @SpringBootApplication kombinon:
 * - @Configuration
 * - @EnableAutoConfiguration
 * - @ComponentScan
 */
@SpringBootApplication
@ComponentScan(basePackages = "eco.kosova")
@EntityScan(basePackages = "eco.kosova.infrastructure.persistence.jpa")
@EnableJpaRepositories(basePackages = "eco.kosova.infrastructure.persistence.jpa")
@EnableScheduling
public class EcoKosovaApplication {
    
    private static final Logger logger = Logger.getLogger(EcoKosovaApplication.class.getName());
    
    public static void main(String[] args) {
        printBanner();
        
        logger.info("🚀 Starting EcoKosova Waste Management System...");
        logger.info("📍 Location: Kosovo");
        logger.info("🏗️ Architecture: DDD + Layered + CQRS");
        logger.info("");
        
        SpringApplication.run(EcoKosovaApplication.class, args);
        
        logger.info("");
        logger.info("✅ EcoKosova System is running!");
        logger.info("🌐 API available at: http://localhost:8080/api");
        logger.info("📊 Monitoring endpoint: http://localhost:8080/api/monitoring/containers");
        logger.info("🗺️ Zones endpoint: http://localhost:8080/api/zones/statistics");
        logger.info("💚 Health check: http://localhost:8080/actuator/health");
    }
    
    private static void printBanner() {
        String banner = """
                
                ███████╗ ██████╗ ██████╗ ██╗  ██╗ ██████╗ ███████╗ ██████╗ ██╗   ██╗ █████╗ 
                ██╔════╝██╔════╝██╔═══██╗██║ ██╔╝██╔═══██╗██╔════╝██╔═══██╗██║   ██║██╔══██╗
                █████╗  ██║     ██║   ██║█████╔╝ ██║   ██║███████╗██║   ██║██║   ██║███████║
                ██╔══╝  ██║     ██║   ██║██╔═██╗ ██║   ██║╚════██║██║   ██║╚██╗ ██╔╝██╔══██║
                ███████╗╚██████╗╚██████╔╝██║  ██╗╚██████╔╝███████║╚██████╔╝ ╚████╔╝ ██║  ██║
                ╚══════╝ ╚═════╝ ╚═════╝ ╚═╝  ╚═╝ ╚═════╝ ╚══════╝ ╚═════╝   ╚═══╝  ╚═╝  ╚═╝
                
                         🌿 Waste Management System for Kosovo 🌿
                                    Version 1.0.0
                """;
        
        logger.info(banner);
    }
}