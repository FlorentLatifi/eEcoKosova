package eco.kosova.startup;

import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Initializer që ekzekutohet kur aplikacioni starton.
 * Verifikon që të dhënat bazë ekzistojnë në databazë.
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
        logger.info("📊 Initializing data from database (MSSQL)...");
        
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
        logger.info("");
        logger.info("╔════════════════════════════════════════╗");
        logger.info("║         DATA INITIALIZATION            ║");
        logger.info("╠════════════════════════════════════════╣");
        logger.info(String.format("║  Containers Loaded: %-18d ║", containers));
        logger.info(String.format("║  Zones Loaded: %-23d ║", zones));
        logger.info("╚════════════════════════════════════════╝");
        logger.info("");
    }
}