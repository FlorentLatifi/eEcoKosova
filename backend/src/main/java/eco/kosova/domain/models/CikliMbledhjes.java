package eco.kosova.domain.models;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a collection cycle/schedule for waste collection.
 */
public class CikliMbledhjes {

    private LocalTime orari;
    private int kapacitetiMax;
    private final Set<DayOfWeek> dita; // days of week when collection happens

    public CikliMbledhjes(LocalTime orari, int kapacitetiMax, Set<DayOfWeek> dita) {
        this.orari = Objects.requireNonNull(orari, "orari cannot be null");
        if (kapacitetiMax <= 0) throw new IllegalArgumentException("kapacitetiMax must be positive");
        this.kapacitetiMax = kapacitetiMax;
        this.dita = new LinkedHashSet<>(Objects.requireNonNull(dita, "dita cannot be null"));
    }

    public LocalTime getOrari() { return orari; }
    public void setOrari(LocalTime orari) { this.orari = Objects.requireNonNull(orari); }
    public int getKapacitetiMax() { return kapacitetiMax; }
    public void setKapacitetiMax(int kapacitetiMax) { if (kapacitetiMax <= 0) throw new IllegalArgumentException("kapacitetiMax must be positive"); this.kapacitetiMax = kapacitetiMax; }
    public Set<DayOfWeek> getDita() { return Collections.unmodifiableSet(dita); }

    public void addDay(DayOfWeek day) { this.dita.add(Objects.requireNonNull(day)); }
    public void removeDay(DayOfWeek day) { this.dita.remove(day); }

    public boolean isScheduledOn(DayOfWeek day) { return this.dita.contains(day); }
}
