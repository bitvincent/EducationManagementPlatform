package com.edu.education.dao;

import com.edu.education.entity.Stu_Info;
import com.edu.education.entity.Teacher_Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Teacher_InfoDao extends JpaRepository<Teacher_Info,Integer> {

    Teacher_Info findByNumber(@Param("number") String number);

    @Query(name = "findTeacheridByNumber",nativeQuery = true,value =
            "select teacherid from teacher_info where number=:number")
    String findTeacheridByNumber(@Param("number") String number);

    Teacher_Info findByTeacherid(String teacherid);

    boolean existsByNumber(String number);

}
