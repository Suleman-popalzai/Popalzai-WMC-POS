// CALCULATOR PROGRAM

const display = document.getElementById("display");

let firstNumber = 0;
let currentOperator = "";


// Zahl anhängen
function appendToDisplay(value) {

    display.value += value;
}


// Alles löschen
function clearDisplay() {
    display.value = "";
}


// Operator speichern
function setOperator(operator) {
    firstNumber = Number(display.value);
    currentOperator = operator;
    display.value = "0";
}


// Rechnen
function calculate() {
display.value = eval(display.value);
}


