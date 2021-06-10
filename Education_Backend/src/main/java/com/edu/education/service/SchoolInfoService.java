package com.edu.education.service;

import com.edu.education.common.ServerResponse;
import com.edu.education.dao.School_InfoDao;
import com.edu.education.entity.School_Info;
import com.edu.education.helper.fileHelper.PictureHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolInfoService {
    @Autowired
    private School_InfoDao school_infoDao;
    @Autowired
    private PictureHelper pictureHelper;

    @Value(value = "${com.visit_path}")
    private String visit_path;
    @Value(value = "${com.ip}")
    private String ip_port;

    public ServerResponse uploadSchoolLogo(MultipartFile file, String schoolname){
        Integer schoolid = school_infoDao.findMaxSchoolid();
        if(schoolid==null)
            schoolid = 0;
        String url = pictureHelper.saveSchoolLogo(file,schoolid+1);
        School_Info school_info = new School_Info();
        school_info.setSchoolid(schoolid+1);
        school_info.setSchoollogo(url);
        school_info.setSchoolname(schoolname);
        School_Info result = school_infoDao.save(school_info);
        if(result==null)
            return ServerResponse.getInstance().failed();
        else
            return ServerResponse.getInstance().ok().data(result);
    }

    @Value(value = "${config.schoolinfo.s1}")
    private String s1;
    @Value(value = "${config.schoolinfo.s2}")
    private String s2;
    @Value(value = "${config.schoolinfo.s3}")
    private String s3;
    @Value(value = "${config.schoolinfo.s4}")
    private String s4;
    @Value(value = "${config.schoolinfo.s5}")
    private String s5;
    @Value(value = "${config.schoolinfo.s6}")
    private String s6;
    @Value(value = "${config.schoolinfo.s7}")
    private String s7;

    public ServerResponse saveDefaultSchoolLogo(){
        List<String> name_list = new ArrayList<>();
        name_list.add(0,"PKU");
        name_list.add(1,s1);
        name_list.add(2,s2);
        name_list.add(3,s3);
        name_list.add(4,s4);
        name_list.add(5,s5);
        name_list.add(6,s6);
        name_list.add(7,s7);

        for(Integer schoolid = 1;schoolid<8;schoolid++){
            String url = ip_port + visit_path + "/school/" + schoolid.toString() +".png";
            School_Info school_info = new School_Info();
            school_info.setSchoolid(schoolid);
            school_info.setSchoollogo(url);
            school_info.setSchoolname(name_list.get(schoolid));
            school_infoDao.save(school_info);
        }
        return ServerResponse.getInstance().ok();
    }


}
