var socket = new WebSocket("ws://" + window.location.host + "/socket");
var numResp = null;

socket.onmessage = function (event) {
    var messageJson = JSON.parse(event.data);
    if (numResp === null) {
        numResp = messageJson.count;
    } else {
        addRowToTable(messageJson);
        numResp++;
    }
    $('.countresp').remove();
    var content = ' <span class="countresp numberCircle">' + numResp + '</span>';
    $('#resps').append(content);
};

function addRowToTable(messageJson) {
    var newRow = '<tr>';
    var table = $('#responsesTable');
    table.find('th').each(function () {
        var columnName = $(this).text();
        var col = '';
        for (var k = 0 in messageJson) {
            if (messageJson[k].field.label === columnName) {
                if (messageJson[k].value != null && messageJson[k].value != '') {
                    col = messageJson[k].value;
                } else if (messageJson[k].options.length != 0) {
                    col = '';
                    for (var i = 0 in messageJson[k].options) {
                        col += messageJson[k].options[i].name + " ";
                    }
                } else {
                    col = 'N/A';
                }
                newRow += '<td>' + col + '</td>'
            }
        }
    });
    newRow += '</tr>';
    table.append(newRow);
}
