package com.edu.education.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "class_info")
public class Class_Info {

    @Id
    private String classid;

    private String classname;
    private Integer category;
    private String description;
    private String teacherid;
    private Integer schoolid;

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date starttime; //课程开始时间

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endtime;   //课程结束时间

    private Integer learnhours;
    private String classpic;    //课程图片url

    private Integer number;

    private String tx_hash;
    private String block_hash;
    private BigInteger block_number;

    public Class_Info(){
        super();
    }

    @Override
    public String toString() {
        return "Class_Info{" +
                "classid=" + classid +
                ", classname='" + classname + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", teacherid='" + teacherid + '\'' +
                ", schoolid=" + schoolid +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", learnhours=" + learnhours +
                ", classpic='" + classpic + '\'' +
                ", number=" + number +
                ", tx_hash='" + tx_hash + '\'' +
                ", block_hash='" + block_hash + '\'' +
                ", block_number=" + block_number +
                '}';
    }

    public String getTeacherid() {
        return teacherid;
    }

    public Integer getSchoolid() {
        return schoolid;
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

    public String getDescription() {
        return description;
    }

    public Date getEndtime() {
        return endtime;
    }

    public Date getStarttime() {
        return starttime;
    }

    public Integer getCategory() {
        return category;
    }

    public String getClassid() {
        return classid;
    }

    public Integer getLearnhours() {
        return learnhours;
    }

    public Integer getNumber() {
        return number;
    }

    public String getClassname() {
        return classname;
    }

    public String getClasspic() {
        return classpic;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public void setSchoolid(Integer schoolid) {
        this.schoolid = schoolid;
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

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setClasspic(String classpic) {
        this.classpic = classpic;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public void setLearnhours(Integer learnhours) {
        this.learnhours = learnhours;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }
}
