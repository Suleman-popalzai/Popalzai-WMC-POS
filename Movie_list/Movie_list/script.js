let filme = [];

const form = document.querySelector("#film-form");
const titel = document.querySelector("#titel");
const jahr = document.querySelector("#jahr");
const liste = document.querySelector("#liste");

form.addEventListener("submit", function (e) {
  e.preventDefault();

  if (!titel.value || !jahr.value ) {
    alert("Bitte alle Felder ausfuellen!");
    return;
  }

  const film = {
    id: Date.now(),
    titel: titel.value,
    jahr: jahr.value,
  };

  filme.push(film);
  zeigeFilme();
  form.reset();
});
function sortMeineFilmeAZ() {
  filme.sort(function (a, b) {
    let nameA = a.titel.toLowerCase();
    let nameB = b.titel.toLowerCase();
    return nameA.localeCompare(nameB); 
  });

  zeigeFilme();
}

function zeigeFilme() {
  liste.innerHTML = "";

  for (let i = 0; i < filme.length; i++) {
    const f = filme[i];

    const box = document.createElement("div");
    box.className = "film";

    const titelEl = document.createElement("h3");
    titelEl.textContent = f.titel;

    const jahrEl = document.createElement("p");
    jahrEl.textContent = "Jahr: " + f.jahr;

  

    const loeschen = document.createElement("button");
    loeschen.textContent = "Dalete 🗑️";
    loeschen.onclick = function () {
      filme = filme.filter(eintrag => eintrag.id !== f.id);
      zeigeFilme();
    };

    box.appendChild(titelEl);
    box.appendChild(jahrEl);
    box.appendChild(loeschen);

    liste.appendChild(box);
  }
}
