
$(function(){
	$.extend($.fn.validatebox.defaults.rules,{
		eq:{
			validator:function(value,param){
				return $(param[0]).val() == value;
			}
			,message:"字段不匹配"
		}
		,words:{
			validator : function(value) {
               return /^[a-zA-Z0-9]*$/gi.test(value);
           },
           message:'只允许字母或数字'
		}
		,digits:{
			validator : function(value) {
				return /^\d*$/.test(value);
			}
			,message:"只能输入整数"
		}
		,number:{
			validator : function(value) {
				return /^(\d+(\.\d+)?)?$/.test(value);
			}
			,message:"只能输入数字"
		}
	});
});
