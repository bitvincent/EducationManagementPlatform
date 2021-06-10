package com.edu.education.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   //自增

    private String studentid;
    private String classid;
    private Integer finalgrade;

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date passtime;  //发证时间

    private String tx_hash;
    private String block_hash;
    private BigInteger block_number;

    public Certificate(){
        super();
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", studentid='" + studentid + '\'' +
                ", classid=" + classid +
                ", finalgrade=" + finalgrade +
                ", passtime=" + passtime +
                ", tx_hash='" + tx_hash + '\'' +
                ", block_hash='" + block_hash + '\'' +
                ", block_number=" + block_number +
                '}';
    }

    public Integer getFinalgrade() {
        return finalgrade;
    }

    public Date getPasstime() {
        return passtime;
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

    public void setFinalgrade(Integer finalgrade) {
        this.finalgrade = finalgrade;
    }

    public void setPasstime(Date passtime) {
        this.passtime = passtime;
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
}
