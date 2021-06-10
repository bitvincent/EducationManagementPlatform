package com.edu.education.dao;

import com.edu.education.entity.Class_Grade;
import org.fisco.bcos.web3j.abi.datatypes.Int;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Class_GradeDao extends JpaRepository<Class_Grade,Integer> {
    List<Class_Grade> findAllByStudentid (String studentid);

    Class_Grade findByStudentidAndClassid(String studentid,String classid);

    @Query(name = "findFinalgradeByStudentidAndClassid",nativeQuery = true,value =
            "select finalgrade from class_grade where studentid=:studentid and classid=:classid")
    Integer findFinalgradeByStudentidAndClassid(String studentid,String classid);

    @Query(name = "findPassByStudentidAndClassid",nativeQuery = true,value =
            "select pass from class_grade where studentid=:studentid and classid=:classid")
    Integer findPassByStudentidAndClassid(String studentid,String classid);
}
