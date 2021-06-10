package com.edu.education.entity;

import io.swagger.models.auth.In;
import org.fisco.bcos.web3j.abi.datatypes.Int;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "class_grade")
public class Class_Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   //自增

    private String studentid;
    private String classid;
    private Integer pass;   //是否通过
    private Integer finalgrade;

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date passtime;  //课程打分时间

    private String tx_hash;
    private String block_hash;
    private BigInteger block_number;

    public Class_Grade(){
        super();
    }

    @Override
    public String toString() {
        return "Class_Grade{" +
                "id=" + id +
                ", studentid='" + studentid + '\'' +
                ", classid=" + classid +
                ", pass=" + pass +
                ", finalgrade=" + finalgrade +
                ", passtime=" + passtime +
                ", tx_hash='" + tx_hash + '\'' +
                ", block_hash='" + block_hash + '\'' +
                ", block_number=" + block_number +
                '}';
    }

    public String getClassid() {
        return classid;
    }

    public BigInteger getBlock_number() {
        return block_number;
    }

    public String getTx_hash() {
        return tx_hash;
    }

    public String getBlock_hash() {
        return block_hash;
    }

    public Integer getId() {
        return id;
    }

    public String getStudentid() {
        return studentid;
    }

    public Date getPasstime() {
        return passtime;
    }

    public Integer getPass() {
        return pass;
    }

    public Integer getFinalgrade() {
        return finalgrade;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public void setTx_hash(String tx_hash) {
        this.tx_hash = tx_hash;
    }

    public void setBlock_number(BigInteger block_number) {
        this.block_number = block_number;
    }

    public void setBlock_hash(String block_hash) {
        this.block_hash = block_hash;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public void setPasstime(Date passtime) {
        this.passtime = passtime;
    }

    public void setFinalgrade(Integer finalgrade) {
        this.finalgrade = finalgrade;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }
}
