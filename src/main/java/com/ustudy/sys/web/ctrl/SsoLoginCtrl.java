package com.ustudy.sys.web.ctrl;

import javax.annotation.Resource;

import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.ustudy.core.security.UserUtils;
import org.ustudy.core.web.ctrl.BaseController;

@Controller
@RequestMapping(path = {"/sso"})
public class SsoLoginCtrl extends BaseController {
	
	@Resource
	private UserUtils userUtils;
	
	@RequestMapping(path = {"login.bi"},method = {RequestMethod.GET},produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String ssoLogin() throws Exception{
		String loginName= AssertionHolder.getAssertion().getPrincipal().getName();
		loginName=loginName.replaceAll("^0*", "");
		userUtils.loginIden(this.service.getEntity("from Identification where authcode=?", loginName));
		if("mobile".equals(get("platform"))) {
			return "redirect:/m/student/index.shtml"; 
		}
		return "redirect:/index.shtml";
	}
}
