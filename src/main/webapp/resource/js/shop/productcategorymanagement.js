$(function () {
    var listUrl = '/background_war/productcategory/getproductcategorylist';
    var addUrl = '/background_war/productcategory/addproductcategories';
    var deleteUrl = '/background_war/productcategory/removeproductcategory';
    getList();
    function getList() {
        $.getJSON(
            listUrl,
            function (data) {
                if(data.success){
                    var dataList  = data.data;
                    $(".category-wrap").html('');
                    var tempHtml = '';
                    dataList.map(function (item,index) {
                           tempHtml += ''
                                +'<div class="row row-product-category now">'
                                +'<div class="col-33 product-category-name">'
                                +item.productCategoryName
                                +'</div>'
                                +'<div class="col-33">'
                                +item.priority
                                +'</div>'
                                +'<div class="col-33"><a href="#" class="button delete" date-id="'
                                +item.productCategoryId
                                +'">删除</a></div>'
                                +'</div>';
                    });
                    $('.category-wrap').append(tempHtml);
                }
            }
        );
    }
    $("#new").click(function () {
        var tempHtml = '<div class="row row-product-category temp">'
        +'<div class="col-33"><input class="category-input category" type="text"></div>'
        +'<div class="col-33"><input class="category-input priority" type="text"></div>'
        +'<div class="col-33"><a href="#" class="button delete" >删除</a></div>'
        +'</div>';
        $('.category-wrap').append(tempHtml);
    });

    $('#submit').click(function () {
        var tempArr = $('.temp');
        var productCategoryList = [];
        tempArr.map(function (index,item) {
            var tempObj = {};
            tempObj.productCategoryName = $(item).find('.category').val();
            tempObj.priority = $(item).find('.priority').val();
            if(tempObj.productCategoryName && tempObj.priority){
                productCategoryList.push(tempObj);
            }
        });
        $.ajax({
            url:addUrl,
            type:'POST',
            data:JSON.stringify(productCategoryList),
            contentType:'application/json',
            success:function (data) {
                if(data.success){
                    alert('提交成功');
                    getList();
                }else {
                    alert("提交失败"+data.errMsg);
                }
            }
        });

    });

   $('.category-wrap').on('click','.row.row-product-category.temp.delete',
       function (e) {
            console.log($(this).parent().parent());
            $(this).parent().parent().remove();
       });
   $('.category-wrap').on('click','.row.row-product-category.now.delete',
       function (e) {
            var target = e.currentTarget;
            $.confirm("确定删除?",function () {
                $.ajax({
                    url: deleteUrl,
                    type: 'POST',
                    data: {
                        productCategoryId:target.dataset.id
                    },
                    dataType:'json',
                    success:function (data) {
                        if(data.success){
                            alert("删除成功");
                            getList();
                        }

                    }
                });
            });
       })
});