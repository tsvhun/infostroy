$('#loginform').submit(function(event) {
    event.preventDefault();
    var formData = new FormData($(event.target)[0]);
    $.ajax({
        method: 'POST',
        url: '/login',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response_text) {
            window.location.href = '/responses';
        },
        error: function(errors) {
            $('.errormsg').remove();
            $('#formlog').append('<p class="errormsg">Invalid login or password</p>')
        }
    });
});