
/**
 * Beschreiben Sie hier die Klasse Student.
 * 
 * @author (Suleman Popalzai) 
 * @version (13.09.2025)
 */
public class Student
{
    // Attribute (Eigenschaften)
    private String name;
    private float kg;
    private int cm;
    private char gender; // also 'm' für männlich und 'w'für weiblich 

    // Konstruktor mit allen Attributen
    public Student(String name, float kg, int cm, char gender) {
        this.setName(name);
        this.setKg(kg);
        this.setCm(cm);
        this.setGender(gender);
    }

    // -- Setter 
    public void setName(String name) {
        this.name = name;
    }

    public void setKg(float kg) {
        this.kg = kg;
    }

    public void setCm(int cm) {
        this.cm = cm;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    // -- Getter 
    public String getName() {
        return this.name;
    }

    public float getKg() {
        return this.kg;
    }

    public int getCm() {
        return this.cm;
    }

    public char getGender() {
        return this.gender;
    }

    // BMI berechnen: Gewicht / (Größe in m)^2
    public float berechneBMI() {
        float groesseInMeter = this.cm / 100.0f;
        return this.kg / (groesseInMeter * groesseInMeter);
    }

    // Männlich oder weiblich als Wort
    public String mannOderFrau() {
        if (this.gender == 'm') {
            return "männlich";
        } else {
            return "weiblich";
        }
    }

    // Gewichtskategorie bestimmen
    public String gewichtskategorie() {
        float bmi = berechneBMI();
        if (bmi < 18.5f) {
            return "Untergewicht";
        } else if (bmi < 25f) {
            return "Normalgewicht";
        } else {
            return "Übergewicht";
        }
    }

    // Ausgabe-Methode
    public void printStudent() {
        System.out.println(
            "Name: " + this.name + " (" + mannOderFrau() + "), " +
            this.kg + "kg, " + this.cm + "cm (" + gewichtskategorie() + ")"
        );
    }
}
