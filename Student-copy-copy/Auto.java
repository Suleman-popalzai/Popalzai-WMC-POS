
/**
 * Beschreiben Sie hier die Klasse Auto.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Auto
{
    // Eigenschaften vom Auto
    private String kennzeichen; // Nummer vom Auto
    private int sitze;          // wie viele Plätze hat das Auto
    private int drin;           // wie viele Leute sitzen gerade drin

    // Konstruktor: neues Auto anlegen
    public Auto(String kennzeichen, int sitze) {
        setKennzeichen(kennzeichen);
        setSitze(sitze);
        this.drin = 0; // am Anfang sitzt niemand drin
    }

    // Kennzeichen setzen
    public void setKennzeichen(String kennzeichen) {
        if (kennzeichen == null || kennzeichen.trim().isEmpty()) {
            throw new IllegalArgumentException("Kennzeichen darf nicht leer sein");
        }
        this.kennzeichen = kennzeichen;
    }

    public String getKennzeichen() {
        return kennzeichen;
    }

    // Sitze setzen
    public void setSitze(int sitze) {
        if (sitze < 1 || sitze > 9) {
            throw new IllegalArgumentException("Sitze müssen 1 bis 9 sein");
        }
        this.sitze = sitze;
    }

    public int getSitze() {
        return sitze;
    }

    public int getDrin() {
        return drin;
    }

    // Leute steigen ein
    public void einsteigen(int personen) {
        if (personen <= 0) {
            throw new IllegalArgumentException("Mindestens 1 Person muss einsteigen");
        }
        if (drin + personen > sitze) {
            throw new IllegalStateException("Kein Platz mehr");
        }
        drin = drin + personen;
    }

    // Leute steigen aus
    public void aussteigen(int personen) {
        if (personen <= 0) {
            throw new IllegalArgumentException("Mindestens 1 Person muss aussteigen");
        }
        if (personen > drin) {
            throw new IllegalStateException("So viele sind nicht im Auto");
        }
        drin = drin - personen;
    }

    // Kurzformen: eine Person ein/aus
    public void einsteigen() {
        einsteigen(1);
    }

    public void aussteigen() {
        aussteigen(1);
    }

    // Info-Text
    public String info() {
        return "Auto " + kennzeichen + ": " + drin + " von " + sitze + " Plätzen belegt";
    }
}


