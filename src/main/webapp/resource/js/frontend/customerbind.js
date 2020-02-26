$(function() {
	var bindUrl = '/background_war/local/bindlocalauth';
	var usertype = getQueryString('usertype');
	$('#submit').click(function() {
		var userName = $('#username').val();
		var password = $('#psw').val();
		var verifyCodeActual = $('#j_captcha').val();
		var needVerify = false;
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		$.ajax({
			url : bindUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				verifyCodeActual : verifyCodeActual
			},
			success : function(data) {
				if (data.success) {
					$.toast('绑定成功！');
					if(usertype == 1){
						window.location.href="/background_war/frontend/index";
					}else {
						window.location.href="/background_war/shopadmin/shoplist";
					}
				} else {
					$.toast('绑定失败！'+data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});
});