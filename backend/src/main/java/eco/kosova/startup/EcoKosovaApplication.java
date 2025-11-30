package eco.kosova.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@EnableScheduling
public class EcoKosovaApplication {
    
    public static void main(String[] args) {
        printBanner();
        
        System.out.println("🚀 Starting EcoKosova Waste Management System...");
        System.out.println("📍 Location: Kosovo");
        System.out.println("🏗️ Architecture: DDD + Layered + CQRS");
        System.out.println();
        
        SpringApplication.run(EcoKosovaApplication.class, args);
        
        System.out.println();
        System.out.println("✅ EcoKosova System is running!");
        System.out.println("🌐 API available at: http://localhost:8080/api");
        System.out.println("📊 Monitoring endpoint: http://localhost:8080/api/monitoring/containers");
        System.out.println("🗺️ Zones endpoint: http://localhost:8080/api/zones/statistics");
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
        
        System.out.println(banner);
    }
}