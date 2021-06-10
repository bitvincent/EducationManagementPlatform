package com.edu.education.dao;

import com.edu.education.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateDao extends JpaRepository<Certificate,Integer> {

    List<Certificate> findAllByStudentid(String studentid);

    Certificate findByStudentidAndClassid(String studentid, String classid);

}
