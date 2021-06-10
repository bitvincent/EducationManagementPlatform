package com.edu.education.dao;

import com.edu.education.entity.School_Info;
import org.fisco.bcos.web3j.abi.datatypes.Int;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface School_InfoDao extends JpaRepository<School_Info,Integer> {
    School_Info findBySchoolid(Integer schoolid);

    @Query(name = "findMaxSchoolid",nativeQuery = true,value =
            "select max(schoolid) from school_info")
    Integer findMaxSchoolid();
}
