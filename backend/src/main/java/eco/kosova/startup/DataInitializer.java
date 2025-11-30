package eco.kosova.startup;

import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Initializer që ekzekutohet kur aplikacioni starton.
 * Ngarkon të dhënat nga JSON files.
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = Logger.getLogger(
        DataInitializer.class.getName()
    );
    
    @Autowired
    private KontenierRepository kontenierRepository;
    
    @Autowired
    private ZoneRepository zoneRepository;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("📊 Initializing data from JSON files...");
        
        // Load repositories (që do të ngarkojnë data nga JSON)
        long containerCount = kontenierRepository.count();
        long zoneCount = zoneRepository.count();
        
        logger.info(String.format(
            "✅ Data loaded successfully! Containers: %d, Zones: %d",
            containerCount, zoneCount
        ));
        
        // Print summary
        printDataSummary(containerCount, zoneCount);
    }
    
    private void printDataSummary(long containers, long zones) {
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         DATA INITIALIZATION            ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║  Containers Loaded: %-18d ║%n", containers);
        System.out.printf("║  Zones Loaded: %-23d ║%n", zones);
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }
}