package com.ustudy.web.yaoyiyao;

import com.ustudy.entity.chives.ChivesFund;
import com.ustudy.entity.chives.ChivesFundSelect;
import com.ustudy.entity.security.Identification;
import com.ustudy.entity.user.Chives;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.ustudy.collections.dao.support.ConditionMap;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.exceptions.AuthLoginException;
import org.ustudy.core.pools.SystemUtils;
import org.ustudy.core.security.RegisterKeyUtils;
import org.ustudy.core.web.ctrl.BaseController;
import org.ustudy.core.web.services.LoginService;
import org.ustudy.core.web.tpl.fun.CustomFunction;
import org.ustudy.utils.DateUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(path="/yaoyiyao/")
public class LoginCtrl extends BaseController {

    @Resource
    private LoginService loginService;

    @Resource(name = "registerKeyUtils")
    private RegisterKeyUtils registerKeyUtils;


    @Resource(name="jcus")
    private CustomFunction jcus;

    @PostConstruct
    public void init() {
        jcus.addFunction("initFund", (List args) -> {
            try {
                return this.initFund(args.get(0).toString());
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        });


    }

    public Map<String,Object> initFund(String chivesId) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        String day = DateUtils.format(new Date(),"yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long dayTime = sdf.parse(day).getTime();
        String id = null;
        String content = null;
        if(chivesId!=null && !"".equals(chivesId)){
            List<ChivesFundSelect> chivesFundSelects = service.list(new HqlBean("from ChivesFundSelect where chives.id=? and selectTime>=?",Arrays.asList(chivesId,dayTime)));
            if(chivesFundSelects.size()>0){
                ChivesFundSelect c = chivesFundSelects.get(0);
                id = c.getId();
                content = c.getFundCode()+"      "+c.getFundName();
            }
        }
        resultMap.put("id",id);
        resultMap.put("content",content);
        return resultMap;
    }

    @RequestMapping(path= {"/login.bi"},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String login(HttpServletRequest request, HttpServletResponse response, String username,String password) throws Exception{
        username = username.trim();

        Chives chives = service.getEntity("from Chives where code=? ",username);
        if(chives==null){
            return  new ConditionMap<>().append("status", false).append("msg","账号不存在").toJsonString();
        }else{
            List<Identification> identifications=this.service.<Identification>list(new HqlBean("from Identification where authcode=?", Arrays.asList(username)));
            HttpSession session =  request.getSession();
            if(identifications.size()>0){
                if(registerKeyUtils.innerdec(identifications.get(0).getPassword()).equals(password)){
                    session.setAttribute("id",chives.getId());
                    return  new ConditionMap<>().append("status", true).append("msg","成功").toJsonString();

                }else {
                    return  new ConditionMap<>().append("status", false).append("msg","账号或密码错误").toJsonString();

                }
            }else {
                return  new ConditionMap<>().append("status", false).append("msg","账号不存在").toJsonString();

            }
        }

    }

    @RequestMapping(path= {"/loginOut.bi"},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String loginOut(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HttpSession session1 = request.getSession();
        session1.invalidate();
        return  new ConditionMap<>().append("status", true).append("msg","成功").toJsonString();

    }

    @RequestMapping(path= {"/goYaoyiyao.bi"},produces= {APPLICATION_JSON_UTF8_VALUE})
    public String goYaoyiyao(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String day = DateUtils.format(new Date(),"yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long dayTime = sdf.parse(day).getTime();
        boolean status = false;
        String msg = "";
        String content = "";
        String chivesId = get("chivesId");
        if(chivesId!=null &&  !"".equals(chivesId)){
            Chives chives = service.getEntity(Chives.class,chivesId);
            List<ChivesFund> chivesFunds = service.list(new HqlBean("from ChivesFund where chives.id=?",Arrays.asList(chivesId)));
            if(chivesFunds.size()>0){
                List<ChivesFundSelect> chivesFundSelects = service.list(new HqlBean("from ChivesFundSelect where chives.id=? and selectTime>=?",Arrays.asList(chivesId,dayTime)));
                if(chivesFundSelects.size()<1){
                    Random random = new Random();
                    int n = random.nextInt(chivesFunds.size());
                    ChivesFund c =   chivesFunds.get(n);
                    ChivesFundSelect chivesFundSelect = new ChivesFundSelect();
                    chivesFundSelect.setChives(chives);
                    chivesFundSelect.setFundCode(c.getFundCode());
                    chivesFundSelect.setFundName(c.getFundName());
                    chivesFundSelect.setSelectTime(System.currentTimeMillis());
                    chivesFundSelect.setDayTime(dayTime);
                    service.save(chivesFundSelect);
                    content = c.getFundCode()+"      "+c.getFundName();

                    status = true;
                }else {
                    msg = "您今天已经摇过了!";
                }


            }else {
                msg = "未设置基金!";
            }
        }else {
            msg = "请登录!";
        }
        return  new ConditionMap<>().append("status", status).append("msg",msg).append("content",content).toJsonString();

    }
}
