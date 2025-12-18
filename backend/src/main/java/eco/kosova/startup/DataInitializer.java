package eco.kosova.startup;

import eco.kosova.domain.models.CikliMbledhjes;
import eco.kosova.domain.models.Qytetari;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.repositories.CikliMbledhjesRepository;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.QytetariRepository;
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

    @Autowired
    private QytetariRepository qytetariRepository;

    @Autowired
    private CikliMbledhjesRepository cikliMbledhjesRepository;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("📊 Initializing demo data for EcoKosova (H2)...");

        seedZonesIfEmpty();
        seedQytetaretIfEmpty();
        seedCikletIfEmpty();

        long containerCount = kontenierRepository.count();
        long zoneCount = zoneRepository.count();
        long qytetareCount = qytetariRepository.count();
        long cikleCount = cikliMbledhjesRepository.count();

        logger.info(String.format(
            "✅ Demo data ready! Zones: %d, Qytetarë: %d, Ciklet: %d, Containers: %d",
            zoneCount, qytetareCount, cikleCount, containerCount
        ));

        printDataSummary(containerCount, zoneCount);
    }

    private void seedZonesIfEmpty() {
        if (zoneRepository.count() > 0) {
            return;
        }

        logger.info("➡️ Seeding demo zones...");

        Zone zone1 = new Zone(
            "ZONE-001",
            "Qendra e Prishtinës",
            new Coordinates(42.6629, 21.1655),
            "Prishtinë"
        );

        Zone zone2 = new Zone(
            "ZONE-002",
            "Zona Industriale",
            new Coordinates(42.6729, 21.1755),
            "Prishtinë"
        );

        zoneRepository.saveAll(java.util.List.of(zone1, zone2));
    }

    private void seedQytetaretIfEmpty() {
        if (qytetariRepository.count() > 0) {
            return;
        }

        logger.info("➡️ Seeding demo qytetarë...");

        Qytetari q1 = new Qytetari("QYT-001", "Arben Hoxha", "Rr. Nënë Tereza 10");
        Qytetari q2 = new Qytetari("QYT-002", "Flora Berisha", "Rr. UÇK 25");

        qytetariRepository.saveAll(java.util.List.of(q1, q2));
    }

    private void seedCikletIfEmpty() {
        if (cikliMbledhjesRepository.count() > 0) {
            return;
        }

        logger.info("➡️ Seeding demo cikle mbledhjesi...");

        java.time.LocalDateTime now = java.time.LocalDateTime.now().plusHours(1);
        java.util.Set<java.time.DayOfWeek> days =
            java.util.EnumSet.of(java.time.DayOfWeek.MONDAY, java.time.DayOfWeek.WEDNESDAY, java.time.DayOfWeek.FRIDAY);

        CikliMbledhjes c1 = new CikliMbledhjes(
            "CIK-001",
            now,
            10000,
            days,
            "ZONE-001"
        );

        cikliMbledhjesRepository.save(c1);
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