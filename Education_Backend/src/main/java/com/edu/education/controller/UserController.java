package com.edu.education.controller;

import com.edu.education.common.DisableToken;
import com.edu.education.common.ResponseEnum;
import com.edu.education.common.ServerResponse;
import com.edu.education.entity.Stu_Info;
import com.edu.education.entity.Teacher_Info;
import com.edu.education.helper.smsHelper.SMSTokenHelper;
import com.edu.education.helper.smsHelper.SMSHelper;
import com.edu.education.service.UserService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequestMapping("/user")
@Api(tags = "用户相关操作接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SMSTokenHelper smsTokenHelper;
    @Autowired
    private SMSHelper smsHelper;


    @DisableToken
    @PostMapping("/loginStudent")
    @ResponseBody
    public ServerResponse loginStudent (@RequestParam("username") String username,
                                        @RequestParam("password") String password){
        return userService.loginStudent(username,password);
    }

    @DisableToken
    @PostMapping("/loginTeacher")
    @ResponseBody
    public ServerResponse loginTeacher(@RequestParam("username") String username,
                                       @RequestParam("password") String password){
        return userService.loginTeacher(username,password);
    }

    /**
     *
     *  number
     *  password
     *  studentname
     *  gender
     *  idcard
     *  schoolspecial
     *  institute
     *  major
     *  sno
     *  degree
     *  intime
     *  outtime
     */
    @DisableToken
    @PostMapping("/registerStudent")
    @ResponseBody
    public ServerResponse registerStudent (@Valid Stu_Info stu_info){
        //用户名或密码为空
        if( null == stu_info.getNumber() || null == stu_info.getPassword() )
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INVALID_PARAM);
        return userService.registerStudent(stu_info);
    }

    @DisableToken
    @PostMapping("/registerTeacher")
    @ResponseBody
    public ServerResponse registerTeacher(@Valid Teacher_Info teacher_info){
        return userService.registerTeacher(teacher_info);
    }


    @PostMapping("/updateTeacherPassword")
    @ResponseBody
    public ServerResponse updateTeacherPassword(@RequestParam("oldpassword") String oldpassword,
                                                @RequestParam("newpassword") String newpassword){
        //新密码校验
        if(newpassword==null || newpassword=="")
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INVALID_PARAM);

        String username = (String) request.getAttribute("username");
        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.TEACHER_TYPE)) return userService.handleUserTypeFault(user_type);

        return userService.updateTeacherPassword(username,oldpassword,newpassword);
    }

    @PostMapping("/updateStudentPassword")
    @ResponseBody
    public ServerResponse updateStudentPassword(@RequestParam("oldpassword") String oldpassword,
                                                @RequestParam("newpassword") String newpassword){
        if(newpassword==null || newpassword=="")
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INVALID_PARAM);
        String username = (String) request.getAttribute("username");

        String user_type = userService.checkUserType(username);
        if(!user_type.equals(UserService.STUDENT_TYPE)) return userService.handleUserTypeFault(user_type);

        return userService.updateStudentPassword(username,oldpassword,newpassword);
    }

    @DisableToken
    @RequestMapping(value = "/registerStudentWithSMS",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse registerStudentWithSMS(@Valid Stu_Info stu_info,
                                              @RequestParam  String verify_token,
                                              @RequestParam  String verify_code){
        int result = smsTokenHelper.checkToken(verify_token,verify_code,stu_info.getNumber());
        if(SMSTokenHelper.TOKEN_SUCCESS == result)
            return userService.registerStudent(stu_info);
        else
            return smsTokenHelper.handleResult(result);
    }

    @DisableToken
    @RequestMapping(value = "/registerTeacherWithSMS",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse registerTeacherWithSMS(@Valid Teacher_Info teacher_info,
                                                 @RequestParam  String verify_token,
                                                 @RequestParam  String verify_code){
        int result = smsTokenHelper.checkToken(verify_token,verify_code,teacher_info.getNumber());
        if(SMSTokenHelper.TOKEN_SUCCESS == result)
            return userService.registerTeacher(teacher_info);
        else
            return smsTokenHelper.handleResult(result);
    }

    @DisableToken
    @RequestMapping(value = "/loginStudentWithSMS",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse loginStudentWithSMS(@RequestParam String username,
                                              @RequestParam String verify_token,
                                              @RequestParam String verify_code){
        int result = smsTokenHelper.checkToken(verify_token,verify_code,username);
        if(SMSTokenHelper.TOKEN_SUCCESS == result)
            return userService.loginStudentWithSMS(username);
        else
            return smsTokenHelper.handleResult(result);
    }

    @DisableToken
    @RequestMapping(value = "/loginTeacherWithSMS",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse loginTeacherWithSMS(@RequestParam String username,
                                              @RequestParam String verify_token,
                                              @RequestParam String verify_code){
        int result = smsTokenHelper.checkToken(verify_token,verify_code,username);
        if(SMSTokenHelper.TOKEN_SUCCESS == result)
            return userService.loginTeacherWithSMS(username);
        else
            return smsTokenHelper.handleResult(result);
    }

    @DisableToken
    @PostMapping("/sendSMS")
    @ResponseBody
    public ServerResponse sendSMS(@RequestParam("number") String number, HttpServletResponse httpServletResponse){
        try {
            String createText = smsTokenHelper.createNumber(4);
            String token = smsTokenHelper.createToken(createText,number);
            httpServletResponse.setHeader("Verify-Token", token);
            String result = smsHelper.sendSMS("+86"+number,createText);
            if(result.equals("Ok"))
                return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SUCCESS).data(token);
            else
                return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.FAILED).data(result);        }catch (Exception e){
            e.printStackTrace();
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.FAILED);
        }
    }

    @DisableToken
    @PostMapping("/verifySMS")
    @ResponseBody
    public ServerResponse verifySMS(String verify_code, String verify_token,String number) {
        Claims claims = smsTokenHelper.getTokenClaim(verify_token);
        if(claims == null || smsTokenHelper.isTokenExpired(claims.getExpiration())){
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.KAPTCHA_EXPIRED);
        }
        if (StringUtils.isEmpty(verify_token) || SMSTokenHelper.TOKEN_SUCCESS != smsTokenHelper.checkToken(verify_token,verify_code,number)) {
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SMS_ERROR);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SUCCESS);
    }


}
