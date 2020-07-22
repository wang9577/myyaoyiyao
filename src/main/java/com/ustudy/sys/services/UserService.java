package com.ustudy.sys.services;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.ustudy.collections.dao.support.EntityMap;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.pools.SystemUtils;
import org.ustudy.core.security.RegisterKeyUtils;
import org.ustudy.core.security.UserUtils;
import org.ustudy.core.service.impl.ServiceImpl;
import org.ustudy.core.web.services.DictService;
import org.ustudy.core.web.services.LoginProvider;
import org.ustudy.core.web.services.LoginService;
import org.ustudy.core.web.tpl.fun.CustomFunction;
import org.ustudy.core.web.utils.WebConstans;

import com.ustudy.entity.security.Identification;
import com.ustudy.entity.sys.Roles;
import com.ustudy.entity.sys.User;
import com.ustudy.entity.sys.UserInfo;
import com.ustudy.entity.sys.UserRoles;

import freemarker.template.TemplateModel;

@Service("userService")
@Lazy(false)
public class UserService extends ServiceImpl {
	
	@Resource(name="registerKeyUtils")
	private RegisterKeyUtils registerKeyUtils;
	
	@Resource(name="loginService")
	private LoginService loginService;
	
	@Resource
	private WebConstans webConstans;
	
	@Resource
	private UserUtils userUtils;
	
	@Resource(name="jcus")
	private CustomFunction jcus;
	
	@Resource(name="dictService")
	private DictService dictService;
	
	@PostConstruct
	public void init() {
//		jcus.addDirective("createTeacherDir", new TemplateDirectiveModel() {
//			public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
//					throws TemplateException, IOException {
//				try {
//					new File(SystemUtils.getUploadRoot(),getTeacher().getCloudFolder()).mkdirs();
//				} catch (Exception e) {
//					throw new TemplateException(e,env);
//				}
//			}
//		});
		jcus.addFunction("userInfo", (List args)->{
			try {
				return getEntity("from UserInfo where code=?", userUtils.getIden().getAuthcode());
			} catch (Exception e) {
				return null;
			}
		});
		jcus.addFunction("teacher", (List args)->{
			try {
				return getEntity("from Teacher where code=?", userUtils.getIden().getAuthcode());
			} catch (Exception e) {
				return null;
			}
		});
		jcus.addFunction("student", (List args)->{
			try {
				return getEntity("from Student where code=?", userUtils.getIden().getAuthcode());
			} catch (Exception e) {
				return null;
			}
		});
		jcus.addFunction("userUrl", (List args)->{
			Identification iden=(Identification) jcus.getWrapper().unwrap((TemplateModel) args.get(0));

			return jcus.getWrapper().wrap("/admin/frame.shtml");
		});
		
//		loginService.prependProvider((HttpServletRequest request,HttpServletResponse response)->{
//			try {
//			Student stu=this.getStudent();
//			if(stu==null) {
//				return "用户未曾登录";
//			}
//			if(!stu.getCode().endsWith("_student")) {
//				return "不是教师身份的学生账号";
//			}
//			List<Identification> iden=this.<Identification>list(new HqlBean("from Identification where authcode=?",Arrays.asList(stu.getCode().split("_")[0])));
//			if(iden.isEmpty()) {
//				return "账号或密码不对";
//			}
//			
//			this.userUtils.loginIden(iden.get(0));
//			request.setAttribute("url", SystemUtils.getConfig("web.auth.url.teacher"));
//			request.setAttribute("msg", "登录成功");
//			}catch(Exception e) {
//				logger.warn(e.getMessage(),e);
//			}
//			return LoginProvider.SUCCESS;
//		});
		
//		loginService.prependProvider((HttpServletRequest request,HttpServletResponse response)->{
//			Teacher user=this.getTeacher();
//			if(user==null) {
//				return "用户未曾登录";
//			}
//			String idenCode=user.getCode()+"_student";
//			List<Identification> iden=this.<Identification>list(new HqlBean("from Identification where authcode=?",Arrays.asList(idenCode)));
//			if(iden.isEmpty()) {
//				this.executeUpdate(s->{
//					Identification newiden=new Identification();
//					newiden.setAuthcode(idenCode);
//					s.save(newiden);
//					List<Student> students=s.list(new HqlBean("from Student where code=?",Arrays.asList(idenCode)));
//					if(students.isEmpty()) {
//						Student newstu=new Student();
//						try {
//							BeanUtils.copyProperties(getTeacher(), newstu,"id");
//							newstu.setCode(idenCode);
//							s.save(newstu);
//							logger.info(newstu.getId());
//						} catch (Exception e) {
//							logger.warn(e.getMessage(),e);
//						}
//					}
//					try {
//						userUtils.loginIden(newiden);
//					} catch (Exception e) {
//						logger.warn(e.getMessage(),e);
//					}
//				});
//				
//			}else {
//				userUtils.loginIden(iden.get(0));
//			}
//			
//			request.setAttribute("url", SystemUtils.getConfig("web.auth.url.student"));
//			request.setAttribute("msg", "登陆成功");
//			return LoginProvider.SUCCESS;
//		});
		


//		loginService.addProvider((HttpServletRequest request,HttpServletResponse response)->{
//			String username=request.getParameter("username");
//			String password=request.getParameter("password");
//			if(StringUtils.isBlank(username)){
//				logger.info("未填写登录用户名");
//				return "none user name";
//			}
//			List<Identification> idens=list(new HqlBean("from Identification where authcode=?",Arrays.asList(username)));
//			
//			if(idens.isEmpty()){
//				logger.info("未找到用户");
//				return "nofound user";
//			}
//			Identification user=idens.get(0);
//			if(StringUtils.isBlank(password)){
//				logger.info("没有填写密码");
//				return "none password";
//			}
//			if(registerKeyUtils.validationPassword(user, password)){
//				userUtils.loginIden(user);
//				List<Teacher> ts= list(new HqlBean("from Teacher where hrCode=?",Arrays.asList(user.getAuthcode())));
//				if(ts.isEmpty()) {
//					ts= list(new HqlBean("from Teacher where code=?",Arrays.asList(user.getAuthcode())));
//				}
//				if(ts.size()>0) {
//					request.setAttribute("url", SystemUtils.getConfig("web.auth.url.teacher"));
//					request.setAttribute("msg", "登陆成功");
//					ts.stream().forEach(Teacher::addLoginCount);
//					save(ts);
//					logger.info("登录成功");
//					return LoginProvider.SUCCESS;
//				}
//				List<Student> stus=this.<Student>list(new HqlBean("from Student where code=?",Arrays.asList(user.getAuthcode())));
//				if(stus.size()>0) {
//					int cnt=this.<Number>list(new HqlBean("select count(*) from StudentStudyConnect")).get(0).intValue();
//					int max=Integer.parseInt(this.<DbConfig>list(new HqlBean("from DbConfig where code=?",Arrays.asList("stuMaxLogin")))
//							.get(0).getVal());
//					if(cnt>=max) {
//						userUtils.logoutUser();
//						request.setAttribute("msg", "当前学习人事已达上限，请稍后再尝试登录！");
//						return "当前学习人事已达上限，请稍后再尝试登录！";
//					}
//					if("mobile".equals(request.getParameter("platform"))) {
//						request.setAttribute("url", SystemUtils.getConfig("web.auth.url.mobile"));
//					}else {
//						request.setAttribute("url", SystemUtils.getConfig("web.auth.url.student"));
//					}
//					
//					request.setAttribute("msg", "登陆成功");
//					stus.stream().forEach(Student::addLoginCount);
//					save(stus);
//					logger.info("登录成功");
//					return LoginProvider.SUCCESS;
//				}
//				
//				return LoginProvider.SUCCESS;
//				
//			}else{
//				logger.info("错误的密码");
//				return "error password";
//			}
//		});
	}
	

