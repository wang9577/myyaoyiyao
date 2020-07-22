package com.ustudy.test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import com.ustudy.entity.security.Identification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.ustudy.core.dao.support.Dao;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.dao.support.impl.DaoSupport;
import org.ustudy.core.security.ModuleManagerServices;
import org.ustudy.core.security.RegisterKeyUtils;
import org.ustudy.core.service.Service;

import com.ustudy.datas.SystemInitTools;
import com.ustudy.service.pages.StaticPagesService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test.xml" })
@Transactional(readOnly = true)
public class DataTest {

	private Logger logger = LogManager.getLogger();

	@Resource(name = "service")
	private Service service;

	@Resource
	private Dao dao;

	@Resource
	private Environment env;

	@Resource(name = "registerKeyUtils")
	private RegisterKeyUtils registerKeyUtils;

	@Resource(name = "systemInitTools")
	private SystemInitTools systemInitTools;

	@Resource(name = "moduleManagerServices")
	private ModuleManagerServices moduleManagerServices;

	@Resource
	private StaticPagesService staticPagesService;

	@Resource
	private SessionFactory sessionFactory;

	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	@Resource
	private DaoSupport daoSupport;



	@Test
	public void getPassword() throws Exception {
//		this.service.list(new SQLBean("create extension \"uuid-ossp\""));
//		List uuid=this.service.list(new SQLBean("select public.uuid_generate_v4() "));
//		FileReader reader=new FileReader("");
//		FileWriter writer=new FileWriter("");
//		int b=0;
//		while((b=reader.read())!=-1) {
//			writer.write(b);
//		}
//		System.out.println(uuid);
		List<Identification> identifications=this.service.<Identification>list(new HqlBean("from Identification where authcode=?", Arrays.asList("hebaodan")));
		System.out.println(registerKeyUtils.innerdec(identifications.get(0).getPassword()));
//		System.out.println(registerKeyUtils.innerenc("111111"));

	}

}
