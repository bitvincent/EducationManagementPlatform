package com.edu.education.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@Table(name = "teacher_info")
public class Teacher_Info {

    @Id
    private String teacherid;   //公钥
    private String privkey;

    @NotBlank(message = "手机号为空")
    private String number;
    @NotBlank(message = "密码为空")
    private String password;

    @NotBlank(message = "姓名为空")
    private String teachername;
    @NotNull(message = "性别为空")
    private Integer gender;
    @NotBlank(message = "身份证为空")
    private String idcard;
    @NotNull(message = "学校为空")
    private Integer schoolid;
    @NotNull(message = "职称为空")
    private Integer title;
    private String teacherlogo; //头像url

    private String tx_hash;
    private String block_hash;
    private BigInteger block_number;

    public Teacher_Info(){
        super();
    }

    @Override
    public String toString() {
        return "Teacher_Info{" +
                "teacherid='" + teacherid + '\'' +
                ", privkey='" + privkey + '\'' +
                ", number='" + number + '\'' +
                ", password='" + password + '\'' +
                ", teachername='" + teachername + '\'' +
                ", gender=" + gender +
                ", idcard='" + idcard + '\'' +
                ", schoolid=" + schoolid +
                ", title=" + title +
                ", teacherlogo='" + teacherlogo + '\'' +
                ", tx_hash='" + tx_hash + '\'' +
                ", block_hash='" + block_hash + '\'' +
                ", block_number=" + block_number +
                '}';
    }

    public String getPrivkey() {
        return privkey;
    }

    public String getPassword() {
        return password;
    }

    public String getNumber() {
        return number;
    }

    public String getIdcard() {
        return idcard;
    }

    public Integer getGender() {
        return gender;
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

    public Integer getSchoolid() {
        return schoolid;
    }

    public Integer getTitle() {
        return title;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public String getTeacherlogo() {
        return teacherlogo;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setPrivkey(String privkey) {
        this.privkey = privkey;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public void setSchoolid(Integer schoolid) {
        this.schoolid = schoolid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public void setTeacherlogo(String teacherlogo) {
        this.teacherlogo = teacherlogo;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public void setTitle(Integer title) {
        this.title = title;
    }
}
