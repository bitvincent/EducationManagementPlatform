package com.edu.education.dao;

import com.edu.education.entity.Stu_Info;
import com.edu.education.entity.Teacher_Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Stu_InfoDao extends JpaRepository<Stu_Info,Integer> {

    Stu_Info findByNumber(@Param("number") String number);

    @Query(name = "findStudentidByNumber",nativeQuery = true,value =
            "select studentid from stu_info where number=:number")
    String findStudentidByNumber(@Param("number") String number);

    @Query(name = "findStudentnameByStudentid",nativeQuery = true,value =
            "select studentname from stu_info where studentid=:studentid")
    String findStudentnameByStudentid(String studentid);

    boolean existsByNumber(String number);
}
