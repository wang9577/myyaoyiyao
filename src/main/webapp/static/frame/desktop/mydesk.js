var myDesk={
	init:function(){
		//设置任务栏时间日期
		this.timerSetter();
		//初始化系统菜单
		this.sysMenu();
		//显示隐藏菜单
		$(document).bind("mousemove",function(event){
			if(event.pageX>($(window).width()-10)){
				if($.trim($('#startmenu').css("display"))=="none")myDesk.menuShowOrHide(false);
			}
		});
		$('#startmenu').bind("mouseout",function(){
			myDesk.menuShowOrHide(true);
		});
		//初始化任务栏悬停按钮
		this.btnMap = new Map();
		this.preButton = null;
		//初始化窗口
		this.winChain = new Array();
		this.winMap = new Map();
		//初始化桌面
		this.iconMap = new Map();
		//初始化右键菜单
        $.contextMenu({
	        selector: '.desktop',
	        callback: function(key, options) {
	            var m = "clicked: " + key;
	            window.console && console.log(m) || alert(m);
	        },
	        items: {
	            "display": {name: "显示桌面", icon: "display"},
	            "sep1": "-",
	            "system": {name: "系统设置", icon: "system"},
	            "theme": {name: "主题设置", icon: "theme"},
	            "sep2": "-",
	            "logout": {name: "退出系统", icon: "logout"},
	            "about": {name: "关于OSOA", icon: "about"}
	        }
	    });
	},
	sysMenu:function(){
		var sysmenu="<div id='startmenu' class='start_menu'>";
		sysmenu = sysmenu + "<iframe id='menuframe' width='100%' height='100%' frameborder='0'></iframe>";
		sysmenu = sysmenu + "</div>";
		$(document.body).append(sysmenu);
		$('#startmenu').css({height:$(window).height()+"px"});
		$('#menuframe').css({height:$(window).height()+"px"});
		$('#menuframe').attr("src","menu.html");

	},
	//设置任务栏日期时间
	timerSetter:function(){
		var date = new Date();
		var year = date.getFullYear();
		var month = ""+date.getMonth();
		if(month.length==1)month="0"+month;
		var dt = ""+date.getDate();
		if(dt.length==1)dt="0"+dt;
		var hour = ""+date.getHours();
		if(hour.length==1)hour="0"+hour;
		var mis = ""+date.getMinutes();
		if(mis.length==1)mis="0"+mis;
		var sec = ""+date.getSeconds();
		if(sec.length==1)sec="0"+sec;
		$(".timer").text(year+"年"+month+"月"+dt+"日"+hour+":"+mis+":"+sec);
		setTimeout("myDesk.timerSetter()",1000);
	},
	//控制菜单的显隐
	menuShowOrHide:function(sh){
		if(sh){
			$('#startmenu').hide();
		}else{
			$('#startmenu').css({display:"block", top:"0px", left:$(window).width()+"px", height:$(window).height()+"px"});
			$('#startmenu').animate({left:($(window).width()-$('#startmenu').width())+"px"});
		}
	},
	//创建应用窗口
	createApp:function(config){
		var defaults = {
			id : null,
			title : '新建窗口',
			barIcon: '${base}/static/frame/images/window.png',
			width : 1024,
			height : 500,
			frameurl : 'http://www.baidu.com',
			iconCls : "window16x16",
			maximizable : true,
			minimizable : true,
			modal : false,
			collapsible : false,
			draggable : true,
			resizable : true,
			closable : true
		};
		var cfg = $.extend(defaults, config);
		//添加窗口链表
		this.winChain[this.winChain.length] = cfg['id'];
		//创建窗口
		var curwin = this.winMap.get(cfg['id']);

		if(curwin){
			$('#win_'+cfg['id']).window('open');
		}else{
			//在桌面上添加新窗口html代码
			$('.desktop').append(this.winHtml(cfg));
			//显示窗口
			$('#win_'+cfg['id']).window();
			//向窗口集合中该窗口
			this.winMap.put(cfg['id'], $('#win_'+cfg['id']));
		}
		//创建任务栏
		var curBtn = this.btnMap.get(cfg['id']);
		if(curBtn){
			if(this.winChain.length>1){
				$("#bar_"+this.winChain[this.winChain.length-2]+">li").removeClass("bar_column_select").addClass("bar_column_out");
			}
			curBtn.removeClass("bar_column_out").addClass("bar_column_select");
		}else{
			var html="<ul id=\"bar_"+cfg['id']+"\">";
			html = html + "<li class=\"bar_column_select\"><img src=\""+cfg['barIcon']+"\"><em>"+cfg['title']+"</em></li>";
			html = html + "</ul>";
			$(".bar_container").append(html);
			this.btnMap.put(cfg['id'], $('#bar_'+cfg['id']+">li"));

			if(this.winChain.length>1){
				$("#bar_"+this.winChain[this.winChain.length-2]+">li").removeClass("bar_column_select").addClass("bar_column_out");
			}
			//添加鼠标事件
			$("#bar_"+cfg['id']+">li").bind("click",function(){
				myDesk.createApp({id:""+cfg['id']});
			});
			$("#bar_"+cfg['id']+">li").bind("mouseover",function(){
				if($("#bar_"+cfg['id']+">li").attr("class")!="bar_column_select")
					$("#bar_"+cfg['id']+">li").removeClass("bar_column_out").addClass("bar_column_in");
			});
			$("#bar_"+cfg['id']+">li").bind("mouseout",function(){
				if($("#bar_"+cfg['id']+">li").attr("class")!="bar_column_select")
					$("#bar_"+cfg['id']+">li").removeClass("bar_column_in").addClass("bar_column_out");
			});
	        //任务栏右键菜单
			$.contextMenu({
		        selector: "#bar_"+cfg['id']+">li",
		        callback: function(key, options) {
		            var m = "clicked: " + key;
		          if(key=='closewin'){
                      myDesk.destoryApp(cfg['id']);
		          }
		        },
		        items: {
		            // "maxi": {name: "最大化", icon: "maxi"},
		            // "mini": {name: "最小化", icon: "mini"},
		            // "sep2": "-",
		            "closewin": {name: "关闭窗口", icon: "closewin"}
		        }
		    });
		}
	},
	destoryApp:function(id){
		//卸载窗口
		this.winMap.remove(id);
		$("#win_"+id).window('destroy');
		//卸载任务栏按钮
		this.btnMap.remove(id);
		$("#bar_"+id).remove();
		//重置窗口链表
        this.restoreChain(id);
	},
	winHtml:function(cfg){
		var html = "<div id=\"win_" + cfg['id'] + "\"";
		html = html + " class=\"easyui-window\"";
		html = html + " data-options=\"";
		html = html + "maximizable:"+cfg['maximizable'];
		html = html + ",minimizable:"+cfg['minimizable'];
		html = html + ",modal:"+cfg['modal'];
		html = html + ",collapsible:"+cfg['collapsible'];
		html = html + ",draggable:"+cfg['draggable'];
		html = html + ",draggable:"+cfg['draggable'];
		html = html + ",closable:"+cfg['closable'];
		html = html + ",inline:true";
		html = html + ",onClose:function(){myDesk.destoryApp('"+cfg['id']+"');}";
		html = html + ",onMinimize:function(){myDesk.restoreChain('"+cfg['id']+"');}";
		html = html + ",closable:"+cfg['closable'];
		html = html + ",width:"+cfg['width'];
		html = html + ",height:"+cfg['height'];
		html = html + ",iconCls:'"+cfg['iconCls'];
		html = html + "',title:'"+cfg['title'];
		html = html + "',style:'{padding:5px}'\">";
		html = html + "<iframe frameborder=\"0\" width=\"100%\" height=\"100%\" marginwidth=\"100%\" marginheight=\"100%\" src=\""+cfg['frameurl']+"\"></iframe>";
		html = html + "</div>";
		return html;
	},
	restoreChain:function(id){
		//清除任务栏样式
		if($("#bar_"+id+">li")){
			$("#bar_"+id+">li").removeClass("bar_column_select").addClass("bar_column_out");
		}
		//重置链表
		var tmpChain = new Array();
		var curChain = this.winChain;
		for(var idx=0;idx<curChain.length;idx++){
			if(curChain[idx]!=id){
				tmpChain[tmpChain.length]=curChain[idx];
			}
		}
		this.winChain = tmpChain;
		//并选中最后窗口
		if(this.winChain.length>0){
			$("#win_"+this.winChain[this.winChain.length-1]).window("open");
			$("#bar_"+this.winChain[this.winChain.length-1]+">li").removeClass("bar_column_out").addClass("bar_column_select");
		}
	},
	getMenuHeight:function(){
		return $(window).height();
	},
	createDeskTopApp:function(data){
		var win_h = $(window).height();
		var cur_top = 10;
		var cur_left = 10;
		for(var idx=0;idx<data.length;idx++){
			var deskicon = data[idx];
			var frame = deskicon.frame[0];
			//组合桌面图标
			var html = "<div id='icon_"+frame.id+"' style='left:"+cur_left+"px;top:"+cur_top+"px;' class='deskicon' ondblclick='myDesk.createApp("+JSON.stringify(frame)+")'>";
			html = html + "<div>";
			html = html + "<p><img src='"+deskicon.icon+"'/><p>";
			html = html + "<em>"+frame.title+"</em>";
			html = html + "</div></div>";
			$(".desktop").append(html);
			//计算左边距和顶边距
			if((cur_top+110)>(win_h-140)){
				cur_top = 10;
				cur_left = cur_left + 110;
			}else{
				cur_top = cur_top+110;
			}

		}
	}
};

