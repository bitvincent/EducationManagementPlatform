package com.edu.education.controller;

import com.edu.education.common.ServerResponse;
import com.edu.education.service.TeacherService;
import com.edu.education.service.UserService;
import com.sun.istack.NotNull;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@RestController
@RequestMapping("/teacher")
@Api(tags = "老师相关操作接口")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;

    @PostMapping("/publishClass")
    @ResponseBody
    ServerResponse publishClass(@RequestParam("classname") String classname,
                                @RequestParam("category")   Integer category,
                                @RequestParam("starttime")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date starttime,
                                @RequestParam("endtime")    @DateTimeFormat(pattern = "yyyy-MM-dd") Date endtime,
                                @RequestParam("learnhours") Integer learnhours,
                                @RequestParam("description")String description,
                                @RequestParam("file")       MultipartFile file){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.publishClass(username,classname,category,starttime,endtime,learnhours,description,file);
    }

    @PostMapping("/publishHomework")
    @ResponseBody
    ServerResponse publishHomework(@RequestParam("classid")         String classid,
                                   @RequestParam("homeworknumber")  Integer homeworknumber,
                                   @RequestParam("homeworkname")    String homeworkname,
                                   @RequestParam("description")     String description,
                                   @RequestParam("ddl") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date ddl){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.publishHomework(username,classid,homeworknumber,homeworkname,description,ddl);
    }

    @PostMapping("/markHomework")
    @ResponseBody
    ServerResponse markHomework(@RequestParam("classid")        String  classid,
                                @RequestParam("grade")          Integer grade,
                                @RequestParam("homeworknumber") Integer homeworknumber,
                                @RequestParam("docaddress")     String  docaddress){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.markHomework(username,classid,grade,homeworknumber,docaddress);
    }

    @PostMapping("/setFinalGrade")
    @ResponseBody
    ServerResponse setFinalGrade(@RequestParam("classid")   String  classid,
                                 @RequestParam("studentid") String  studentid,
                                 @RequestParam("finalgrade")Integer finalgrade,
                                 @RequestParam("pass")      Integer pass){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.setFinalGrade(username,classid,studentid,finalgrade,pass);
    }

    /**
     * number(手机号)
     * teachername(老师名称)
     * gender（性别，int）
     * idcard(身份证号码)
     * schoolid(学校id，int)
     * title (职称，int)
     * teacherlogo(老师头像,url)
     * **/

    @PostMapping("/getTeacherInfo")
    @ResponseBody
    ServerResponse getTeacherInfo(){
        String username = (String) request.getAttribute("username");

        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.getTeacherInfo(username);
    }


    /**
     * List<
     * classid（课程id,int）,
     * classname（课程名）,
     * schoolid（课程学校,int）,
     * number（参加人数,int）,
     * description(课程描述),
     * classpic（课程图片）>
     * **/
    @PostMapping("/getPublishedClass")
    @ResponseBody
    ServerResponse getPublishedClass(){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.getPublishedClass(username);
    }


    /**
     * List<
     * homeworknumber(作业次数，int)
     * homeworkname(作业名称)
     * description(作业详情)
     * starttime(发布时间 YYYY-MM-DD HH:MM:SS)
     * ddl(截止时间 YYYY-MM-DD HH:MM:SS)
     * >
     * **/
    @PostMapping("/getPublishedHomework")
    @ResponseBody
    ServerResponse getPublishedHomework(@RequestParam("classid") String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.getPublishedHomework(username,classid);
    }

    /**
     *   classname(课程名称)
     *   category (课程门类，int)
     *   homeworknumber(作业次数, int)
     *   homeworkname（作业名称）
     *   description（作业详情）
     *   starttime（发布时间 YYYY-MM-DD HH:MM:SS）
     *   ddl（截止时间 YYYY-MM-DD HH:MM:SS）
     *   **/
    @PostMapping("/getTeacherHomeworkInfo")
    @ResponseBody
    ServerResponse getTeacherHomeworkInfo(@RequestParam("classid") String classid,
                                          @RequestParam("homeworknumber") Integer homeworknumber){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.getTeacherHomeworkInfo(username,classid,homeworknumber);
    }


    /**
     * List<
     *   homeworknumber(作业次数，int)
     *   homeworkname(作业题目)
     *   studentname(学生姓名)
     *   uploadtime(作业上传时间 YYYY-MM-DD HH:MM:SS)
     *   ddl(截止时间 YYYY-MM-DD HH:MM:SS)
     *   description(作业详情，批改作业页面需要)
     *   docname(学生作业的文档名称，批改作业页面需要)
     *   docaddress(学生作业的文档下载地址，批改作业页面需要)
     * >
     * **/
    @PostMapping("/getUnMarkedHomework")
    @ResponseBody
    ServerResponse getUnMarkedHomework(@RequestParam("classid") String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.getUnMarkedHomework(username,classid);
    }



    /**
     * List<
     *   homeworknumber(作业次数，int)
     *   homeworkname(作业题目)
     *   studentname(学生姓名)
     *   uploadtime(作业上传时间 YYYY-MM-DD HH:MM:SS)
     *   marktime(作业批改时间 YYYY-MM-DD HH:MM:SS)
     *   description(作业详情，批改作业页面需要)
     *   docname(学生作业的文档名称，批改作业页面需要)
     *   docaddress(学生作业的文档下载地址，批改作业页面需要)
     *   grade(分数)
     * >
     * **/
    @PostMapping("/getMarkedHomework")
    @ResponseBody
    ServerResponse getMarkedHomework(@RequestParam("classid")   String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.getMarkedHomework(username,classid);
    }

    /**
     *   List<
     *     studentid(学生id)
     *     studentname(学生姓名)
     *     grades：（成绩
     *       List<
     *         作业次数 成绩
     *         1      ：80
     *         2      ：100
     *         3      ：0
     *         ..
     *         ..
     *       >
     *     ）
     *   >
     *   **/
    @PostMapping("/getAllGrade")
    @ResponseBody
    ServerResponse getAllGrade(@RequestParam("classid") String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return teacherService.getAllGrade(username,classid);
    }



}
