// DOM 加载完再执行
$(function(){
	
	//注册之前，先校验输入数据的正确性
	$(".btn-primary").click(function() {
 
		$.ajax({ 
			 url: "/users/register", 
			 type: 'POST',
			 data:$('#registerForm').serialize(),
			 success: function(data){
				 
				 if (data.success) {
					 
					 //注册成功之后，重定向去登录页面
					 window.location.href = 'login';
					 
				 } else {
					 toastr.error(data.message);
				 }

		     },
		     error : function() {
		    	 toastr.error("注册失败!");
		     }
		 });
		
	});
	
});