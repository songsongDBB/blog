// DOM 加载完再执行
$(function() {

	//给每一个菜单添加一个点击事件
	$(".blog-menu .list-group-item").click(function() {
 
		//得到每一个菜单a 标签的url属性值
		var url = $(this).attr("url");
		
		// 先移除其他的点击样式，再添加当前的点击样式
		$(".blog-menu .list-group-item").removeClass("active");
		$(this).addClass("active");  
 
		// 加载其他模块的页面到右侧工作区
		//这里是后台管理员点击用户管理，请求所有的用户信息，将返回的页面放在这个页面的 rightContainer div中
		 $.ajax({ 
			 url: url, 
			 success: function(data){
				 $("#rightContainer").html(data);
		 },
		 error : function() {
		     alert("error");
		     }
		 });
	});
	
	
	//一进页面，默认加载第一个菜单的页面
	$(".blog-menu .list-group-item:first").trigger("click");
});