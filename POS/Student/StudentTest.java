import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Die Test-Klasse StudentTest.
 *
 * @author  Suleman Popalzai
 * @version 24.09.2025
 */
public class StudentTest {

    @Test
    public void testBMI_Normalgewicht() {
        Student s = new Student("Ali", 70, 175, 'm'); // BMI ca. 22.9
        assertEquals(22.9f, s.getBMI(), 0.1f);
        assertEquals("Normalgewicht", s.gewichtsklasse());
    }

    @Test
    public void testBMI_Untergewicht() {
        Student s = new Student("Mina", 45, 170, 'w'); // BMI ca. 15.6
        assertEquals(15.6f, s.getBMI(), 0.1f);
        assertEquals("starkes Untergewicht", s.gewichtsklasse());
    }

    @Test
    public void testBMI_Uebergewicht() {
        Student s = new Student("Max", 82, 170, 'm'); // BMI ca. 28.4
        assertEquals(28.4f, s.getBMI(), 0.1f);
        assertEquals("Übergewicht", s.gewichtsklasse());
    }
}
