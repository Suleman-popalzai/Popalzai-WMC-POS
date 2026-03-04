// Meine HÜ - hab lang gebraucht aber jetzt check ich es (hoffentlich)
// Aufgabe: Sortieren nach Typ und dann nach Wert

let a = [23, false, -7n, true, "Omar", {age: 19}];

a.sort(function vergleich(e1, e2) {
 // erstmal schauen was das für sachen sind
    let typ1 = typeof e1;
    let typ2 = typeof e2;
    
 //  verschiedene Typen -> nach Typ sortieren

    if (typ1 !== typ2) {
     // ich geb jedem typ eine nummer für die reihenfolge
     // boolean soll zuerst kommen (hab ich so entschieden)
     // dann number, dann bigint, dann string, dann object
        
     let nummer1 = 0;
     let nummer2 = 0;
        
    // für e1 die nummer rausfinden
     if (typ1 === "boolean") {
     nummer1 = 1; }
     if (typ1 === "number") {
         nummer1 = 2;
      }
     if (typ1 === "bigint") {
            nummer1 = 3;
        }
      if (typ1 === "string") {
         nummer1 = 4;
        }
     if (typ1 === "object") {
          nummer1 = 5;
        }
        
     // für e2 die nummer rausfinden
    if (typ2 === "boolean") {
         nummer2 = 1;
        }
     if (typ2 === "number") {
          nummer2 = 2;
      }
     if (typ2 === "bigint") {
            nummer2 = 3;
        }
      if (typ2 === "string") {
            nummer2 = 4;
        }
      if (typ2 === "object") {
            nummer2 = 5;
        }
        
     // wenn nummer1 kleiner ist kommt e1 vor e2
     if (nummer1 < nummer2) {
         return -1; // minus = e1 zuerst
        }
      if (nummer1 > nummer2) {
            return 1; // plus = e2 zuerst
        }
        // fall sie gleich sind (kann eigentlich nicht passieren)
        return 0;
    }
    
    
 //  gleiche Typen -> nach Wert sortieren

    //  BOOLEANS 
    if (typ1 === "boolean") {
        // false ist wie 0, true ist wie 1
        // also false kommt vor true
        if (e1 === false && e2 === true) {
            return -1; // false vor true
        }
        if (e1 === true && e2 === false) {
            return 1; // true nach false
        }
        return 0; // beide false oder beide true
    }
    
    //  NUMBERS 
    if (typ1 === "number") {
        // kleine zahlen zuerst
        if (e1 < e2) {
            return -1;
        }
        if (e1 > e2) {
            return 1;
        }
        return 0; // gleich
    }
    
    //  BIGINTS
    if (typ1 === "bigint") {
        // bigints sind mir noch nicht ganz klar aber < und > geht glaub ich
        if (e1 < e2) {
            return -1;
        }
        if (e1 > e2) {
            return 1;
        }
        return 0;
    }
    
    //  STRINGS 
    if (typ1 === "string") {
        // strings alphabetisch, hab ich im internet gefunden wie das geht
        // A < B < C usw.
        if (e1 < e2) {
            return -1;
        }
        if (e1 > e2) {
            return 1;
        }
        return 0; // gleicher string
    }
    
    //  OBJECTS 
    if (typ1 === "object") {
        // objects haben alle ein alter (age) weil in der aufgabe steht {age:11}
        // also sortier ich nach alter
        if (e1.age < e2.age) {
            return -1;
        }
        if (e1.age > e2.age) {
            return 1;
        }
        return 0; // gleiches alter
    }
    
    // falls irgendwas komisches passiert
    return 0;
});

// ausgabe damit ich seh obs funktioniert
console.log("MEIN SORTIERTES ARRAY:");
console.log(a);

// nochmal mit typen damit ich sicher bin
console.log("");
console.log("KONTROLLE:");
for (let i = 0; i < a.length; i++) {
    console.log(i + 1 + ". " + typeof a[i] + " -> " + a[i]);
}

console.log("");
console.log("Erklärung:");
console.log("1. zuerst alle booleans (false vor true)");
console.log("2. dann zahlen (23)");
console.log("3. dann bigints (-7n)");
console.log("4. dann strings (Omar)");
console.log("5. dann objects (nach age: 11)");