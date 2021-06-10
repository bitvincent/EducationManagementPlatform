package com.edu.education.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edu.education.common.DisableToken;
import com.edu.education.common.ResponseEnum;
import com.edu.education.common.ServerResponse;
import com.edu.education.entity.Class_Info;
import com.edu.education.service.ClassService;
import com.edu.education.service.UserService;
import com.sun.istack.NotNull;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/class")
@Api(tags = "课程相关操作接口")
public class ClassController {

    @Autowired
    private ClassService classService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;

    /**
     *     List<
     *     classid（课程id,int）,
     *     classname（课程名）,
     *     schoolid（课程学校,int）,
     *     number（参加人数,int）,
     *     description(课程描述),
     *     classpic（课程图片）>
     *     **/
    @DisableToken
    @PostMapping("/getRecommendClass")
    @ResponseBody
    public ServerResponse getRecommendClass (Integer neednum){
        if(neednum == null)
            neednum =  11;
        return classService.getRecommendClass(neednum);

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
    @DisableToken
    @PostMapping("/searchClass")
    @ResponseBody
    public ServerResponse searchClass (String classname, Integer category, Integer schoolid){
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(classService.searchClass(classname,category,schoolid));
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
    @DisableToken
    @PostMapping("/getClassInfo")
    @ResponseBody
    public ServerResponse getClassInfo (@RequestParam("classid")String classid){
        return classService.getClassInfo(classid);
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
    @PostMapping("/getStudentClassInfo")
    @ResponseBody
    public ServerResponse getStudentClassInfo (@RequestParam("classid") String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return classService.getStudentClassInfo(classid,username);
    }


}
