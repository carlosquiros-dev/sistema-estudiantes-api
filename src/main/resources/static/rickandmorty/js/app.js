$(function () {

    const API = '/api/rickandmorty';
    let personajesActuales = [];   // lo que se está mostrando (para el modal)
    let temporizador;              // para el debounce

    // ================= SELECT 1: elegir categoría =================
    $('#selectTipo').on('change', function () {
        const tipo = $(this).val();              // "lugares", "episodios" o ""
        const $item = $('#selectItem');

        limpiarResultados();

        if (!tipo) {
            $item.prop('disabled', true).html('<option value="">2. Primero elige una categoría</option>');
            return;
        }

        $item.prop('disabled', true).html('<option value="">Cargando...</option>');

        // Llenamos el select 2 según lo elegido en el select 1
        $.getJSON(`${API}/${tipo}`)
            .done(function (lista) {
                $item.html('<option value="">2. Elige uno...</option>');
                lista.forEach(function (x) {
                    const texto = (tipo === 'lugares')
                        ? `${x.name} (${x.dimension})`
                        : `${x.episode} - ${x.name}`;
                    $item.append($('<option>').val(x.id).text(texto));
                });
                $item.prop('disabled', false);
            })
            .fail(function () {
                mostrarEstado('❌ Error al cargar la lista.');
            });
    });

    // ================= SELECT 2: elegir lugar o episodio =================
    $('#selectItem').on('change', function () {
        const id = $(this).val();
        if (!id) { limpiarResultados(); return; }

        const tipo = $('#selectTipo').val();
        const sub = (tipo === 'lugares') ? 'residentes' : 'personajes';

        $('#buscador').val('');
        // Endpoint anidado: /lugares/3/residentes  o  /episodios/5/personajes
        cargarPersonajes(`${API}/${tipo}/${id}/${sub}`);
    });

    // ================= BUSCADOR CON DEBOUNCE =================
    $('#buscador').on('input', function () {
        const texto = $(this).val().trim();

        clearTimeout(temporizador);           // cancela la búsqueda anterior
        mostrarEstado('⌨️ Escribiendo...');

        temporizador = setTimeout(function () {
            if (!texto) { limpiarResultados(); return; }
            reiniciarSelects();
            cargarPersonajes(`${API}/personajes?nombre=${encodeURIComponent(texto)}`);
        }, 500);                              // espera 500 ms sin teclear
    });

    // ================= MODAL =================
    $('#resultados').on('click', '.card', function () {
        const p = personajesActuales[$(this).data('index')];
        const $body = $('#modalBody').empty();

        $body.append($('<img class="modal-image">').attr('src', p.image));
        $body.append($('<h2>').text(p.name));
        $body.append(detalle('ID', p.id));
        $body.append(detalle('Estado', p.status));
        $body.append(detalle('Especie', p.species));
        $body.append(detalle('Género', p.gender));
        $body.append(detalle('Aparece en', p.episode.length + ' episodio(s)'));

        $('#modal').addClass('active');
    });

    $('#cerrarModal').on('click', function () {
        $('#modal').removeClass('active');
    });

    // ================= FUNCIONES DE AYUDA =================
    function cargarPersonajes(url) {
        mostrarEstado('⏳ Cargando...');
        $.getJSON(url)
            .done(function (lista) {
                personajesActuales = lista;
                pintarTarjetas(lista);
            })
            .fail(function () {
                mostrarEstado('❌ Error al consultar la API.');
            });
    }

    function pintarTarjetas(lista) {
        const $grid = $('#resultados').empty();

        if (lista.length === 0) {
            mostrarEstado('No se encontraron personajes.');
            return;
        }
        mostrarEstado(`${lista.length} personaje(s) encontrado(s).`);

        lista.forEach(function (p, i) {
            const $card = $('<div class="card">').attr('data-index', i);
            $card.append($('<img>').attr('src', p.image).attr('alt', p.name));
            $card.append(
                $('<div class="card-body">')
                    .append($('<div class="card-title">').text(p.name))
                    .append($('<div class="card-text">').text(`${p.species} · ${p.status}`))
            );
            $grid.append($card);
        });
    }

    function detalle(etiqueta, valor) {
        return $('<div class="modal-detail-text">')
            .append($('<strong>').text(etiqueta + ': '))
            .append(document.createTextNode(valor));
    }

    function reiniciarSelects() {
        $('#selectTipo').val('');
        $('#selectItem').prop('disabled', true).html('<option value="">2. Primero elige una categoría</option>');
    }

    function limpiarResultados() {
        $('#resultados').empty();
        mostrarEstado('');
        personajesActuales = [];
    }

    function mostrarEstado(texto) {
        $('#estado').text(texto);
    }
});