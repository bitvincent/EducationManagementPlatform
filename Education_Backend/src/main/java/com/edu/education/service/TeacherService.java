package com.edu.education.service;

import com.edu.education.common.ServerResponse;
import com.edu.education.entity.Class_Info;
import com.edu.education.entity.Homework_Publish;
import com.edu.education.entity.Teacher_Info;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface TeacherService {

    ServerResponse getTeacherInfo(String username);

    ServerResponse getPublishedClass(String username);

    ServerResponse getPublishedHomework(String username, String classid);

    ServerResponse getTeacherHomeworkInfo(String username,String classid,Integer homeworknumber);

    ServerResponse getUnMarkedHomework(String username,String classid);

    ServerResponse getMarkedHomework(String username,String classid);

    ServerResponse getAllGrade(String username,String classid);

    ServerResponse publishHomework(String username,String classid,Integer homeworknumber,String homeworkname, String description,
                                   Date ddl);
    ServerResponse publishClass(String username,String classname, Integer category, Date starttime, Date endtime,
                                Integer learnhours, String description, MultipartFile file);

    ServerResponse setFinalGrade(String username,String classid,String studentid,Integer finalgrade,Integer pass);

    ServerResponse markHomework(String username,String classid,Integer grade,Integer Homeworknumber,String docaddress);


}
