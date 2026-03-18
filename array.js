// Unsere Schüler-Liste zum Üben 
const students = [
  { name: "Anna", age: 17, grade: 2 },
  { name: "Ben", age: 16, grade: 4 },
  { name: "Clara", age: 18, grade: 1 },
  { name: "David", age: 17, grade: 5 }, 
  { name: "Elena", age: 16, grade: 3 },
  { name: "Felix", age: 19, grade: 2 },
  { name: "Gina", age: 17, grade: 1 }, 
  { name: "Hugo", age: 18, grade: 4 },
];

// TASK 1: Wer hat überhaupt bestanden? (Note 1-4)
// Filter gibt mir nur die zurück, bei denen die Bedingung stimmt
const bestanden = students.filter(schueler => schueler.grade <= 4);
console.log("Bestanden haben:", bestanden);

// TASK 2: Ich will für jeden so'n Label wie "Anna (17)"
// map durchläuft alle und baut neuen String für jeden
const labels = students.map(schueler => {
  return schueler.name + " (" + schueler.age + ")";
});
console.log("Labels:", labels);

// TASK 3: Erst die Bestandenen, dann nur deren Namen
// Voll praktisch, dass man filter und map verketten kann!
const namenDerBestandenen = students
  .filter(schueler => schueler.grade <= 4)  // erst filtern
  .map(schueler => schueler.name);           // dann namen holen
console.log("Namen der Bestandenen:", namenDerBestandenen);

// TASK 4: Notendurchschnitt berechnen
//  ist am Anfang komisch, aber praktisch...
const summe = students.reduce((zwischensumme, schueler) => {
  return zwischensumme + schueler.grade;
}, 0); // fang bei 0 an zu zählen

const durchschnitt = summe / students.length;
console.log("Klassendurchschnitt:", durchschnitt.toFixed(2)); // auf 2 Stellen gerundet

// TASK 5: BONUS - Alle ab 17 mit bestanden als schönen Text
// Das fand ich richtig cool! Alles in einem Durchgang
const aeltereBestandene = students
  .filter(schueler => schueler.age >= 17 && schueler.grade <= 4)  // zwei Bedingungen
  .map(schueler => schueler.name)                                 // nur Namen
  .join(", ");                                                    // zu Text mit Kommas
console.log("Ältere Schüler die bestanden haben:", aeltereBestandene);

// ich könnt noch schnell gucken, wer die 1er hat - nur so aus Spaß
const einserSchueler = students
  .filter(schueler => schueler.grade === 1)
  .map(schueler => schueler.name);
console.log("Die Einser-Kandidaten:", einserSchueler.join(" und "));