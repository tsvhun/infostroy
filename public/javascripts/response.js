/**
 * Created by Alexander on 02.03.2016.
 */

$('#response').submit(function (e) {
    var formData = new FormData($(e.target)[0]);
    e.preventDefault();
    $.ajax({
        method: 'POST',
        url: '/sendresponse',
        data: formData,
        processData: false,
        contentType: false,
        success: function (response_text) {
            var form = $('.form');
            form.empty();
            form.append('<h3 class="thank">Thank you for submitting your data</h3>');
        },
        error: function(msg){
           var obj = JSON.parse(msg.responseText);
            $('.error').remove();
           for(var v in obj){
              $(".form").append('<div class="error"> Error field "'+v+ '": ' + obj[v]+'</div>');
           }
        }
    });
});

$('#cancel').click(function(){
    $('.error').remove();
})

$(document).on('change', '.opts', function () {
    var allReq = $(this).closest('fieldset').find(':checkbox[required]');
    var notReq = $(this).closest('fieldset').find(':checkbox');
    var val = false;
    norReq.each(function(index,element) {
        if($(element).prop('checked')) {
            val = true;
        }
    })
    if (val) {
        allReq.removeAttr('required');
    } else {
        norReq.attr('required', 'required');
    }
});