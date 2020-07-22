var desktop = {
	menus : function($parent) {
		$parent.find("._left_side ._menu_item a").click(function(){
			desktop.load($parent);
			$.post($(this).attr("href"),JSON.parse($(this).attr("params")?$(this).attr("params"):"{}"),function(h){
				$parent.find("._main_content").html(h);
				$.parser.parse($parent.find("._main_content"));
				desktop.loadClose();
			})
			
			$parent.find("._left_side ._menu_item").removeClass("cur");
			$(this).closest("._menu_item").addClass("cur");
			return false;
		});
	}
	,load:function(panel){
		$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:panel.height()}).appendTo(panel);   
	    $("<div class=\"datagrid-mask-msg\"></div>").css("height","auto").html("正在处理，请稍候。。。").appendTo(panel).css({display:"block",left:(panel.outerWidth(true) - 190) / 2,top:(panel.height() - 45) / 2}); 
	}
	,loadClose:function(){
		$(".datagrid-mask").remove();   
	    $(".datagrid-mask-msg").remove();   
	}
}