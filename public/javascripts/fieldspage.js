$('form').submit(function (e) {
    var formData = new FormData($(e.target)[0]);
    e.preventDefault();
    bootbox.confirm("Are you sure?", function (result) {
        if (result) {
            $.ajax({
                method: 'POST',
                url: '/deletefield',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response_text) {
                    $(e.target).closest('tr').hide('slow');
                },
                error: function(e){
                    console.log(e.responseText);
                }
            });
        }
    });
});

