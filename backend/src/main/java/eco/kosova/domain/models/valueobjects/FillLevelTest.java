package eco.kosova.domain.models.valueobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests për FillLevel Value Object.
 */
class FillLevelTest {
    
    @Test
    void testValidFillLevel() {
        FillLevel level = new FillLevel(50);
        assertEquals(50, level.getValue());
    }
    
    @Test
    void testInvalidFillLevel_Negative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FillLevel(-1);
        });
    }
    
    @Test
    void testInvalidFillLevel_Over100() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FillLevel(101);
        });
    }
    
    @Test
    void testIsCritical() {
        FillLevel critical = new FillLevel(90);
        assertTrue(critical.isCritical());
        
        FillLevel notCritical = new FillLevel(89);
        assertFalse(notCritical.isCritical());
    }
    
    @Test
    void testIsWarning() {
        FillLevel warning = new FillLevel(75);
        assertTrue(warning.isWarning());
        
        FillLevel notWarning = new FillLevel(50);
        assertFalse(notWarning.isWarning());
    }
    
    @Test
    void testIsNormal() {
        FillLevel normal = new FillLevel(50);
        assertTrue(normal.isNormal());
        
        FillLevel notNormal = new FillLevel(75);
        assertFalse(notNormal.isNormal());
    }
    
    @Test
    void testIsEmpty() {
        FillLevel empty = new FillLevel(5);
        assertTrue(empty.isEmpty());
        
        FillLevel notEmpty = new FillLevel(20);
        assertFalse(notEmpty.isEmpty());
    }
    
    @Test
    void testEquality() {
        FillLevel level1 = new FillLevel(50);
        FillLevel level2 = new FillLevel(50);
        FillLevel level3 = new FillLevel(60);
        
        assertEquals(level1, level2);
        assertNotEquals(level1, level3);
    }
    
    @Test
    void testGetStatusText() {
        assertEquals("CRITICAL", new FillLevel(95).getStatusText());
        assertEquals("WARNING", new FillLevel(75).getStatusText());
        assertEquals("NORMAL", new FillLevel(50).getStatusText());
        assertEquals("EMPTY", new FillLevel(5).getStatusText());
    }
}