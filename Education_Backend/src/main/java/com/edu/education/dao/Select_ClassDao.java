package com.edu.education.dao;

import com.edu.education.entity.Select_Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Select_ClassDao extends JpaRepository<Select_Class,Integer> {

    List<Select_Class> findAllByStudentid (String studentid);
    List<Select_Class> findByClassid(String classid);
    Select_Class findByClassidAndStudentid(String classid,String studentid);

}
