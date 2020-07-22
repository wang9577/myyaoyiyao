package com.ustudy.web.admin;

import com.google.common.collect.Lists;
import com.ustudy.entity.chives.ChivesFund;
import com.ustudy.entity.user.Chives;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.ustudy.collections.dao.support.ConditionMap;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.web.ctrl.BaseController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/admin/fund/")
public class FundMngCtrl extends BaseController {

    @RequestMapping(path= {"/fund_list.bi"},method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String list() throws Exception {
        List<ChivesFund> res=this.search("ChivesFund","prm","fundCode","asc");
        return new ConditionMap<>().append("status", true).append("total", BeanUtilsBean.getInstance().getProperty(res, "total"))
                .append("rows", Lists.<ChivesFund,ConditionMap<Object>>transform(res, (ChivesFund item)->new ConditionMap<>()
                        .append("id", item.getId())
                        .append("chives.id",item.getChives().getId())
                        .append("fid", item.getId())
                        .append("fundCode", item.getFundCode())
                        .append("fundName", item.getFundName())
                )).toJsonString();
    }


    @RequestMapping(path="/fund_save.bi",method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String save()throws Exception{
        ChivesFund vo=this.createBean("ChivesFund", "form", null);
        service.save(vo);
        return new ConditionMap<>().append("status", true).append("msg","保存成功").toJsonString();
    }
    //
//
    @RequestMapping(path="/fund_remove.bi",method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String remove()throws Exception{
        List<ChivesFund> items=service.list(new HqlBean(ChivesFund.class.getSimpleName(),new ConditionMap().append("id", Arrays.asList(get("ids").split(",")))));
        this.service.remove(items);
        return new ConditionMap<>().append("status", true).append("msg","删除成功").toJsonString();
    }


}
