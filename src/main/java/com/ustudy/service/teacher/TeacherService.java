package com.ustudy.service.teacher;

import com.ustudy.entity.security.Identification;
import com.ustudy.entity.user.Chives;
import com.ustudy.entity.user.LoginUser;
import com.ustudy.utils.EntitiesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.ustudy.core.dao.support.bean.HqlBean;
import org.ustudy.core.security.RegisterKeyUtils;
import org.ustudy.core.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class TeacherService extends ServiceImpl {


    @Resource
    private RegisterKeyUtils registerKeyUtils;

    @Resource
    private EntitiesService entitiesService;

    @Transactional(readOnly=false,rollbackFor=Exception.class,propagation= Propagation.REQUIRED)
    public void saveTeacher(Chives t, String password) throws Exception {
            dao.save(t);
            List<Identification> idenList = this.dao.list(new HqlBean("from Identification where authcode = ?", Arrays.asList(t.getCode())));
            if(idenList.isEmpty()) {
                Identification iden=new Identification();
                iden.setAuthcode(t.getCode());
                iden.setPassword(registerKeyUtils.innerenc(password));
                entitiesService.setRecord(iden);
                this.dao.save(iden);
            }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class,propagation= Propagation.REQUIRED)
    public void changePassword(Chives user, String password)throws Exception {
        Identification iden= getEntity("from Identification where authcode=?",user.getCode());
        iden.setPassword(registerKeyUtils.innerenc(password));
        save(iden);
    }
}
