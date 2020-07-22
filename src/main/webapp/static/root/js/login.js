$(function(){
	$("#login").submit(function(){
		if(!$("#username").val()){
			layer.tips("请输入用户名","#username",{
				tips:[2,"red"]
			});
			return false;
		}
		if(!$("#password").val()){
			layer.tips("请输入密码","#password",{
				tips:[2,"red"]
			});
			return false;
		}
		var load=layer.load();
		$.post($(this).attr("action"), $(this).serialize(),
			function (res) {
				if(res.status=="success"){
					window.location=res.url;
				}else{
					layer.msg(res.msg,{
						icon:5
					});
					if(res.validcode){
						$("#validateCode").load("/validateCode.shtml");
					}
				}
			},
			"json"
		).fail(function(){
			layer.msg("用户名或密码错误",{
				icon:5
			});
		}).always(function(){
			layer.close(load);
		});
		return false;
	});
});