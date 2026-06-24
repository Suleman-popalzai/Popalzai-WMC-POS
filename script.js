const STORAGE_KEY = 'cinelog_v1';

let state = {
  movies: [],
  search: '',
  sort: 'added',
  filterYear: ''
};

function loadFromStorage() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (raw) state.movies = JSON.parse(raw);
  } catch(e) { state.movies = []; }
}

function saveToStorage() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state.movies));
}

/* ══════════════════════════════════════════
   DERIVED / HELPERS
   ══════════════════════════════════════════ */
function uid() {
  return Date.now().toString(36) + Math.random().toString(36).slice(2,7);
}

function fmtDuration(min) {
  if (!min) return '—';
  const h = Math.floor(min / 60), m = min % 60;
  return h ? `${h}h ${m}m` : `${m}m`;
}

function filteredMovies() {
  let list = [...state.movies];

  // search
  const q = state.search.toLowerCase();
  if (q) list = list.filter(m =>
    m.title.toLowerCase().includes(q) ||
    (m.genre || '').toLowerCase().includes(q)
  );

  // filter year
  if (state.filterYear) list = list.filter(m => String(m.year) === state.filterYear);

  // sort
  switch (state.sort) {
    case 'title':        list.sort((a,b) => a.title.localeCompare(b.title)); break;
    case 'year-desc':    list.sort((a,b) => (b.year||0) - (a.year||0)); break;
    case 'year-asc':     list.sort((a,b) => (a.year||0) - (b.year||0)); break;
    case 'rating-desc':  list.sort((a,b) => (b.rating||0) - (a.rating||0)); break;
    case 'duration-desc':list.sort((a,b) => (b.duration||0) - (a.duration||0)); break;
    default:             list.sort((a,b) => b._added - a._added);
  }

  return list;
}

function updateStats() {
  const all = state.movies;
  document.getElementById('stat-total').textContent = all.length;

  const rated = all.filter(m => m.rating);
  document.getElementById('stat-avg').textContent = rated.length
    ? (rated.reduce((s,m) => s + parseFloat(m.rating), 0) / rated.length).toFixed(1)
    : '—';

  const totMin = all.reduce((s,m) => s + (parseInt(m.duration)||0), 0);
  document.getElementById('stat-dur').textContent = totMin ? `${Math.round(totMin/60)}h` : '0h';
}

function updateYearFilter() {
  const sel = document.getElementById('filter-year');
  const current = sel.value;
  const years = [...new Set(state.movies.map(m => m.year).filter(Boolean))].sort((a,b) => b-a);
  sel.innerHTML = '<option value="">All years</option>' +
    years.map(y => `<option value="${y}" ${y==current?'selected':''}>${y}</option>`).join('');
}

/* ══════════════════════════════════════════
   RENDER UI  (State → DOM)
   ══════════════════════════════════════════ */
function render() {
  const grid = document.getElementById('grid');
  const empty = document.getElementById('empty');
  const list = filteredMovies();

  // remove existing cards (keep #empty)
  [...grid.querySelectorAll('.movie-card')].forEach(c => c.remove());

  if (!list.length) {
    empty.style.display = 'block';
    updateStats();
    return;
  }
  empty.style.display = 'none';

  list.forEach(movie => {
    const card = document.createElement('div');
    card.className = 'movie-card';
    card.dataset.id = movie.id;

    const posterHtml = movie.poster
      ? `<img src="${escHtml(movie.poster)}" alt="${escHtml(movie.title)}" loading="lazy" onerror="this.style.display='none';this.nextElementSibling.style.display='flex'">`
      : '';
    const phStyle = movie.poster ? 'style="display:none"' : '';

    const trailerHtml = movie.trailer
      ? `<a class="trailer-link" href="${escHtml(movie.trailer)}" target="_blank" rel="noopener">
          <svg viewBox="0 0 24 24"><polygon points="5 3 19 12 5 21 5 3"/></svg>
          Watch trailer
        </a>`
      : '';

    card.innerHTML = `
      <div class="poster-wrap">
        ${posterHtml}
        <div class="poster-placeholder" ${phStyle}>🎬</div>
        ${movie.rating ? `<div class="card-badge">★ ${parseFloat(movie.rating).toFixed(1)}</div>` : ''}
        <div class="card-actions">
          <button class="icon-btn" data-action="edit" title="Edit">
            <svg viewBox="0 0 24 24"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
          </button>
          <button class="icon-btn" data-action="delete" title="Delete" style="color:#e74c3c">
            <svg viewBox="0 0 24 24"><polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/><path d="M10 11v6M14 11v6"/><path d="M9 6V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/></svg>
          </button>
        </div>
      </div>
      <div class="card-body">
        <div class="card-title" title="${escHtml(movie.title)}">${escHtml(movie.title)}</div>
        <div class="card-meta">
          ${movie.year ? `<span>📅 ${movie.year}</span>` : ''}
          ${movie.duration ? `<span>⏱ ${fmtDuration(parseInt(movie.duration))}</span>` : ''}
          ${movie.genre ? `<span>🎭 ${escHtml(movie.genre)}</span>` : ''}
        </div>
        ${trailerHtml}
      </div>`;

    // event handlers on card buttons
    card.querySelector('[data-action="edit"]').addEventListener('click', e => {
      e.stopPropagation();
      openModal(movie.id);
    });
    card.querySelector('[data-action="delete"]').addEventListener('click', e => {
      e.stopPropagation();
      deleteMovie(movie.id);
    });

    grid.appendChild(card);
  });

  updateStats();
}

