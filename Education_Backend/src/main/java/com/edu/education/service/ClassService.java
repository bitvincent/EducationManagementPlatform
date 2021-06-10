package com.edu.education.service;

import com.edu.education.common.ServerResponse;
import com.edu.education.entity.Class_Info;

import java.util.List;

public interface ClassService {

    public ServerResponse getRecommendClass (Integer neednum);

    public List<Class_Info> searchClass (String classname, Integer category, Integer schoolid);

    public ServerResponse getClassInfo (String classid);

    public ServerResponse getStudentClassInfo(String classid,String username);
}
