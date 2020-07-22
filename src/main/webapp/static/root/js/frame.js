var form = {
	open: function (options) {
		var settings = $.extend({
			buttons: function ($dialog) {
				return [{
					text: '确定',
					iconCls: 'icon-ok',
					handler: function () {
						$dialog.find("form").submit();
					}
				}, {
					text: '取消',
					iconCls: 'icon-cancel',
					handler: function () {
						$dialog.dialog('close');
						$.messager.progress('close');
					}
				}];
			}
			, paramPrefix: "prm"
			, loaded: function () { }
			, preSubmit: function () { }
			, upload: false
			, closeAfterFinished:true
		}, options);

		$.messager.progress({
			msg: "正在加载，请稍后..."
		});
		var $parent = $(settings.target).load(settings.formurl, function () {
			var $dialog = $parent.children();
			$.parser.parse($parent);
			$dialog.find("form").submit(function () {
				if ($dialog.find("form").form("validate")) {
					settings.preSubmit($dialog.find("form"));
					$.messager.progress({
						msg: "正在加载，请稍后..."
					});
					if (settings.upload) {
						var $form = $dialog.find("form");
						prms = new FormData($form[0]);
						$.ajax({
							url: $form.attr("action"),
							type: 'POST',
							data: prms,
							async: true,
							cache: false,
							contentType: false,
							processData: false,
							success: function (result) {
								$.messager.show({
									title: '操作提示',
									msg: result.msg,
									timeout: 3000,
									showType: 'slide'
									, style: {
										right: ''
										, bottom: ''
									}
								});
								if (settings.grid) {
									if (settings.ui == "datagrid") {
										$(settings.grid).datagrid("reload");
									} else if (settings.ui == "tree") {
										$(settings.grid).tree("reload");
									} else if (settings.ui == "treegrid") {
										$(settings.grid).treegrid("reload");
									} else {
										$(settings.grid).datagrid("reload");
									}
								}
							},
							error: function (returndata) {
								$.messager.show({
									title: '操作提示',
									msg: "保存失败",
									timeout: 3000,
									showType: 'slide'
									, style: {
										right: ''
										, bottom: ''
									}
								});
							}
							, complete: function () {
								$.messager.progress("close");
								if(settings.closeAfterFinished){
									$dialog.dialog("close");
								}
							}
						});
					} else {
						$.post($dialog.find("form").attr("action"), $dialog.find("form").serialize(), function (result) {
							$.messager.show({
								title: '操作成功',
								msg: result.msg,
								timeout: 3000,
								showType: 'slide'
								, style: {
									right: ''
									, bottom: ''
								}
							});
							if (settings.grid) {
								if (settings.ui == "datagrid") {
									$(settings.grid).datagrid("reload");
								} else if (settings.ui == "tree") {
									$(settings.grid).tree("reload");
								} else if (settings.ui == "treegrid") {
									$(settings.grid).treegrid("reload");
								} else {
									$(settings.grid).datagrid("reload");
								}
							}
						}, "json").fail(function () {
							$.messager.show({
								title: '操作提示',
								msg: "保存失败",
								timeout: 3000,
								showType: 'slide'
								, style: {
									right: ''
									, bottom: ''
								}
							});
						}).always(function () {
							$.messager.progress("close");
							if(settings.closeAfterFinished){
								$dialog.dialog("close");
							}
						});
					}
				}
				return false;
			});

			$dialog.dialog({
				closed: false,
				modal: true,
				onClose: function () {
					$dialog.dialog("destroy");
				},
				title: settings.title || "编辑信息",
				buttons: settings.buttons($dialog)
			});

			if (settings.id) {
				var params = {};
				params[(settings.paramPrefix + ".id")] = settings.id;
				$.post(settings.listUrl, params, function (datas) {
					$dialog.find("form").append($("<input/>").attr("type", "hidden").attr("name", "id"));
					$dialog.find("form").form("load", datas.rows[0]);
					settings.loaded($dialog, datas);
				}).fail(function () {
					settings.loaded($dialog);
				}).always(function () {
					$.messager.progress("close");
				});
			} else {
				settings.loaded($dialog);
				$.messager.progress("close");
			}


		});
	}
};
