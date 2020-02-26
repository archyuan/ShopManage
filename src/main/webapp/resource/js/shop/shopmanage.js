$(function () {
	var shopId = getQueryString('shopId');
	var shopInfoUrl = '/background_war/shopmanagement/getShopmanagementinfo?shopId='+shopId;
	$.getJSON(shopInfoUrl,function (data) {
		if(data.redirect){
			window.location.href = data.url;
		}else {
			if(data.shopId != undefined && data.shopId != null){
				shopId = data.shopId;
			}
			$("#shopInfo").attr('href','/background_war/shopadmin/shopoperation?shopId='+shopId);
		}
	})
})