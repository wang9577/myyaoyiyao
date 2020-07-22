package com.ustudy.web.admin;

import com.google.common.collect.Lists;
import com.ustudy.entity.chives.ChivesFund;
import com.ustudy.entity.chives.ChivesFundSelect;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.ustudy.collections.dao.support.ConditionMap;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.web.ctrl.BaseController;
import org.ustudy.utils.DateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/admin/fundselect/")
public class FundSelectMngCtrl extends BaseController {

    @RequestMapping(path= {"/fundselect_list.bi"},method= {RequestMethod.POST},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String list() throws Exception {
        List<ChivesFundSelect> res=this.search("ChivesFundSelect","prm","selectTime","desc");
        return new ConditionMap<>().append("status", true).append("total", BeanUtilsBean.getInstance().getProperty(res, "total"))
                .append("rows", Lists.<ChivesFundSelect,ConditionMap<Object>>transform(res, (ChivesFundSelect item)->new ConditionMap<>()
                        .append("id", item.getId())
                        .append("chives.id",item.getChives().getId())
                        .append("fid", item.getId())
                        .append("fundCode", item.getFundCode())
                        .append("fundName", item.getFundName())
                        .append("dayTime", DateUtils.format(new Date(item.getDayTime()),"yyyy-MM-dd"))
                        .append("selectTime", DateUtils.format(new Date(item.getSelectTime()),"yyyy-MM-dd HH:mm:dd"))
                )).toJsonString();
    }




}
