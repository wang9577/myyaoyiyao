package com.ustudy.resource.web.ctrl;

import java.io.File;
import java.io.OutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ustudy.core.pools.SystemUtils;
import org.ustudy.core.web.ctrl.BaseController;
import org.ustudy.core.web.services.DictService;
import org.ustudy.core.web.services.ResourceService;
import org.ustudy.core.web.tpl.fun.CustomFunction;


@Controller
@RequestMapping(path= {"/resource"})
@Lazy(false)
public class ResourceCtrl extends BaseController {
	
	public static final String IMAGES_AVATAR="/avatar";
	public static final String IMAGES_COVER="/cover";
	
	@Resource(name="resourceService")
	private ResourceService resourceService;
	
	@Resource(name="dictService")
	private DictService dictService;
	
	@Resource(name="jcus")
	private CustomFunction jcus;
	
	@PostConstruct
	private void init() {
		jcus.addFunction("avatars", list->Stream.of(
				new File(new StringBuilder(SystemUtils.getImagesRoot()).append(IMAGES_AVATAR).toString()).listFiles()
				).map(File::getName).map(name->name.split("\\.")[0]).collect(Collectors.toList())
				);
		jcus.addFunction("cover", list->Stream.of(
				new File(new StringBuilder(SystemUtils.getImagesRoot()).append(IMAGES_COVER).toString()).listFiles()
				).map(File::getName).map(name->name.split("\\.")[0]).collect(Collectors.toList())
				);
	}
	
	@RequestMapping(value="/avatar/{code}/{width}/{height}",method={RequestMethod.GET})
	private@ResponseBody void avatar(HttpServletResponse response,@PathVariable(name="code")String code,@PathVariable(name="width") int width,@PathVariable(name="height")int height) {
		try(OutputStream out=response.getOutputStream()){
			byte[] b= resourceService.thumb(new StringBuilder(IMAGES_AVATAR).append("/").append(code).append(".gif").toString(), width, height);
			out.write(b);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
		}
		response.setContentType("image/gif");
	}
	
	@RequestMapping(value="/cover/{code}/{width}/{height}",method={RequestMethod.GET})
	private@ResponseBody void cover(HttpServletResponse response,@PathVariable(name="code")String code,@PathVariable(name="width") int width,@PathVariable(name="height")int height) {
		try(OutputStream out=response.getOutputStream()){
			out.write(resourceService.thumb(new StringBuilder(IMAGES_COVER).append("/").append(code).append(".jpg").toString(), width, height));
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
		}
		response.setContentType("image/jpeg");
	}
	
}
