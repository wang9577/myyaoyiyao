package com.ustudy.sys.web.ctrl;

import javax.annotation.Resource;

import org.hibernate.service.spi.ServiceException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.ustudy.core.security.RegisterKeyUtils;
import org.ustudy.core.web.ctrl.BaseController;

import com.ustudy.entity.security.Identification;
import com.ustudy.sys.services.UserService;

@RestController
@RequestMapping("/protected/login_user")
public class LoginUserCtrl extends BaseController {
	
	@Resource
	private RegisterKeyUtils registerKeyUtils;
	
	@Resource
	private UserService userService;
	
	@RequestMapping(path = {"change_password.bi"},method = {RequestMethod.POST},produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String changePassword() throws Exception{
		try {
			this.service.executeUpdate(s->{
				Identification identification=s.getEntity("from Identification where authcode=?", get("code"));
				try {
					logger.info(identification);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(registerKeyUtils.validationPassword(identification, get("password"))) {
					try {
						identification.setPassword(registerKeyUtils.innerenc(get("newPassword")));
						service.save(identification);
					} catch (Exception e) {
						throw new ServiceException("密码认证错误");
					}
				}else {
					throw new ServiceException("密码认证错误");
				}
			});
		}catch(Exception e) {e.printStackTrace();
			return this.createResult(false).append("msg","原密码认证错误").toJsonString();
		}
		return this.createResult(true).append("msg","修改密码成功").toJsonString();
	}
	
	
	@RequestMapping(path = {"transTeacherToStudent.bi"},method = {RequestMethod.POST},produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String transTeacherToStudent() throws Exception{
		return this.createResult(true).toJsonString();
	}
}
