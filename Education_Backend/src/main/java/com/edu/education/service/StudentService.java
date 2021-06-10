package com.edu.education.service;

import com.edu.education.common.ResponseEnum;
import com.edu.education.common.ServerResponse;
import com.edu.education.entity.Select_Class;
import com.edu.education.entity.Stu_Info;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {


    public ServerResponse getStudentInfo (String username);

    public ServerResponse getSelectedClass (String username);

    public ServerResponse getUnUploadedHomework (String username,String classid);

    public ServerResponse getUnMarkedHomework (String username, String classid);

    public ServerResponse getMarkedHomework (String username, String classid);

    public ServerResponse getAllCertification (String username);

    public ServerResponse getCertification (String username,String classid);

    public ServerResponse getLearnProcess (String username,String classid);

    public ServerResponse selectClass (String username,String classid);

    public ServerResponse uploadHomework (String username, String classid, Integer homeworknumber, MultipartFile file);
}
