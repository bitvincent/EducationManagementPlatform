package com.edu.education.entity;

import org.fisco.bcos.web3j.abi.datatypes.Int;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "homework_grade")
public class Homework_Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   //自增

    private String studentid;
    private String classid;
    private Integer homeworknumber;
    private String content; //ipfs地址
    private String docname;

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date marktime;  //打分时间
    private Integer grade;

    private String tx_hash;
    private String block_hash;
    private BigInteger block_number;

    public Homework_Grade(){
        super();
    }

    public String getDocname() {
        return docname;
    }

    public String getContent() {
        return content;
    }

    public String getStudentid() {
        return studentid;
    }

    public Integer getId() {
        return id;
    }

    public String getBlock_hash() {
        return block_hash;
    }

    public String getTx_hash() {
        return tx_hash;
    }

    public BigInteger getBlock_number() {
        return block_number;
    }

    public String getClassid() {
        return classid;
    }

    public Integer getHomeworknumber() {
        return homeworknumber;
    }

    public Date getMarktime() {
        return marktime;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBlock_hash(String block_hash) {
        this.block_hash = block_hash;
    }

    public void setBlock_number(BigInteger block_number) {
        this.block_number = block_number;
    }

    public void setTx_hash(String tx_hash) {
        this.tx_hash = tx_hash;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public void setHomeworknumber(Integer homeworknumber) {
        this.homeworknumber = homeworknumber;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public void setMarktime(Date marktime) {
        this.marktime = marktime;
    }
}
