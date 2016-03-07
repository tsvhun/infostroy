var i = 1;
$("#add").click(function (e) {
    var str = "'option[" + i + "]'";
    $("div.opti:last").after("<div class='opti'><input class='form-control tx' required='required' type='text' name=" + str + "><button class='but delete btn btn-danger'><span class='glyphicon glyphicon-remove'></span> Remove</button></div>");
    i++;
});
$("body").on("click", ".delete", function (e) {
    $(this).parent("div").remove();
    i--;
});


$(document).on('click', '#canbut', function(){
    $('.error').remove();
})


$(document).on('change', '#type', function () {
    var multiOptionalFields = ["RADIO_BUTTON", "CHECK_BOX", "COMBO_BOX"];
    var fieldValue = $('#type').val();
    if ($.inArray(fieldValue, multiOptionalFields) != -1) {
        $('.opts').removeClass('hidden');
        $('.opti').remove();
        $('.error').remove();
        addInput();
        if ($('#type').val() === 'RADIO_BUTTON' || $('#type').val() === 'COMBO_BOX') {
            addInput();
        }
    } else {
        $('.opts').addClass('hidden');
        $('.tx, .but').remove();
    }
});

$('#addfield').submit(function (e) {
    var formData = new FormData($(e.target)[0]);
    e.preventDefault();
    $.ajax({
        method: 'POST',
        url: $('#addfield').attr('action'),
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            window.location.href = '/fields';
        },
        error: function (msg) {
            var obj = JSON.parse(msg.responseText);
            $('.error').remove();
            for (var v in obj) {
                $("#addfield").append('<div class="error"> Error field "' + v + '": ' + obj[v] + '</div>');
            }
        }
    });
});

function addInput() {
    var str = "'option[" + i + "]'";
    $('#input').append("<div class='opti'><input class='form-control tx' required='required' type='text' name=" + str + "></div>");
    i++;
}