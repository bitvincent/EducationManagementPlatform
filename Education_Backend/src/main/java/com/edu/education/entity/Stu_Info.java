package com.edu.education.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "stu_info")
public class Stu_Info {

    @Id
    private String studentid;   //public key
    private String privkey;

    @NotBlank(message = "手机号为空")
    private String number;
    @NotBlank(message = "密码为空")
    private String password;

    @NotBlank(message = "姓名为空")
    private String studentname;
    @Min(value = 0,message = "gender超出范围") @Max(value = 1,message = "gender超出范围")
    private Integer gender;
    @NotBlank(message = "身份证为空")
    private String idcard;
    @NotBlank(message = "学校名为空")
    private String schoolspecial;
    @NotBlank(message = "学院名为空")
    private String institute;
    @NotBlank(message = "专业为空")
    private String major;
    @NotBlank(message = "学号为空")
    private String sno;

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "入学时间为空")
    private Date intime;

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "毕业时间为空")
    private Date outtime;

    @NotNull
    private Integer degree;
    //头像url
    private String studentlogo;

    private String tx_hash;
    private String block_hash;
    private BigInteger block_number;

    public Stu_Info(String number, String password, String studentname,
                    Integer gender, String idcard, String schoolspecial,
                    String institute, String major, String sno,Integer degree,
                    @DateTimeFormat(pattern = "yyyy-MM-dd")Date intime,
                    @DateTimeFormat(pattern = "yyyy-MM-dd")Date outtime){
        this.number = number;
        this.password = password;
        this.studentname = studentname;
        this.gender = gender;
        this.idcard = idcard;
        this.schoolspecial = schoolspecial;
        this.institute = institute;
        this.major = major;
        this.sno = sno;
        this.degree = degree;
        this.intime = intime;
        this.outtime = outtime;
    }
    public Stu_Info() {
        super();
    }

    @Override
    public String toString() {
        return "Stu_Info{" +
                "studentid='" + studentid + '\'' +
                ", privkey='" + privkey + '\'' +
                ", number='" + number + '\'' +
                ", password='" + password + '\'' +
                ", studentname='" + studentname + '\'' +
                ", gender=" + gender +
                ", idcard='" + idcard + '\'' +
                ", schoolspecial='" + schoolspecial + '\'' +
                ", institute='" + institute + '\'' +
                ", major='" + major + '\'' +
                ", sno='" + sno + '\'' +
                ", intime=" + intime +
                ", outtime=" + outtime +
                ", degree=" + degree +
                ", studentlogo='" + studentlogo + '\'' +
                ", tx_hash='" + tx_hash + '\'' +
                ", block_hash='" + block_hash + '\'' +
                ", block_number=" + block_number +
                '}';
    }

    public String getBlock_hash() {
        return block_hash;
    }

    public String getTx_hash() {
        return tx_hash;
    }

    public Date getIntime() {
        return intime;
    }

    public Date getOuttime() {
        return outtime;
    }

    public Integer getDegree() {
        return degree;
    }

    public BigInteger getBlock_number() {
        return block_number;
    }

    public Integer getGender() {
        return gender;
    }

    public String getIdcard() {
        return idcard;
    }

    public String getInstitute() {
        return institute;
    }

    public String getMajor() {
        return major;
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }

    public String getPrivkey() {
        return privkey;
    }

    public String getSchoolspecial() {
        return schoolspecial;
    }

    public String getSno() {
        return sno;
    }

    public String getStudentid() {
        return studentid;
    }

    public String getStudentlogo() {
        return studentlogo;
    }

    public String getStudentname() {
        return studentname;
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

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setOuttime(Date outtime) {
        this.outtime = outtime;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPrivkey(String privkey) {
        this.privkey = privkey;
    }

    public void setSchoolspecial(String schoolspecial) {
        this.schoolspecial = schoolspecial;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public void setStudentlogo(String studentlogo) {
        this.studentlogo = studentlogo;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }
}


