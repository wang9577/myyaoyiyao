package com.ustudy.resource.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.hibernate.Session;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.dao.support.impl.DaoSupport;
import org.ustudy.core.security.RegisterKeyUtils;
import org.ustudy.core.service.impl.ServiceImpl;

import com.ustudy.entity.security.Identification;

@Service("ftpService")
@Lazy(false)
public class FtpService extends ServiceImpl implements UserManager {
	@Override
	public User getUserByName(String s) throws FtpException {
		return null;
	}

	@Override
	public String[] getAllUserNames() throws FtpException {
		return new String[0];
	}

	@Override
	public void delete(String s) throws FtpException {

	}

	@Override
	public void save(User user) throws FtpException {

	}

	@Override
	public boolean doesExist(String s) throws FtpException {
		return false;
	}

	@Override
	public User authenticate(Authentication authentication) throws AuthenticationFailedException {
		return null;
	}

	@Override
	public String getAdminName() throws FtpException {
		return null;
	}

	@Override
	public boolean isAdmin(String s) throws FtpException {
		return false;
	}
//
//	@Resource
//	private RegisterKeyUtils registerKeyUtils;
//
//	@Resource
//	private DaoSupport daoSupport;
//
//	private FtpServer ftpServer;
//
//	@Resource
//	private CapacityFtpLet capacityFtpLet;
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@PostConstruct
//	public void init() throws Exception {
//		if("true".equals(this.getProperty(this.configs, "module.ftp.open"))) {
//			logger.info("FTP server starting");
//			FtpServerFactory ftpServerFactory = new FtpServerFactory();
//			ConcurrentHashMap ftplets=new ConcurrentHashMap<>();
//			ftplets.put("size", capacityFtpLet);
//			ftpServerFactory.setFtplets(ftplets);
//			ListenerFactory listenerFactory = new ListenerFactory();
//			listenerFactory.setPort(Integer.parseInt(this.getProperty(this.configs, "module.ftp.port")));
//			ftpServerFactory.setUserManager(this);
//			ftpServerFactory.addListener("default", listenerFactory.createListener());
//			ftpServer = ftpServerFactory.createServer();
//			ftpServer.start();
//			logger.info("FTP server started");
//		}else {
//			logger.info("FTP server not open");
//		}
//	}
//
//	@PreDestroy
//	public void stop() {
//		if(ftpServer!=null) {
//			ftpServer.stop();
//		}
//
//	}
//
//	public User getUserByName(String username) throws FtpException {
//		try (Session session = this.getDao().open()) {
//			List<Teacher> teachers = this.daoSupport
//					.list(new HqlBean("from Teacher where code=?", Arrays.asList(username)), session);
//			if (teachers.isEmpty()) {
//				return null;
//			}
//			File root=new File(teachers.get(0).getCode());
//			if(!root.exists()) {
//				root.mkdirs();
//				new File(root,"我的文档").mkdir();
//				new File(root,"我的课程").mkdir();
//				new File(root,"我的视频").mkdir();
//				new File(root,"我的音频").mkdir();
//				new File(root,"我的网站").mkdir();
//			}
//			return teachers.get(0);
//		}
//
//	}
//
//	public String[] getAllUserNames() throws FtpException {
//		try(Session session = this.dao.open()){
//			return this.daoSupport.<Teacher>list("from Teacher",session).stream().map(Teacher::getCode).collect(Collectors.toList())
//					.toArray(new String[] {});
//		}
//	}
//
//	public void delete(String username) throws FtpException {
//		throw new FtpException("管理用户请登录系统后台程序进行管理！");
//
//	}
//
//	@Override
//	public void save(User user) throws FtpException {
//		throw new FtpException("管理用户请登录系统后台程序进行管理！");
//
//	}
//
//	public boolean doesExist(String username) throws FtpException {
//		try (Session session = this.dao.open()) {
//			return this.daoSupport.list(new HqlBean("from Teacher where code=?", Arrays.asList(username)), session)
//					.size() > 0;
//		}
//	}
//
//	@Override
//	public User authenticate(Authentication authentication) throws AuthenticationFailedException {
//		if (authentication instanceof UsernamePasswordAuthentication) {
//			UsernamePasswordAuthentication auth = (UsernamePasswordAuthentication) authentication;
//			String code = auth.getUsername();
//			String passwd = auth.getPassword();
//			Identification t=null;
//			try(Session session=this.getDao().open()){
//				List<Identification> teachers = this.daoSupport
//						.list(new HqlBean("from Identification where authcode=?", Arrays.asList(code)),session);
//				if (teachers.isEmpty()) {
//					List<Teacher> ts=this.daoSupport.list(new HqlBean("from Teacher where hrCode=?",Arrays.asList(code)), session);
//					if(ts.isEmpty()) {
//						throw new AuthenticationFailedException("用户名或密码错误");
//					}
//					teachers = this.daoSupport
//							.list(new HqlBean("from Identification where authcode=?", Arrays.asList(ts.get(0).getCode())),session);
//				}
//				t = teachers.get(0);
//			}
//			if (registerKeyUtils.validationPassword(t, passwd)) {
//				try {
//					User teacher = this.getUserByName(t.getAuthcode());
//					if (teacher == null) {
//						throw new AuthenticationFailedException("用户名或密码错误");
//					} else {
//						return teacher;
//					}
//				} catch (FtpException e) {
//					throw new AuthenticationFailedException("用户名或密码错误");
//				}
//			} else {
//				throw new AuthenticationFailedException("用户名或密码错误");
//			}
//		} else {
//			throw new AuthenticationFailedException("不支持的登录方式");
//		}
//	}
//
//	@Override
//	public String getAdminName() throws FtpException {
//		return "*";
//	}
//
//	@Override
//	public boolean isAdmin(String username) throws FtpException {
//		return true;
//	}

}
