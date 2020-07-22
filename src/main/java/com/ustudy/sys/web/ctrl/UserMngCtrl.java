package com.ustudy.sys.web.ctrl;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.ustudy.collections.dao.support.ConditionMap;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.dao.support.bean.HqlPagesSearchBean;
import org.ustudy.core.web.ctrl.BaseController;
import org.ustudy.core.web.services.DictService;

import com.google.common.collect.Lists;
import com.ustudy.entity.sys.Roles;
import com.ustudy.entity.sys.UserInfo;
import com.ustudy.entity.sys.UserRoles;
import com.ustudy.sys.services.UserService;;

@RestController
@RequestMapping(path= {"/admin/sys/users"})
public class UserMngCtrl extends BaseController {
	
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="dictService")
	private DictService dictService;
	
	@RequestMapping(path= {"/user_list.bi"},method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
	public String list(HttpServletRequest request) throws Exception {
		List<UserInfo> users=this.service.list(new HqlPagesSearchBean(new HqlBean("UserInfo", this.createConditions(request, "prm", ",")
				,Arrays.asList(
						StringUtils.defaultString(get("sort"), "id")+" "+StringUtils.defaultString(get("order"),"asc")))
				, NumberUtils.createInteger(StringUtils.defaultString(get("page"), "1")),NumberUtils.createInteger(StringUtils.defaultString(get("rows"), "20")), null));
		
		return new ConditionMap<>().append("status", true).append("total", BeanUtilsBean.getInstance().getProperty(users, "total"))
				.append("rows"
						,Lists.<UserInfo,ConditionMap<Object>>transform(users, (UserInfo user)->new ConditionMap<>()
								.append("id", user.getId())
								.append("code", user.getCode())
								.append("name", user.getName())
								.append("signature", user.getSignature())
								.append("status", user.getUser().getStatus())
								.append("gender", user.getGender())
								.append("genderLabel",this.getProperty(dictService.getDict("gender", user.getGender()), "name"))
								.append("birthday", user.getBirthday())
								.append("userType", user.getUserType())
								.append("userTypeLabel", this.getProperty(dictService.getDict("USER_TYPE", user.getUserType()), ""))
								.append("position", user.getPosition())
								.append("mobile", user.getMobile())
								.append("email", user.getEmail())
								)).toJsonString();
	}
	
	@RequestMapping(path= {"/user_save.bi"},method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
	public String save(HttpServletRequest request)throws Exception {
		final UserInfo userInfo=this.createBean("UserInfo", "form", null);
		userService.saveUserInfo(userInfo, new ConditionMap<String>().append("password", get("password")));
		return new ConditionMap<>().append("status",true).append("msg","保存成功").toJsonString();
	}
	
	@RequestMapping(path= {"/user_change_password.bi"},method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
	public String changePassword() throws Exception{
		userService.changePassword(service.getEntity(UserInfo.class, get("id")), get("password"));
		return new ConditionMap<>().append("status",true).append("msg","修改密码成功").toJsonString();
	}
	
	@RequestMapping(path= {"/user_change_roles.bi"},method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
	public String changeRoles()throws Exception{
		String [] roles=get("roles").split(",");
		UserInfo userInfo=service.getEntity(UserInfo.class, get("id"));
		userService.setRoles(userInfo, service.list(new HqlBean("Roles",new ConditionMap().append("id", Arrays.asList(roles)))));
		return new ConditionMap<>().append("status",true).append("msg","设置角色成功").toJsonString();
	}
	
	@RequestMapping(path= {"/user_update_batchs.bi"},method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
	public String updateBatchs() throws Exception{
		 List<String> ids=Arrays.asList(get("ids").split(",")) ;
		service.update(service.list(new HqlBean("UserInfo",new ConditionMap().addPrm("id",ids))), new ConditionMap<Serializable>().append(get("name"), BooleanUtils.toBoolean(get("value"))));
		return new ConditionMap<>().append("status",true).append("msg","修改成功").toJsonString();
	}
	
	@RequestMapping(path= {"/user_roles.bi"},method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
	public String userRoles() throws Exception{
		UserInfo userInfo=this.service.getEntity(UserInfo.class, get("id"));
		List<UserRoles> urs=userInfo.getUser().getRoles();
		List<Roles> roles=service.list(new HqlBean("from Roles"));
		List<Roles> uRoles=Lists.transform(urs, (UserRoles ur)->ur.getRoles());
		return new ConditionMap<>().append("status",true).append("total", BeanUtilsBean.getInstance().getProperty(roles, "total")).append("rows",Lists.transform(roles,(Roles r)->new ConditionMap<>()
				.append("rid",r.getId())
				.append("code",r.getCode())
				.append("name",r.getName())
				.append("checked",uRoles.contains(r))
				)).toJsonString();
	}
}
