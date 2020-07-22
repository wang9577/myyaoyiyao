package com.ustudy.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.service.Service;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:create.xml"
})
@Transactional(readOnly=true)
public class CreateTableTest {
	
	@Resource(name="service")
	private Service service;
	
	@Resource
	private Environment env;
	
	@Test
	public void create() throws UnsupportedEncodingException{
		System.out.println(env.getProperty("jdbc.url"));
		System.out.println(this.service.list(new HqlBean("from Message")));
		System.out.println(URLEncoder.encode("上海", "utf-8"));
	}
}
