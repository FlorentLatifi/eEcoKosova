package eco.kosova.domain.models;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CikliMbledhjesKontrollPanelTest {

    @Test
    public void cikliAndPanelBasicBehavior() {
        CikliMbledhjes cikli = new CikliMbledhjes(LocalTime.of(8,0), 2000, Set.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY));
        assertTrue(cikli.isScheduledOn(DayOfWeek.MONDAY));
        assertFalse(cikli.isScheduledOn(DayOfWeek.SUNDAY));

        cikli.addDay(DayOfWeek.SUNDAY);
        assertTrue(cikli.isScheduledOn(DayOfWeek.SUNDAY));

        KontrollPanel panel = new KontrollPanel("panel-1", "sq", "light", "HOME");
        assertEquals("panel-1", panel.getPanelID());
        assertEquals("sq", panel.getGjuha());

        panel.setGjuha("en");
        assertEquals("en", panel.getGjuha());
    }
}
