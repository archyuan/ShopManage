$(function() {
	var loginUrl = '/background_war/local/logincheck';
	var loginCount = 0;
    var usertype = getQueryString("usertype");
	$('#submit').click(function() {
		var userName = $('#username').val();
		var password = $('#psw').val();
		var verifyCodeActual = $('#j_captcha').val();
		var needVerify = false;
		if (loginCount >= 3) {
			if (!verifyCodeActual) {
				$.toast('请输入验证码！');
				return;
			} else {
				needVerify = true;
			}
		}
		$.ajax({
			url : loginUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				userName : userName,
				password : password,
				verifyCodeActual : verifyCodeActual,
				needVerify : needVerify
			},
			success : function(data) {
				if (data.success) {
					$.toast('登录成功！');
					if(usertype == 1){
						window.location.href = '/background_war/frontend/index';
					}else {
						window.location.href = '/background_war/shopadmin/shoplist';
					}

				} else {
					$.toast('登录失败！'+data.errMsg);
					loginCount++;
					if (loginCount >= 3) {
						$('#verifyPart').show();
					}
				}
			}
		});
	});

	$('#register').click(function() {
		window.location.href = '/myo2o/shop/register';
	});
});