function escHtml(s) {
  return String(s)
    .replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;')
    .replace(/"/g,'&quot;').replace(/'/g,'&#39;');
}

/* ══════════════════════════════════════════
   EVENT HANDLERS
   ══════════════════════════════════════════ */
document.getElementById('search').addEventListener('input', e => {
  state.search = e.target.value;
  render();
});

document.getElementById('sort').addEventListener('change', e => {
  state.sort = e.target.value;
  render();
});

document.getElementById('filter-year').addEventListener('change', e => {
  state.filterYear = e.target.value;
  render();
});

document.getElementById('add-btn').addEventListener('click', () => openModal(null));

/* ── MODAL ───────────────────────────────── */
let editingId = null;

function openModal(id) {
  editingId = id;
  const m = id ? state.movies.find(x => x.id === id) : null;

  document.getElementById('modal-title').textContent = id ? 'Edit movie' : 'Add movie';
  document.getElementById('f-title').value    = m?.title    || '';
  document.getElementById('f-year').value     = m?.year     || '';
  document.getElementById('f-duration').value = m?.duration || '';
  document.getElementById('f-rating').value   = m?.rating   || '';
  document.getElementById('f-genre').value    = m?.genre    || '';
  document.getElementById('f-poster').value   = m?.poster   || '';
  document.getElementById('f-trailer').value  = m?.trailer  || '';

  updatePreview(m?.poster || '');
  document.getElementById('modal').classList.add('open');
  document.getElementById('f-title').focus();
}

function closeModal() {
  document.getElementById('modal').classList.remove('open');
  editingId = null;
}

document.getElementById('modal-cancel').addEventListener('click', closeModal);
document.getElementById('modal').addEventListener('click', e => {
  if (e.target === document.getElementById('modal')) closeModal();
});

// live poster preview
document.getElementById('f-poster').addEventListener('input', e => {
  updatePreview(e.target.value);
});

function updatePreview(url) {
  const box = document.getElementById('preview-box');
  if (url) {
    box.innerHTML = `<img src="${url}" style="width:100%;height:100%;object-fit:cover" onerror="this.parentElement.innerHTML='<span>Bad URL</span>'">`;
  } else {
    box.innerHTML = '<span>Preview</span>';
  }
}

document.getElementById('modal-save').addEventListener('click', saveMovie);

function saveMovie() {
  const title = document.getElementById('f-title').value.trim();
  if (!title) {
    document.getElementById('f-title').focus();
    toast('Title is required');
    return;
  }

  const data = {
    title,
    year:     document.getElementById('f-year').value     || null,
    duration: document.getElementById('f-duration').value || null,
    rating:   document.getElementById('f-rating').value   || null,
    genre:    document.getElementById('f-genre').value.trim()   || null,
    poster:   document.getElementById('f-poster').value.trim()  || null,
    trailer:  document.getElementById('f-trailer').value.trim() || null,
  };

  if (editingId) {
    // UPDATE STATE
    const idx = state.movies.findIndex(m => m.id === editingId);
    if (idx !== -1) state.movies[idx] = { ...state.movies[idx], ...data };
    toast('Movie updated');
  } else {
    // ADD TO STATE
    state.movies.push({ id: uid(), _added: Date.now(), ...data });
    toast('Movie added');
  }

  saveToStorage();       // persist
  updateYearFilter();    // update UI
  closeModal();
  render();              // re-render UI
}

function deleteMovie(id) {
  if (!confirm('Delete this movie?')) return;
  // UPDATE STATE
  state.movies = state.movies.filter(m => m.id !== id);
  saveToStorage();
  updateYearFilter();
  render();
  toast('Deleted');
}

/* ── TOAST ───────────────────────────────── */
let toastTimer;
function toast(msg) {
  const el = document.getElementById('toast');
  el.textContent = msg;
  el.classList.add('show');
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => el.classList.remove('show'), 2200);
}

/* ══════════════════════════════════════════
   INIT  (deserialize + first render)
   ══════════════════════════════════════════ */
loadFromStorage();
updateYearFilter();
render();