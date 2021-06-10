package com.edu.education.dao;

import com.edu.education.entity.Homework_Grade;
import org.fisco.bcos.web3j.abi.datatypes.Int;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Homework_GradeDao extends JpaRepository<Homework_Grade,Integer> {
    List<Homework_Grade> findByClassid(String classid);

    List<Homework_Grade> findByStudentidAndClassid(String studentid, String classid);

    Homework_Grade findByStudentidAndClassidAndHomeworknumber(String studentid, String classid, Integer homeworknumber);

    Homework_Grade findByContent(String content);
}
