package com.edu.education.dao;

import com.edu.education.entity.Class_Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Class_InfoDao extends JpaRepository<Class_Info,Integer> {
    Class_Info findByClassid(String classid);

    @Query(name = "findClassnameByClassid",nativeQuery = true,value =
            "select classname from class_info where classid=:classid")
    String findClassnameByClassid(String classid);

    @Query(name = "findTeacheridByClassid",nativeQuery = true,value =
            "select teacherid from class_info where classid=:classid")
    String findTeacheridByClassid(String classid);

    @Query(name = "searchClass",nativeQuery = true,value =
            "select * from class_info as c where (c.classname like CONCAT('%',:classname,'%') or :classname is null) and " +
                    "(c.category like :category or :category is null) and (c.schoolid like :schoolid or :schoolid is null) order by number desc")
    List<Class_Info> findByClassnameAndCategoryAndSchoolid(String classname, Integer category, Integer schoolid);

    List<Class_Info> findByTeacherid(String teacherid);

    @Query(name = "getRecommendClass",nativeQuery = true,value =
            "select * from class_info order by number desc limit :neednum")
    List<Class_Info> getRecommendClass(@Param("neednum") Integer neednum);


}
