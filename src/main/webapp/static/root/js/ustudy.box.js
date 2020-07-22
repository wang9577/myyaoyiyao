var box = {
	ins : {},
	dialog : function(path, setting, prms) {
		var opt = $.extend({
			width : 800,
			height : 550
		}, setting);

		box.ins = new jBox('Modal', {
			content : "",
			width : opt.width,
			height : opt.height,
			onCloseComplete : function() {
				this.destroy();
			}
		}).open();
		box.ins.setContent($("#progress").clone().attr("id", "dialog_progress").removeClass("hide"));
		$.post(path, prms, function(html) {
			box.ins.setContent(html);
		}, "html");
	},
	error : function(target, content) {
		var t = $('.tooltip').jBox('Tooltip', {
			target : target,
			content : $("<font color='red'/>").html(content),
			position : {
				x : 'right',
				y : 'top'
			},
			outside : 'x',
			zIndex : 20000000,
			autoClose : 1,
			color : "red"
			//, closeButton:"box"
			,
			onCloseComplete : function() {
				this.destroy();
			},
			onOpen : function() {
				setTimeout(function() {
					t.close();
				}, 2000);
			}
		});
		t.open();
	},
	tips : {},
	floatPanel : function(name, target, path) {
		if (box.tips[name]) {
			box.tips[name].close();
		}
		var loading = $("<i/>").addClass("fa fa-spinner fa-pulse fa-fw margin-bottom");
		box.tips[name] = $('.tooltip').jBox('Tooltip', {
			target : target,
			content : loading,
			position : {
				x : 'right',
				y : 'top'
			},
			minWidth : 80,
			minHeight : 80,
			maxWidth : 450,
			maxHeight : 450,
			outside : 'x',
			autoClose : 1,
			color : "red",
			zIndex : 20000000,
			closeButton : "box",
			onCloseComplete : function() {
				this.destroy();
			}
		});

		box.tips[name].open();
		$.post(path, {}, function(html) {
			loading.remove();
			box.tips[name].setContent(html);
		}, "html");
	},
	message : function(msg, icon) {
		layer.msg(msg, {
			icon : icon
		});
	},
	confirm : function(msg, confirm, cancel) {
		layer.confirm(msg, {
			btn : ['确认', '取消'] //按钮
		}, function(idx) {
			confirm();
			layer.close(idx);
		}, cancel);
	},
	inputConfirm : function(msg, confirm, end) {
		if (end) {
			end = function() {
			};
		}
		layer.prompt({
			title : msg,
			formType : 0,
			closeBtn : 0,
			end : end
		}, confirm);
	},
	panel : function(h, f) {
		var index = layer.open({
			content : h.html(),
			yes : f
		});
	},
	showMessage : function(t, c, i) {
		layer.open({
			type : 0,
			title : t,
			content : c,
			btn : ["我知道了"],
			offset : "rb",
			anim : 2,
			isOutAnim : 2,
			shade : false,
			icon : i,
			success : function(l, i) {
				setTimeout(function() {
					layer.close(i);
				}, 10000);

			}
		});
	},
	getUserId : function() {
		return $("#current_login_user_id").val();
	}
	,passwordPower:function(pwd){
		var d=pwd.search(/\d+/);
		var w=pwd.search(/[a-zA-Z]+/);
		var s=pwd.search(/[^0-9a-zA-Z]/);
		var score=0;
		if(d>=0){
			score+=1;
		}
		if(w>=0){
			score+=1;
		}
		if(s>=0){
			score+=1;
		}
		if(pwd.length>=8){
			score+=1;
		}
		
		return score;
	}
}; 