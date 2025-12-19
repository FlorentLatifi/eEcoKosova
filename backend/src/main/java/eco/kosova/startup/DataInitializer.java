package eco.kosova.startup;

import eco.kosova.domain.models.CikliMbledhjes;
import eco.kosova.domain.models.Kamioni;
import eco.kosova.domain.models.Kontenier;
import eco.kosova.domain.models.Qytetari;
import eco.kosova.domain.models.Zone;
import eco.kosova.domain.models.valueobjects.Address;
import eco.kosova.domain.models.valueobjects.ContainerStatus;
import eco.kosova.domain.models.valueobjects.ContainerType;
import eco.kosova.domain.models.valueobjects.Coordinates;
import eco.kosova.domain.repositories.CikliMbledhjesRepository;
import eco.kosova.domain.repositories.KamioniRepository;
import eco.kosova.domain.repositories.KontenierRepository;
import eco.kosova.domain.repositories.QytetariRepository;
import eco.kosova.domain.repositories.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    private KamioniRepository kamioniRepository;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("📊 Initializing demo data for EcoKosova (H2)...");

        seedZonesIfEmpty();
        seedContainersIfEmpty();
        seedQytetaretIfEmpty();
        seedCikletIfEmpty();
        seedKamionetIfEmpty();

        long containerCount = kontenierRepository.count();
        long zoneCount = zoneRepository.count();
        long qytetareCount = qytetariRepository.count();
        long cikleCount = cikliMbledhjesRepository.count();
        long kamioneCount = kamioniRepository.count();

        logger.info(String.format(
            "✅ Demo data ready! Zones: %d, Containers: %d, Qytetarë: %d, Ciklet: %d, Kamionë: %d",
            zoneCount, containerCount, qytetareCount, cikleCount, kamioneCount
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
            new Coordinates(42.6720, 21.1750),
            "Prishtinë"
        );

        Zone zone3 = new Zone(
            "ZONE-003",
            "Lagjja Arbëria",
            new Coordinates(42.6519, 21.1545),
            "Prishtinë"
        );

        zoneRepository.saveAll(java.util.List.of(zone1, zone2, zone3));
    }

    private void seedContainersIfEmpty() {
        if (kontenierRepository.count() > 0) {
            return;
        }

        logger.info("➡️ Seeding demo containers...");

        // Për thjeshtësi, vendosim koordinata fikse në Prishtinë
        Coordinates qendra = new Coordinates(42.6629, 21.1655);
        Coordinates industriale = new Coordinates(42.6720, 21.1750);
        Coordinates arberia = new Coordinates(42.6519, 21.1545);

        Address addr1 = new Address("Rr. Nënë Tereza", "Prishtinë", "Prishtinë", "10000");
        Address addr2 = new Address("Rr. UÇK", "Prishtinë", "Prishtinë", "10000");
        Address addr3 = new Address("Rr. Bregu i Diellit", "Prishtinë", "Prishtinë", "10000");
        Address addr4 = new Address("Rr. Industriale 1", "Prishtinë", "Prishtinë", "10000");
        Address addr5 = new Address("Rr. Industriale 2", "Prishtinë", "Prishtinë", "10000");

        Kontenier c1 = new Kontenier("CONT-001", "ZONE-001", ContainerType.PLASTIC, 1000, qendra, addr1);
        Kontenier c2 = new Kontenier("CONT-002", "ZONE-001", ContainerType.GLASS, 800, qendra, addr2);
        Kontenier c3 = new Kontenier("CONT-003", "ZONE-001", ContainerType.GENERAL, 1500, qendra, addr3);
        Kontenier c4 = new Kontenier("CONT-004", "ZONE-002", ContainerType.ORGANIC, 1200, industriale, addr4);
        Kontenier c5 = new Kontenier("CONT-005", "ZONE-002", ContainerType.METAL, 600, industriale, addr5);

        // Përditësojmë nivelet e mbushjes për demonstrim
        c1.updateFillLevel(35);
        c2.updateFillLevel(92); // kritik
        c3.updateFillLevel(65);
        c4.updateFillLevel(48);
        c5.updateFillLevel(88); // warning

        kontenierRepository.saveAll(java.util.List.of(c1, c2, c3, c4, c5));
    }

    private void seedKamionetIfEmpty() {
        if (kamioniRepository.count() > 0) {
            return;
        }

        logger.info("➡️ Seeding demo kamionë...");

        Coordinates depo = new Coordinates(42.6660, 21.1600);
        Instant now = Instant.now();

        // Përdorim ditë në vend të viteve/muajve, pasi Instant nuk mbështet direkt YEARS/MONTHS
        Kamioni k1 = new Kamioni(
            "TRUCK-001",
            "Kamioni 1",
            "PR-100-AB",
            12000,
            depo,
            "OP-001",
            now.minus(365, ChronoUnit.DAYS)
        );

        Kamioni k2 = new Kamioni(
            "TRUCK-002",
            "Kamioni 2",
            "PR-200-CD",
            15000,
            depo,
            "OP-002",
            now.minus(180, ChronoUnit.DAYS)
        );

        kamioniRepository.saveAll(java.util.List.of(k1, k2));
    }

    private void seedQytetaretIfEmpty() {
        if (qytetariRepository.count() > 0) {
            return;
        }

        logger.info("➡️ Seeding demo qytetarë...");

        Qytetari q1 = new Qytetari("QYT-001", "Arben Hoxha", "Rr. Nënë Tereza 10");
        Qytetari q2 = new Qytetari("QYT-002", "Flora Berisha", "Rr. UÇK 25");
        Qytetari q3 = new Qytetari("QYT-003", "Luan Krasniqi", "Rr. Bregu i Diellit 7");
        Qytetari q4 = new Qytetari("QYT-004", "Arta Gashi", "Rr. Dardania 3");

        qytetariRepository.saveAll(java.util.List.of(q1, q2, q3, q4));
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

        CikliMbledhjes c2 = new CikliMbledhjes(
            "CIK-002",
            now.plusDays(1),
            8000,
            java.util.EnumSet.of(java.time.DayOfWeek.TUESDAY, java.time.DayOfWeek.THURSDAY),
            "ZONE-002"
        );

        cikliMbledhjesRepository.saveAll(java.util.List.of(c1, c2));
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