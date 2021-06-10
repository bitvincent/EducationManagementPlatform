package com.edu.education.service;

import com.edu.education.common.ServerResponse;
import com.edu.education.entity.Stu_Info;
import com.edu.education.entity.Teacher_Info;
import org.fisco.bcos.web3j.abi.datatypes.Int;

public interface UserService {

    String USER_NOT_EXIST = "USER_NOT_EXIST";
    String USER_TYPE_WRONG = "USER_TYPE_WRONG";
    String USER_BOTH_FAULT = "USER_BOTH_FAULT";
    String USER_SUCCESS = "USER_SUCCESS";

    String STUDENT_TYPE = "STUDENT_TYPE";
    String TEACHER_TYPE = "TEACHER_TYPE";

    ServerResponse registerTeacher(Teacher_Info teacher_info);
    ServerResponse loginTeacher(String username, String password);
    ServerResponse loginTeacherWithSMS(String username);

    ServerResponse registerStudent(Stu_Info stu_info);
    ServerResponse loginStudent(String username, String password);
    ServerResponse loginStudentWithSMS(String username);

    ServerResponse updateTeacherPassword(String username,String oldpassword,String newpassword);
    ServerResponse updateStudentPassword(String username,String oldpassword,String newpassword);

    String checkUserType(String username);
    ServerResponse handleUserTypeFault(String type);

}
