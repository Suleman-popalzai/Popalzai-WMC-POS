public class Person {
    private String name;
    private float kg;
    private int cm;
    private char gender;

    public Person(String name, float kg, int cm, char gender) {
        setName(name);
        setKg(kg);
        setCm(cm);
        setGender(gender);
    }

    public Person(String name, boolean isMann, int cm, int kg) {
        setName(name);
        setKg(kg);
        setCm(cm);
        setGender(isMann ? 'm' : 'w'); // hier habe ich 'w' statt 'f' genommen, damit es passt
    }

    public void setName(String name) {
        if (name.length() < 3 || name.length() > 50) {
            throw new IllegalArgumentException("Name muss 3-50 Zeichen haben");
        }
        this.name = name;
    }

    public void setKg(float kg) {
        if (kg < 2 || kg > 635) {
            throw new IllegalArgumentException("Ungültiges Gewicht");
        }
        this.kg = kg;
    }

    public void setCm(int cm) {
        if (cm < 50 || cm > 250) {
            throw new IllegalArgumentException("Ungültige Größe");
        }
        this.cm = cm;
    }

    public void setGender(char gender) {
        gender = Character.toLowerCase(gender);
        if (gender != 'm' && gender != 'w') {
            throw new IllegalArgumentException("Geschlecht muss m oder w sein");
        }
        this.gender = gender;
    }

    // BMI berechnen: Gewicht durch Größe in Metern zum Quadrat
    public float bmi() {
        float meter = cm / 100.0f;
        return kg / (meter * meter);
    }

    // Alias für die Tests
    public float getBMI() {
        return bmi();
    }

     public String gewichtsklasse() {
        float bmi = getBMI();
        if (bmi < 16f) {
            return "starkes Untergewicht";
        } else if (bmi < 18.5f) {
            return "Untergewicht";
        } else if (bmi < 25f) {
            return "Normalgewicht";
        } else if (bmi < 30f) {
            return "Übergewicht";             
        } else if (bmi < 35f) {
            return "Adipositas I";
        } else if (bmi < 40f) {
            return "Adipositas II";
        } else {
            return "Adipositas III";
        }
    }

    

    public String mannOderFrau() {
        return (gender == 'm') ? "männlich" : "weiblich";
    }

    public String printStudent() {
        return "Name: " + name
             + " (" + mannOderFrau() + "), "
             + kg + "kg, "
             + cm + "cm ("
             + gewichtsklasse() + ")";
    }
}