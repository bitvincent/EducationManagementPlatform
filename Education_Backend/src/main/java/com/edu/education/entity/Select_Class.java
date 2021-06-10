package com.edu.education.entity;

import org.fisco.bcos.web3j.abi.datatypes.Int;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "select_class")
public class Select_Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   //自增

    private String studentid;
    private String classid;

    public Select_Class(){
        super();
    }

    public String getClassid() {
        return classid;
    }

    public String getStudentid() {
        return studentid;
    }

    public Integer getId() {
        return id;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
