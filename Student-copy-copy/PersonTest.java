import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test-Klasse für Student
 * Wir testen:
 *  - 3 Männer (Untergewicht, Normalgewicht, Übergewicht)
 *  - 3 Frauen (Untergewicht, Normalgewicht, Übergewicht)
 *  - Fehler im Konstruktor
 */
public class PersonTest {

    // ===== Männer bereich =====

    @Test
    public void testMannUntergewicht() {
        
        
        Person s = new Person("Max", 50f, 170, 'm');
        assertEquals(17.3f, s.getBMI(), 0.1f);
        assertTrue(s.gewichtsklasse().contains("Untergewicht"));
    }

    @Test
    public void testMannNormalgewicht() {
        
        
        Person s = new Person("Ali", 70f, 175, 'm');
        assertEquals(22.9f, s.getBMI(), 0.1f);
        assertEquals("Normalgewicht", s.gewichtsklasse());
    }

    @Test
    public void testMannUebergewicht() {
        // 170 cm, 85 kg
        // BMI = 85 / (1.70 * 1.70) = 29.4
        Person s = new Person("Omar", 85f, 170, 'm');
        assertEquals(29.4f, s.getBMI(), 0.1f);
        assertTrue(s.gewichtsklasse().contains("Übergewicht"));
    }

    // ===== Frauen bereich=====

    @Test
    public void testFrauUntergewicht() {
        
        
        Person s = new Person("Mina", 45f, 170, 'w');
        assertEquals(15.6f, s.getBMI(), 0.1f);
        assertTrue(s.gewichtsklasse().contains("Untergewicht"));
    }

    @Test
    public void testFrauNormalgewicht() {
        
        
        Person s = new Person("Sara", 60f, 168, 'w');
        assertEquals(21.3f, s.getBMI(), 0.1f);
        assertEquals("Normalgewicht", s.gewichtsklasse());
    }

    @Test
    public void testFrauUebergewicht() {
        
        
        Person s = new Person("Nora", 80f, 165, 'w');
        assertEquals(29.4f, s.getBMI(), 0.1f);
        assertTrue(s.gewichtsklasse().contains("Übergewicht"));
    }

    // ===== Fehler im Konstruktor =====

    @Test
    public void testNameZuKurz() {
        
        assertThrows(IllegalArgumentException.class,
            () -> new Person("Al", 70f, 175, 'm'));
    }

    @Test
    public void testGroesseZuKlein() {
        
        assertThrows(IllegalArgumentException.class,
            () -> new Person("Alex", 70f, 40, 'm'));
    }

    @Test
    public void testGewichtZuKlein() {
        
        assertThrows(IllegalArgumentException.class,
            () -> new Person("Alex", 1.5f, 175, 'm'));
    }

    @Test
    public void testFalschesGender() {
        
        assertThrows(IllegalArgumentException.class,
            () -> new Person("Alex", 70f, 175, 'x'));
    }
}
