$(function () {

    const API_URL = '/api/students';
    let allStudents = [];        // every student loaded from the backend
    let currentStudent = null;   // the student open in the detail modal

    // ==========================================
    // 1. GET ENDPOINT (READ)
    // ==========================================
    function loadData() {
        $.getJSON(API_URL)
            .done(function (data) {
                allStudents = data;
                renderCards(allStudents);
            })
            .fail(function () {
                $('#studentGrid').html('<p class="empty-message">Could not load students.</p>');
            });
    }

    function renderCards(list) {
        const $grid = $('#studentGrid').empty();

        if (list.length === 0) {
            $grid.append($('<p class="empty-message">').text('No students found.'));
            return;
        }

        list.forEach(function (student) {
            const $card = $('<div class="card">').attr('data-id', student.id);
            $card.append($('<div class="card-name">').text(`${student.firstName} ${student.lastName}`));
            $card.append($('<div class="card-id">').text(`ID: ${student.id}`));
            $grid.append($card);
        });
    }

    // Realtime search (in-memory filter)
    $('#searchInput').on('input', function () {
        const kw = $(this).val().toLowerCase().trim();
        renderCards(allStudents.filter(function (s) {
            return `${s.firstName} ${s.lastName}`.toLowerCase().includes(kw) || s.id.toString().includes(kw);
        }));
    });

    // ==========================================
    // 2. POST / PUT ENDPOINTS (CREATE / UPDATE)
    // ==========================================
    $('#studentForm').on('submit', function (e) {
        e.preventDefault();

        const id = $('#studentId').val();
        const data = {
            firstName: $('#firstName').val(),
            lastName: $('#lastName').val(),
            email: $('#email').val()
        };

        $.ajax({
            url: id ? `${API_URL}/${id}` : API_URL,
            method: id ? 'PUT' : 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data)
        })
            .done(function () {
                closeModal('#formModal');
                loadData(); // Reload data
            })
            .fail(function () {
                alert('Could not save the student.');
            });
    });

    // ==========================================
    // 3. DELETE ENDPOINT
    // ==========================================
    $('#deleteBtn').on('click', function () {
        if (!confirm('Delete this student?')) return;

        $.ajax({ url: `${API_URL}/${currentStudent.id}`, method: 'DELETE' })
            .done(function () {
                closeModal('#detailModal');
                loadData(); // Reload data
            })
            .fail(function () {
                alert('Could not delete the student.');
            });
    });

    // ==========================================
    // MODAL CONTROL (POPUPS)
    // ==========================================

    // Click on a card -> open details (event delegation: cards are created later)
    $('#studentGrid').on('click', '.card', function () {
        const id = $(this).data('id');
        currentStudent = allStudents.find(function (s) { return s.id === id; });

        $('#modalName').text(`${currentStudent.firstName} ${currentStudent.lastName}`);
        $('#modalEmail').text(currentStudent.email);
        openModal('#detailModal');
    });

    // "+ New" button -> empty form
    $('#newBtn').on('click', function () {
        $('#studentForm')[0].reset();
        $('#studentId').val('');
        $('#formTitle').text('New Student (POST)');
        openModal('#formModal');
    });

    // "Edit" button -> form filled with the current student
    $('#editBtn').on('click', function () {
        closeModal('#detailModal');
        $('#studentId').val(currentStudent.id);
        $('#firstName').val(currentStudent.firstName);
        $('#lastName').val(currentStudent.lastName);
        $('#email').val(currentStudent.email);
        $('#formTitle').text('Edit Student (PUT)');
        openModal('#formModal');
    });

    // Every "×" closes the modal it belongs to
    $('.close-btn').on('click', function () {
        $(this).closest('.modal-overlay').removeClass('active');
    });

    function openModal(selector) {
        $(selector).addClass('active');
    }

    function closeModal(selector) {
        $(selector).removeClass('active');
    }

    // Start
    loadData();
});
