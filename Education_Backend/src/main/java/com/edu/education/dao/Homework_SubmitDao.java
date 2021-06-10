package com.edu.education.dao;

import com.edu.education.entity.Homework_Publish;
import com.edu.education.entity.Homework_Submit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Homework_SubmitDao extends JpaRepository<Homework_Submit,Integer> {

    @Query(name = "getRecommendClass",nativeQuery = true,value =
            "select * from homework_submit where classid=:classid and flag=0")
    List<Homework_Submit> findUnMarkedHomeworkTeacher(@Param("classid")String classid);

    @Query(name = "getRecommendClass",nativeQuery = true,value =
            "select * from homework_submit where classid=:classid and flag=1")
    List<Homework_Submit> findMarkedHomeworkTeacher(@Param("classid")String classid);

    @Query(name = "getAllUnMarkedHomeworkStudent", nativeQuery = true, value =
            "select * from homework_submit where studentid=:studentid and classid=:classid and flag=0")
    List<Homework_Submit> findUnMarkedHomeworkStudent(@Param("studentid")String studentid, @Param("classid")String classid);

    @Query(name = "getAllUnMarkedHomeworkStudent", nativeQuery = true, value =
            "select * from homework_submit where studentid=:studentid and flag=1")
    List<Homework_Submit> findMarkedHomeworkStudent(@Param("studentid")String studentid);

    Homework_Submit findByStudentidAndClassidAndHomeworknumber(String studentid, String classid, Integer homeworknumber);

    // 返回一个学生的所有提交作业
    @Query(name = "getHomeworkStudent", nativeQuery = true, value =
            "select * from homework_submit where studentid=:studentid and classid=:classid")
    List<Homework_Submit> findHomeworkSubmitStudent(@Param("studentid")String studentid, @Param("classid")String classid);

    Homework_Submit findByHomeworknumberAndClassid(Integer homeworknumber,String classid);

    Homework_Submit findByContent(String content);
}
