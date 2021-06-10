package com.edu.education.controller;

import com.edu.education.common.ServerResponse;
import com.edu.education.service.StudentService;
import com.edu.education.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/student")
@Api(tags = "学生相关操作接口")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;

    /**
     *      number(手机号)
     *      password(密码)
     *      studentname(姓名)
     *      gender (性别 int 男-1、女-2)
     *      idcard (身份证号码 String)
     *      schoolspecial(学校名称 String)
     *      institute(院系 String)
     *      major(专业 String)
     *      sno(学号 String)
     *      intime（入学年月 YYYY：MM：DD）
     *      outtime（毕业年月 YYYY：）
     *      degree (类别 int，小学-1、初中-2、高中-3、专科-4、本科-5、硕士-6、博士-7)
     *      studentlogo (头像，url)
     *      **/
    @PostMapping("/getStudentInfo")
    @ResponseBody
    public ServerResponse getStudentInfo(){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return studentService.getStudentInfo(username);
    }

    @PostMapping("/selectClass")
    @ResponseBody
    public ServerResponse selectClass (@RequestParam("classid") String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return studentService.selectClass(username,classid);
    }

    @PostMapping("/uploadHomework")
    @ResponseBody
    public ServerResponse uploadHomework (@RequestParam("classid") String classid,
                                          @RequestParam("homeworknumber") Integer homeworknumber,
                                          @RequestParam("file") MultipartFile file){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return studentService.uploadHomework(username,classid,homeworknumber,file);
    }

    /**
     *     List<
     *     classid（课程id,int）,
     *     classname（课程名）,
     *     schoolid（课程学校,int）,
     *     number（参加人数,int）,
     *     description(课程描述),
     *     classpic（课程图片）
     *     **/
    @PostMapping("/getSelectedClass")
    @ResponseBody
    public ServerResponse getSelectedClass (){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return studentService.getSelectedClass(username);
    }

    /**
     *   List<
     *   homeworknumber(作业次数，int)
     *   homeworkname(作业名称)
     *   description(作业详情)
     *   starttime(发布时间 YYYY-MM-DD HH:MM:SS)
     *   ddl(截止时间 YYYY-MM-DD HH:MM:SS)
     *   >
     *   **/
    @PostMapping("/getUnUploadedHomework")
    @ResponseBody
    public ServerResponse getUnUploadedHomework (@RequestParam("classid")String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return studentService.getUnUploadedHomework(username,classid);
    }


    /**
     *   List<
     *   homeworknumber(作业次数，int)
     *   homeworkname(作业题目)
     *   uploadtime(作业上传时间 YYYY-MM-DD HH:MM:SS)
     *   ddl(截止时间 YYYY-MM-DD HH:MM:SS)
     *   description(作业详情，批改作业页面需要)
     *   docname(学生作业的文档名称，批改作业页面需要)
     *   docaddress(学生作业的文档下载地址，批改作业页面需要)
     *   >
     *   **/
    @PostMapping("/getUnMarkedHomework")
    @ResponseBody
    public ServerResponse getUnMarkedHomework (@RequestParam("classid") String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return studentService.getUnMarkedHomework(username,classid);
    }

    /**
     *   List<
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
    public ServerResponse getMarkedHomework (@RequestParam("classid") String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return studentService.getMarkedHomework(username,classid);
    }

    /**
     *   List<
     *     classid(课程id)
     *     classstatus(通过状态 int 学习中 0 ； 已通过 1 ；未通过 2)
     *     classname（课程名）
     *     passtime（发证时间，即打分时间，YYYY-MM-DD）
     *   >
     *   **/
    @PostMapping("/getAllCertification")
    @ResponseBody
    public ServerResponse getAllCertification (){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return studentService.getAllCertification(username);
    }

    /**
     *   studentname(学生名)
     *   schoolspecial（学生学校 String）
     *   institute(院系)
     *   sno(学号）
     *   classname（课程名）
     *   teachername（教师名）
     *   finalgrade（课程成绩）
     *   blockchainid(区块链ID String)
     *   block_number（成绩块号）
     *   tx_hash（交易哈希）
     *   tx_url（二维码url）
     *   passtime（发证时间 YYYY:MM:DD）
     *   **/
    @PostMapping("/getCertification")
    @ResponseBody
    public ServerResponse getCertification (@RequestParam("classid") String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return studentService.getCertification(username,classid);
    }

    /**
     *   studentname(学生名)
     *   schoolspecial（学生学校 String）
     *   institute(院系)
     *   sno(学号）
     *   classname（课程名）
     *   teachername（教师名）
     *   finalgrade（课程成绩）
     *   blockchainid(区块链ID String)
     *   block_number（成绩块号）
     *   tx_hash（交易哈希）
     *   tx_url（二维码url）
     *   passtime（发证时间 YYYY:MM:DD）
     *   **/
    @PostMapping("/getLearnProcess")
    @ResponseBody
    public ServerResponse getLearnProcess (@RequestParam("classid") String classid){
        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return studentService.getLearnProcess(username,classid);
    }


}
