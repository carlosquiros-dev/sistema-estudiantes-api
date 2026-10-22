$(function () {

    const API = '/api/rickandmorty';
    let currentCharacters = [];   // what is on screen right now (used by the modal)
    let debounceTimer;            // for the debounce

    // ================= SELECT 1: choose a category =================
    $('#categorySelect').on('change', function () {
        const category = $(this).val();          // "locations", "episodes" or ""
        const $item = $('#itemSelect');

        clearResults();

        if (!category) {
            $item.prop('disabled', true).html('<option value="">2. Choose a category first</option>');
            return;
        }

        $item.prop('disabled', true).html('<option value="">Loading...</option>');

        // Fill select 2 based on what was chosen in select 1
        $.getJSON(`${API}/${category}`)
            .done(function (list) {
                $item.html('<option value="">2. Choose one...</option>');
                list.forEach(function (x) {
                    const label = (category === 'locations')
                        ? `${x.name} (${x.dimension})`
                        : `${x.episode} - ${x.name}`;
                    $item.append($('<option>').val(x.id).text(label));
                });
                $item.prop('disabled', false);
            })
            .fail(function () {
                showStatus('❌ Error loading the list.');
            });
    });

    // ================= SELECT 2: choose a location or episode =================
    $('#itemSelect').on('change', function () {
        const id = $(this).val();
        if (!id) { clearResults(); return; }

        const category = $('#categorySelect').val();
        const sub = (category === 'locations') ? 'residents' : 'characters';

        $('#searchInput').val('');
        // Nested endpoint: /locations/3/residents  or  /episodes/5/characters
        loadCharacters(`${API}/${category}/${id}/${sub}`);
    });

    // ================= DEBOUNCED SEARCH =================
    $('#searchInput').on('input', function () {
        const text = $(this).val().trim();

        clearTimeout(debounceTimer);          // cancel the previous search
        showStatus('⌨️ Typing...');

        debounceTimer = setTimeout(function () {
            if (!text) { clearResults(); return; }
            resetSelects();
            loadCharacters(`${API}/characters?name=${encodeURIComponent(text)}`);
        }, 500);                              // wait 500 ms without typing
    });

    // ================= MODAL =================
    $('#results').on('click', '.card', function () {
        const c = currentCharacters[$(this).data('index')];
        const $body = $('#modalBody').empty();

        $body.append($('<img class="modal-image">').attr('src', c.image));
        $body.append($('<h2>').text(c.name));
        $body.append(detail('ID', c.id));
        $body.append(detail('Status', c.status));
        $body.append(detail('Species', c.species));
        $body.append(detail('Gender', c.gender));
        $body.append(detail('Appears in', c.episode.length + ' episode(s)'));

        $('#modal').addClass('active');
    });

    $('#closeModal').on('click', function () {
        $('#modal').removeClass('active');
    });

    // ================= HELPER FUNCTIONS =================
    function loadCharacters(url) {
        showStatus('⏳ Loading...');
        $.getJSON(url)
            .done(function (list) {
                currentCharacters = list;
                renderCards(list);
            })
            .fail(function () {
                showStatus('❌ Error calling the API.');
            });
    }

    function renderCards(list) {
        const $grid = $('#results').empty();

        if (list.length === 0) {
            showStatus('No characters found.');
            return;
        }
        showStatus(`${list.length} character(s) found.`);

        list.forEach(function (c, i) {
            const $card = $('<div class="card">').attr('data-index', i);
            $card.append($('<img>').attr('src', c.image).attr('alt', c.name));
            $card.append(
                $('<div class="card-body">')
                    .append($('<div class="card-title">').text(c.name))
                    .append($('<div class="card-text">').text(`${c.species} · ${c.status}`))
            );
            $grid.append($card);
        });
    }

    function detail(label, value) {
        return $('<div class="modal-detail-text">')
            .append($('<strong>').text(label + ': '))
            .append(document.createTextNode(value));
    }

    function resetSelects() {
        $('#categorySelect').val('');
        $('#itemSelect').prop('disabled', true).html('<option value="">2. Choose a category first</option>');
    }

    function clearResults() {
        $('#results').empty();
        showStatus('');
        currentCharacters = [];
    }

    function showStatus(text) {
        $('#status').text(text);
    }
});
