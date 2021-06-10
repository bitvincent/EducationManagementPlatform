package com.edu.education.dao;

import com.edu.education.entity.Homework_Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Homework_PublishDao extends JpaRepository<Homework_Publish,Integer> {
    // 这门课的所有作业
    List<Homework_Publish> findAllByClassid(String classid);

    // 这门课的某一次作业
    Homework_Publish findByClassidAndHomeworknumber(String classid, Integer homeworknumber);

    @Query(name = "getHomeworkNumberByClassid", nativeQuery = true, value =
            "select count(homeworknumber) from homework_publish where classid=:classid")
    Integer getHomeworkNumberByClassid(@Param("classid") String classid);
}
