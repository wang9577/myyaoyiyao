package com.ustudy.web.admin;

import com.google.common.collect.Lists;
import com.ustudy.entity.user.Chives;
import com.ustudy.service.teacher.TeacherService;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.ustudy.collections.dao.support.ConditionMap;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.web.ctrl.BaseController;
import org.ustudy.core.web.services.DictService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * 教师管理
 */
@RestController
@RequestMapping("/admin/chives/")
public class TeacherMngCtrl extends BaseController {

    @Resource
    private DictService dictService;

    @Resource
    private TeacherService teacherService;

    @RequestMapping(path= {"/chives_list.bi"},method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String list() throws Exception {
        List<Chives> res=this.search("Chives","prm","code","asc");
        return new ConditionMap<>().append("status", true).append("total", BeanUtilsBean.getInstance().getProperty(res, "total"))
                .append("rows", Lists.<Chives,ConditionMap<Object>>transform(res, (Chives item)->new ConditionMap<>()
                        .append("id", item.getId())
                        .append("fid", item.getId())
                        .append("code", item.getCode())
                        .append("name", item.getName())
                )).toJsonString();
    }

    @RequestMapping(path="/chives_save.bi",method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String save()throws Exception{
        Chives vo=this.createBean("Chives", "form", null);
        teacherService.saveTeacher(vo,"123456");
        return new ConditionMap<>().append("status", true).append("msg","保存成功").toJsonString();
    }
//
//
    @RequestMapping(path="/chives_remove.bi",method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String remove()throws Exception{
        List<Chives> items=service.list(new HqlBean(Chives.class.getSimpleName(),new ConditionMap().append("id", Arrays.asList(get("ids").split(",")))));
        this.service.remove(items);
        return new ConditionMap<>().append("status", true).append("msg","删除成功").toJsonString();
    }
//
//
    @RequestMapping(path= {"/chives_change_password.bi"},method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String teacherChangePassword()throws Exception {
        Chives t=this.service.getEntity(Chives.class,get("id"));
        this.teacherService.changePassword(t, get("password"));
        return this.createResult(true).append("msg","保存成功").toJsonString();
    }
}
