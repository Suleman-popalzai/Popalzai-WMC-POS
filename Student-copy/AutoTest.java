

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Die Test-Klasse AutoTest.
 *
 * @author  (Ihr Name)
 * @version (eine Versionsnummer oder ein Datum)
 */

public class AutoTest {

    @Test
    public void testEinUndAussteigen() {
        // neues Auto mit 4 Sitzen
        Auto a = new Auto("W-123ABC", 4);
        assertEquals(0, a.getDrin()); // am Anfang sitzt niemand drin

        a.einsteigen();       // 1 Person steigt ein
        a.einsteigen(2);      // noch 2 Personen -> jetzt 3 drin
        assertEquals(3, a.getDrin());

        a.aussteigen();       // 1 Person steigt aus -> jetzt 2 drin
        assertEquals(2, a.getDrin());
    }

    @Test
    public void testZuVieleSteigenEin() {
        // Auto mit 2 Sitzen
        Auto a = new Auto("W-1", 2);
        a.einsteigen(2); // beide Plätze belegt
        // noch eine Person -> kein Platz mehr -> Fehler erwartet
        assertThrows(IllegalStateException.class, () -> a.einsteigen(1));
    }

    @Test
    public void testMehrAussteigenAlsDrin() {
        Auto a = new Auto("W-2", 4);
        a.einsteigen(2); // 2 Personen sind drin
        // 3 Personen aussteigen -> geht nicht -> Fehler erwartet
        assertThrows(IllegalStateException.class, () -> a.aussteigen(3));
    }

    @Test
    public void testFalscheWerte() {
        // leeres Kennzeichen -> Fehler
        assertThrows(IllegalArgumentException.class, () -> new Auto("", 4));

        // 0 Sitze -> Fehler
        assertThrows(IllegalArgumentException.class, () -> new Auto("W-3", 0));

        Auto a = new Auto("W-4", 4);

        // 0 Personen einsteigen -> Fehler
        assertThrows(IllegalArgumentException.class, () -> a.einsteigen(0));

        // 0 Personen aussteigen -> Fehler
        assertThrows(IllegalArgumentException.class, () -> a.aussteigen(0));
    }
}


