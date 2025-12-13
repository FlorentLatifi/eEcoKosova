package eco.kosova.domain.models;

import eco.kosova.domain.models.valueobjects.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QytetariOperatoriTest {

    @Test
    public void qytetariAndOperatoriBasic() {
        Address addr = new Address("Rruga 1", "Prishtine", "Komuna", "10000");
        Qytetari q = new Qytetari("q-1", "Emri Qytetarit", addr);
        assertEquals("q-1", q.getQytetariID());
        assertEquals(addr, q.getAdresa());

        Operatori o = new Operatori("op-1", "Emri Operatorit", "+38344123456");
        assertEquals("op-1", o.getOperatoriID());
        assertEquals("Emri Operatorit", o.getEmri());
    }
}
