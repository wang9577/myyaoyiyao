package com.ustudy.sys.web.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.ustudy.core.web.ctrl.BaseController;



@Controller
@RequestMapping(path = {"/tpl"})
public class TplCtrl extends BaseController {
	
	@RequestMapping(path = "/tpl.bi",method = {RequestMethod.GET})
	public String tplTest() {
		return "/template/template01/tpl.html";
	}
}
