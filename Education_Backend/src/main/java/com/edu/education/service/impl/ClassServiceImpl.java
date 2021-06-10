package com.edu.education.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edu.education.common.ResponseEnum;
import com.edu.education.common.ServerResponse;
import com.edu.education.dao.*;
import com.edu.education.entity.Class_Info;
import com.edu.education.entity.Select_Class;
import com.edu.education.entity.Teacher_Info;
import com.edu.education.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private Class_InfoDao class_infoDao;
    @Autowired
    private Teacher_InfoDao teacher_infoDao;
    @Autowired
    private Stu_InfoDao stu_infoDao;
    @Autowired
    private Select_ClassDao select_classDao;
    @Autowired
    private School_InfoDao school_infoDao;

    /**
     *     List<
     *     classid（课程id,int）,
     *     classname（课程名）,
     *     schoolid（课程学校,int）,
     *     number（参加人数,int）,
     *     description(课程描述),
     *     classpic（课程图片）>
     *     **/
    @Override
    public ServerResponse getRecommendClass(Integer neednum) {
        List<Class_Info> classList = class_infoDao.getRecommendClass(neednum);
        JSONArray resultJson = new JSONArray();
        for(Class_Info class_info : classList){
            JSONObject temp = new JSONObject();
            temp.put("classid", class_info.getClassid());
            temp.put("classname", class_info.getClassname());
            temp.put("schoolid", class_info.getSchoolid());
            temp.put("number", class_info.getNumber());
            temp.put("description", class_info.getDescription());
            temp.put("classpic", class_info.getClasspic());
            resultJson.add(temp);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultJson);
    }

    /**
     * name（课程名）,
     * starttime(开始时间，YYYY-MM-DD)，
     * endtime(结束时间,  YYYY-MM-DD)，
     * learnhours（学时安排，int），
     * number（参加人数,int）,
     * description(课程描述),
     * classpic（课程图片，url）,
     * schoollogo(校徽图片，url)，
     * teachername（教师姓名），
     * title（教师职称，int）
     * **/
    @Override
    public ServerResponse getClassInfo(String classid) {
        Class_Info class_info = class_infoDao.findByClassid(classid);
        if(class_info==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_EXIST);
        JSONObject result = new JSONObject();
        result.put("name", class_info.getClassname());
        result.put("starttime", class_info.getStarttime());
        result.put("endtime", class_info.getEndtime());
        result.put("learnhours", class_info.getLearnhours());
        result.put("number", class_info.getNumber());
        result.put("description", class_info.getDescription());
        result.put("classpic", class_info.getClasspic());

        Teacher_Info teacher_info = teacher_infoDao.findByTeacherid(class_info.getTeacherid());
        result.put("teacherid", class_info.getTeacherid());
        if(school_infoDao.findBySchoolid(teacher_info.getSchoolid())==null)
            result.put("schoollogo", "");
        else
            result.put("schoollogo", school_infoDao.findBySchoolid(teacher_info.getSchoolid()).getSchoollogo());
        result.put("teachername", teacher_info.getTeachername());
        result.put("title", teacher_info.getTitle());
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(result);
    }

    @Override
    public ServerResponse getStudentClassInfo(String classid,String username) {
        Class_Info class_info = class_infoDao.findByClassid(classid);
        JSONObject result = new JSONObject();
        result.put("name", class_info.getClassname());
        result.put("starttime", class_info.getStarttime());
        result.put("endtime", class_info.getEndtime());
        result.put("learnhours", class_info.getLearnhours());
        result.put("number", class_info.getNumber());
        result.put("description", class_info.getDescription());
        result.put("classpic", class_info.getClasspic());
        result.put("teacherid", class_info.getTeacherid());

        Teacher_Info teacher_info = teacher_infoDao.findByTeacherid(class_info.getTeacherid());
        result.put("teachername", teacher_info.getTeachername());
        result.put("teacherid", class_info.getTeacherid());
        result.put("title", teacher_info.getTitle());
        result.put("schoollogo", school_infoDao.findBySchoolid(teacher_info.getSchoolid()).getSchoollogo());

        String studentid = stu_infoDao.findStudentidByNumber(username);
        Select_Class select_class = select_classDao.findByClassidAndStudentid(classid,studentid);
        result.put("select", (select_class!=null)?1:0);

        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(result);
    }

    /**
     * List<
     * classid（课程id,int）,
     * classname（课程名）,
     * schoolid（课程学校,int）,
     * number（参加人数,int）,
     * description(课程描述),
     * classpic（课程图片）
     * >
     * **/
    @Override
    public List<Class_Info> searchClass(String classname, Integer category, Integer schoolid) {
        return class_infoDao.findByClassnameAndCategoryAndSchoolid(classname,category,schoolid);
    }
}
