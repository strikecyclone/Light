/**
 * Created by song on 17-7-17.
 */
$(document).ready(function () {
    $("#search").click(function () {
        var q = $("#q").val();
        var rows = $("#rows").val();
        var highlight = $("#highlight").is(":checked") ? true : false;
        var format = $('#format option:selected').val();
        var url;
        var show_url = '/webpages/json.do?q=' + q + '&rows=' + rows + '&highlight=' + highlight;
        if (format == "json")
            url = "/webpages/json";
        if (format == "xml")
            url = "/webpages/xml";
        $("#get_url").text(show_url);
        $("#get_url").attr("href",show_url);
        $.ajax({
            type: 'GET',
            url: url,
            data: {q:q, rows:rows, highlight:highlight},
            success: function (data) {
                $("tr.tmp").remove();
                $("br.tmp").remove();
                $("p.tmp").remove();

                $("#get_url").after('<br class="tmp"><p class="tmp">耗时：'+data.time+'秒</p>');
                var len = data.results.length;
                var other_lines = '';
                for(var i = 0; i < len; i++){
                    if(i%2 == 0) {
                        other_lines = other_lines + '<tr class="tmp"><td>' + data.results[i].id + '</td><td>' + data.results[i].url + '</td><td>' + data.results[i].title + '</td><td>' + data.results[i].content + '</td></tr>';
                    }else{
                        other_lines = other_lines + '<tr class="info tmp"><td>' + data.results[i].id + '</td><td>' + data.results[i].url + '</td><td>' + data.results[i].title + '</td><td>' + data.results[i].content + '</td></tr>';
                    }
                }
                $("#first_line").after(other_lines);
            },
            contentType: "application/json",
            dataType: 'json'
        });
    });
});