	public UserInfo getUserInfo()throws Exception{
		UserInfo userinfo=this.getEntity("from UserInfo where user=?", userUtils.getUser());
		return userinfo;
	}
	
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public void saveUserInfo(UserInfo userInfo,Map<String,String> params) throws Exception{
		if(StringUtils.isBlank(userInfo.getId())) {
			User user=new User();
			Identification identification=new Identification();
			identification.setAuthcode(userInfo.getCode());
			identification.setPassword(registerKeyUtils.innerenc(params.get("password")));
			this.dao.save(identification);
			user.setCode(userInfo.getCode());
			user.setName(userInfo.getName());
			user.setStatus(true);
			user.setIden(identification);
			this.dao.save(user);
			userInfo.setUser(user);
			this.dao.save(userInfo);
		}else {
			User user=userInfo.getUser();
			Identification identification=user.getIden();
			user.setCode(userInfo.getCode());
			identification.setAuthcode(userInfo.getCode());
			this.save(identification,user,userInfo);
		}
	}
	
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public void changePassword(UserInfo userInfo,String password) throws Exception {
		Identification identification=userInfo.getUser().getIden();
		identification.setPassword(registerKeyUtils.innerenc(password));
		this.dao.save(identification);
	}
	
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public void setRoles(UserInfo userInfo,List<Roles> roles) {
		this.dao.updateHQL("delete from UserRoles where user=?", userInfo.getUser());
		for (Roles role : roles) {
			UserRoles userRoles=new UserRoles();
			userRoles.setUser(userInfo.getUser());
			userRoles.setRoles(role);
			this.dao.save(userRoles);
		}
	}

}
