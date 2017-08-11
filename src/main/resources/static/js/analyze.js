/**
 * Created by song on 17-8-11.
 */
$(document).ready(function () {
    $("#analyze").click(function () {
        var obj = {};
        obj['query'] = $("#query").val();
        obj['sentences'] = $("#origin").val() ;
        $.ajax({
            type: 'POST',
            url: "/analyze",
            data: JSON.stringify(obj),
            success: function (data) {
                $("#abstract").val(data.summary);
            },
            contentType: "application/json",
            dataType: 'json'
        });
    });
});