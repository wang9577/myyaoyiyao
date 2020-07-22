package com.ustudy.test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustudy.entity.security.Identification;
import com.ustudy.entity.sys.Roles;
import com.ustudy.entity.sys.User;
import com.ustudy.entity.sys.UserRoles;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.ustudy.collections.dao.support.ConditionMap;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.dao.support.entity.IdEntity;
import org.ustudy.core.security.ModuleManagerServices;
import org.ustudy.core.security.RegisterKeyUtils;
import org.ustudy.core.service.Service;
import org.ustudy.core.system.SystemKeyGenerator;

import com.ustudy.datas.SystemInitTools;
import com.ustudy.entity.security.RegisterKey;
import com.ustudy.utils.RegionService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:create.xml"
})
@Transactional(readOnly=true)
@FixMethodOrder(MethodSorters.DEFAULT)
public class InitTest {

	private Logger logger=LogManager.getLogger(this.getClass());

	@Resource(name="service")
	private Service service;

	@Resource(name="regionService")
	private RegionService regionService;
	private ModuleManagerServices moduleManagerServices;

	@Resource
	private Environment env;

	@Resource(name="systemInitTools")
	private SystemInitTools systemInitTools;
	@Resource(name="systemKeyGenerator")
	private SystemKeyGenerator systemKeyGenerator;
	@Resource(name="registerKeyUtils")
	private RegisterKeyUtils registerKeyUtils;
	@Test
	public void createTables() throws UnsupportedEncodingException{
		logger.info("create tables......");
		this.service.list(new HqlBean("from User"));
		logger.info("create tables successful");
	}
	@Test
	@Transactional(readOnly=false)
	@Rollback(false)
	public void registerSystem() throws Exception{
		logger.info("register system......");
		YamlMapFactoryBean yaml=new YamlMapFactoryBean();
		yaml.setResources(new ClassPathResource("register.yml"));
		ConditionMap<Object> configs=new ConditionMap<>(yaml.getObject());
		System.out.println(configs.get("publicKey"));
		System.out.println(configs.get("privateKey"));
		System.out.println(configs.get("registerYear"));
		RegisterKey keys=systemKeyGenerator.createRegister("admin", "Wwang9577", 3,(byte)0);
		registerKeyUtils.registerSystem(Base64.encodeBase64URLSafeString(keys.toData()));
		logger.info("register system successful......");
	}

	@Test()
	@Transactional(readOnly=false)
	@Rollback(false)
	public void initDatas() throws Exception{
		//registerSystem();
//		logger.info("init menus successful");
//		logger.info("init Regions......");
////	regionService.initRegion( roles.setCode("root");
//            roles.setName("超级管理员");
//            roles.setSortNum(System.currentTimeMillis());
//            roles.setCreateTime(System.currentTimeMillis());
//            roles.setCreator("技术支持");
//            roles.setLo roles.setCode("root");
//            roles.setName("超级管理员");
//            roles.setSortNum(System.currentTimeMillis());
//            roles.setCreateTime(System.currentTimeMillis());
//            roles.setCreator("技术支持");
//            roles.setLo);
//		//systemInitTools.initConfigs();
//		//systemInitTools.initDicts();
//systemInitTools.initService();
		systemInitTools.initAuthInfo();
//		logger.info("init Regions successful");

	System.err.println(Base64.encodeBase64URLSafeString(Base64.decodeBase64("Wwang9577")));
	}

	@Test()
	@Transactional(readOnly=false)
	@Rollback(false)
	public void initAuthInfo() throws Exception {
			Roles roles = new Roles();
			roles.setCode("root");
			roles.setName("超级管理员");
			roles.setSortNum(System.currentTimeMillis());
			roles.setCreateTime(System.currentTimeMillis());
			roles.setCreator("技术支持");
			roles.setLocked(true);
			roles.setLastModifyTime(System.currentTimeMillis());
			roles.setLastModifier("技术支持");
			roles.setMemo("系统内置的初始化角色");
			ObjectMapper mapper = new ObjectMapper();
			roles.setPerms(mapper.writeValueAsString(this.moduleManagerServices.getPermsSign()));
			roles.setMenus(mapper.writeValueAsString(this.moduleManagerServices.getAllMenus()));
			this.moduleManagerServices.save(new IdEntity[]{roles});
			Identification iden = (Identification)this.moduleManagerServices.createEntity(Identification.class, "authcode", "wang");
			iden.setAuthcode("wang");
			iden.setPassword(this.registerKeyUtils.innerenc("Wwang9577"));
			this.moduleManagerServices.save(new IdEntity[]{iden});
			User user = (User)this.moduleManagerServices.createEntity(User.class, "code", "wang");
			user.setMemo("系统内置的初始化账号");
			user.setCode("wang");
			user.setName("超级管理员");
			user.setStatus(true);
			user.setIden(iden);
			user.setUmenus(mapper.writeValueAsString(this.moduleManagerServices.getAllMenus()));
			user.setPerms(mapper.writeValueAsString(this.moduleManagerServices.getPermsSign()));
			this.moduleManagerServices.save(new IdEntity[]{user});
			UserRoles ur = new UserRoles();
			ur.setRoles(roles);
			ur.setUser(user);
			this.moduleManagerServices.remove(this.moduleManagerServices.list(new HqlBean("from UserRoles where user=?", Arrays.asList(user))));
			this.moduleManagerServices.save(new IdEntity[]{ur});


	}

}