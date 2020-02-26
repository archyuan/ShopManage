$(function () {
 $("#log-out").click(function () {
    $.ajax({
        url:"/background_war/local/logout",
        type:"post",
        async:false,
        cache:false,
        dataType:'json',
        success:function (data) {
            if(data.success){
                var usertype = $("#log-out").attr("usertype");
                window.event.returnValue=false;
                window.location.href='/background_war/local/login?usertype='+usertype;
                return false;
            }
        },
        error:function (data,error) {
            alert(error);
        }
    });
 });
});