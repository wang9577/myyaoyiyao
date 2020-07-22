$(function(){
	$("body").on("click", ".ajax", function() {
		var _this = this;
		var path = $(this).attr("href");
		var target = $(this).attr("target");
		var confirm = $(this).attr("confirm");
		if (confirm) {
			box.confirm(confirm, function() {
				if (Pages.turn.call(_this, path, target)) {
					if ("_self" == target) {
						window.location = path;
					}
				}
			});
			return false;
		} else {
			return Pages.turn.call(this, path, target);
		}
	});
});
var Pages = {
	formidx:null,
	infoidx:null,
	loadPanel : function(msg) {
		var e = $("#progress").clone().attr("id", "show_progress").removeClass("hide");
		if (msg) {
			e.find(".progressmessage").html(msg);
		}
		return e;
	},
	loadBlock : function() {
		return $("<div/>").append($("<i/>").addClass("fa fa-spinner fa-pulse fa-3x fa-fw"));
	},
	reload : function() {
		if ($("#main_body").length > 0) {
			Pages.load($("#main_body"), $("#main_body").attr("curpath"));
		} else {
			Pages.load($("#main"), $("#main").attr("curpath"));
		}
	},
	load : function(e, h, f) {
		var idx = layer.load(0, {
			shade : 0.1,
			scrollbar : false,
			zIndex : 300000000
		});
		$.get(h, function(html) {
			e.html(html);
			if (f) {
				f();
			}

		}).fail(function() {

		}).always(function() {
			layer.close(idx);
		});
	},
	turn : function(path, target) {
		if (!path) {
			return false;
		}
		if (/^javaScript.*/i.test(path)) {
			return true;
		}

		if (/^mailto.*/.test(path)) {
			return true;
		}
		if (/^tel.*/.test(path)) {
			return true;
		}
		if (/^#.*/.test(path)) {
			return true;
		}
		var f = function() {
			//$("#"+target+" a:not([target])").attr("target",target);
			//$("#"+target+" form:not([target])").attr("target",target);

		};
		if (target == "menu") {
			var id = $(this).attr('data');
			var $this = $(this);
			var loadType = $(this).attr("loadType");
			$(this).closest("ul").find("li").removeClass("active");
			$(this).closest("li").addClass("active");
			var icon = $(this).closest("li").find("i").clone();
			if ($("#tab_" + id).length > 0) {
				$("#tab_" + id).find("a").trigger("click");
				if ($(this).hasClass("menus_init_open")) {
					$(this).closest("li").removeClass("active");
				}
				return false;
			}

			var $tab = $("#tab_template").clone().removeClass("hide").attr("id", "tab_" + id).appendTo("#top_menus");
			if ($(this).hasClass("menus_init_open")) {
				$(this).closest("li").removeClass("active");
				$tab.addClass("hide");
			}
			if ("index_0" == id) {
				$tab.find(".fa-remvoe").remove();
			}
			$tab.children("a:eq(0)").prepend(icon.css("margin-right", "3px")).click(function() {
				$("#used_list_top_name_text").html($tab.find("font").html());
				//$tab.prependTo("#top_menus");
				$(".sys_main_bodies").hide();
				$("#" + $(this).attr("page")).show();
			});
			$tab.find("font").html($(this).find("span").html());
			var loadpage = function() {
				if (loadType == "IFRAME") {
					var $content = $("#content_" + id).html("");
					$tab.find("a").attr("page", "content_" + id).trigger("click");
					$("<iframe/>").attr("src", $this.attr("href")).attr("width", "100%").attr("height", "850").css("border", "0px").appendTo($content);
				} else {
					Pages.load($("#content_" + id), path, function() {
						$tab.find("a").attr("page", "content_" + id).trigger("click");

						//$("#content_"+id+" a:not([target])").attr("target","content_"+id);
						//$("#content_"+id+" form:not([target])").attr("target","content_"+id);
					});
				}

			};

			$tab.find(".page_remove").click(function() {
				box.confirm("关闭'" + $tab.find("font").html() + "'选项卡?", function() {
					var show = $("#tab_" + id).hasClass("active");
					$("#tab_" + id).remove();
					$("#content_" + id).remove();
					if (show) {
						$(".sys_tabs a:last").trigger("click");
					}
				});
				return false;
			});
			$tab.bind("dblclick", function() {
				loadpage();
			});
			$(".sys_main_bodies").hide();
			var $content = $("#body_template").clone().attr("path", path).addClass("sys_main_bodies").removeClass("hide").attr("id", "content_" + id).appendTo("#main_body").show();
			if ($("#top_menus li:not(.hide)").length > 5) {
				var page = $("#top_menus li:not(.hide):first a").attr("page");
				$("#" + page).remove();
				$("#top_menus li:not(.hide):first").remove();
			};
			loadpage();
		} else if (/^diaform(:(.*))?$/.test(target)) {
			var $diaform = $("<div/>").attr("id", "dialog_form").css("overflow-x", "hidden").css("margin", "20px");
			var $content = $("<div/>").html($diaform);
			var m = /^diaform(:(.*))?$/.exec(target);
			eval("var setting=" + (m[2] ? m[2] : "{}"));
			var opts = $.extend({
				width : "auto",
				height : "auto",
				title : "编辑信息",
				type : "size",
				btn : ["提交", "关闭"]
			}, setting);
			if (opts.type == "full") {
				var config = {
					shadeClose : true,
					type : 1,
					title : opts.title,
					btn : opts.btn,
					closeBtn : 1,
					scrollbar : false,
					yes : function(idx) {
						$("#dialog_form form").submit();
						var closeform = $("#dialog_form form").find("#close_form_panel").val();
						if (closeform) {
							return false;
						};
					},
					btn2 : function() {

					}
				};

				Pages.load($diaform, path, function() {
					config["content"] = $content.html();
					var idx = layer.open(config);
					layer.full(idx);
					Pages.formidx=idx;
				});
			} else {
				var config = {
					shadeClose : true,
					type : 1,
					title : opts.title,
					maxWidth : 1366,
					maxHeight : 768,
					btn : opts.btn,
					closeBtn : 1
					//, area:[]
					,
					scrollbar : false,
					yes : function(idx) {
						$("#dialog_form form").submit();
						var closeform = $("#dialog_form form").find("#close_form_panel").val();
						if (closeform) {
							return false;
						};
					},
					btn2 : function() {

					}
				};
				if (opts.width == "auto") {
					//config["area"][0]="auto";
				} else {
					config["area"] = [];
					config["area"][0] = opts.width + "px";
				}
				if (opts.height == "auto") {
					//config["area"][1]="auto";
				} else {
					config["area"][1] = opts.height + "px";
				}

				Pages.load($diaform, path, function() {
					config["content"] = $content.html();
					Pages.formidx=layer.open(config);
				});
			}
			return false;
		} else if (/^diainfo(:(.*))?$/.test(target)) {
			var $diainfo = $("<div/>").attr("id", "dialog_info").css("overflow-x", "hidden").css("margin", "20px");
			var $content = $("<div/>").html($diainfo);
			var m = /^diainfo(:(.*))?$/.exec(target);
			eval("var setting=" + (m[2] ? m[2] : "{}"));
			var opts = $.extend({
				width : "auto",
				height : "auto",
				title : "编辑信息",
				type : "size"
			}, setting);
			if (opts.type == "full") {
				var config = {
					shadeClose : true,
					type : 1,
					title : opts.title,
					closeBtn : 1,
					scrollbar : false
				};

				Pages.load($diainfo, path, function() {
					config["content"] = $content.html();
					var idx = layer.open(config);
					layer.full(idx);
				});
			} else {
				var config = {
					shadeClose : true,
					type : 1,
					title : opts.title,
					maxWidth : 1366,
					maxHeight : 768,
					closeBtn : 1
					//, area:[]
					,
					scrollbar : false
				};
				if (opts.width == "auto") {
					//config["area"][0]="auto";
				} else {
					config["area"] = [];
					config["area"][0] = opts.width + "px";
				}
				if (opts.height == "auto") {
					//config["area"][1]="auto";
				} else {
					config["area"][1] = opts.height + "px";
				}

				Pages.load($diainfo, path, function() {
					config["content"] = $content.html();
					layer.open(config);
				});
			}
			return false;
		} else if (/^rmout:(.*)$/.test(target)) {
			var $this = $(this);
			var nodeName = RegExp.$1;
			$.get(path, function() {
				$this.closest(nodeName).remove();
			});
			return false;
		} else if (/^rmin:(.*)$/.test(target)) {
			var $this = $(this);
			var nodeName = RegExp.$1;
			$.get(path, function() {
				$this.find(nodeName).remove();
			});
			return false;
		} else if (/^box(:(.*))?$/.test(target)) {
			var m = /^box(:(.*))?$/.exec(target);
			var boxid = m[2];
			var $this = $(this);

			if (boxid) {
				var t = $("#" + boxid);
			} else {
				var t = $this.closest(".box").parent();

			}
			t.find(".box").append($("<div>").addClass("overlay").append($("<i>").addClass("fa fa-refresh fa-spin")));
			$.get(path, function(h) {
				t.html(h);
			});
		} else if (target) {
			if ($("#" + target).length > 0) {
				var loadType = $(this).attr("loadType");
				if (loadType == "IFRAME") {
					$("#" + target).html($("<iframe/>").attr("src", $(this).attr("href")).attr("width", "100%").attr("height", "850").css("border", "0px"));
				} else {
					Pages.load($("#" + target), path, f);
				}
			} else {
				return true;
			}
		} else {
			Pages.load($(this).closest(".sys_main_bodies"), path, f);
		}
		return false;
	},
	post : function(e, h, prms, f) {
		var idx = layer.load(0, {
			shade : 0.1,
			scrollbar : false,
			zIndex : 300000000
		});
		$.post(h, prms, function(html) {
			if (e) {
				e.html(html);
			}
			if (f) {
				f();
			}

		}).always(function() {
			layer.close(idx);
		});
	},
	postBoxes : function(h, f, msg) {
		if (msg) {
			box.confirm(msg, function() {
				var e;
				if ($("#main_body").length > 0) {
					e = $("#main_body");
				} else {
					e = $("#main");
				}

				if ($(".boxitem:checked").length == 0) {
					box.message("请选择一项", "red");
					return;
				}
				var ids = "";
				$(".boxitem:checked").each(function(index) {
					if (index != 0) {
						ids += ",";
					}
					ids += $(this).val();
				});
				Pages.post(e, h, {
					ids : ids
				}, f);
			});
			return;
		} else {
			var e;
			if ($("#main_body").length > 0) {
				e = $("#main_body");
			} else {
				e = $("#main");
			}

			if ($(".boxitem:checked").length == 0) {
				box.message("请选择一项", "red");
				return;
			}
			var ids = "";
			$(".boxitem:checked").each(function(index) {
				if (index != 0) {
					ids += ",";
				}
				ids += $(this).val();
			});
			Pages.post(e, h, {
				ids : ids
			}, f);
		}

	}
	,getUrl:function(f){
		var file=f.files[0];
        var url = null;
        if (window.createObjectURL) {
            url = window.createObjectURL(file);
        } else if (window.URL) {
            url = window.URL.createObjectURL(file);
        } else if (window.webkitURL) {
            url = window.webkitURL.createObjectURL(file);
        }
        return url;
    }
}; 