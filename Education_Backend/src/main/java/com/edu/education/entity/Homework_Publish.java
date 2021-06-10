package com.edu.education.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "homework_publish")
public class Homework_Publish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   //自增

    private String classid;
    private Integer homeworknumber;
    private String homeworkname;
    private String description;

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date starttime; //作业发布时间

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ddl;       //作业截止时间


    private String tx_hash;
    private String block_hash;
    private BigInteger block_number;

    public Homework_Publish(String classid,Integer homeworknumber,String homeworkname, String description, Date ddl){
        this.classid = classid;
        this.homeworkname = homeworkname;
        this.homeworknumber = homeworknumber;
        this.description = description;
        this.ddl = ddl;
    }

    public Homework_Publish(){
        super();
    }

    @Override
    public String toString() {
        return "Homework_Publish{" +
                "id=" + id +
                ", classid='" + classid + '\'' +
                ", homeworknumber=" + homeworknumber +
                ", homeworkname='" + homeworkname + '\'' +
                ", description='" + description + '\'' +
                ", starttime=" + starttime +
                ", ddl=" + ddl +
                ", tx_hash='" + tx_hash + '\'' +
                ", block_hash='" + block_hash + '\'' +
                ", block_number=" + block_number +
                '}';
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

    public Date getStarttime() {
        return starttime;
    }

    public String getDescription() {
        return description;
    }

    public Date getDdl() {
        return ddl;
    }

    public Integer getHomeworknumber() {
        return homeworknumber;
    }

    public String getHomeworkname() {
        return homeworkname;
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

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDdl(Date ddl) {
        this.ddl = ddl;
    }

    public void setHomeworkname(String homeworkname) {
        this.homeworkname = homeworkname;
    }

    public void setHomeworknumber(Integer homeworknumber) {
        this.homeworknumber = homeworknumber;
    }
}
