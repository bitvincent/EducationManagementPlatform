package com.edu.education.entity;

import org.fisco.bcos.web3j.abi.datatypes.Int;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "homework_submit")
public class Homework_Submit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   //自增

    private String studentid;
    private String classid;
    private Integer homeworknumber;
    private String content; //ipfs地址
    private String docname;
    private Integer flag;   //是否批改 0未批改，1已批改

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadtime;    //作业上传时间

    private String tx_hash;
    private String block_hash;
    private BigInteger block_number;

    public Homework_Submit(){
        super();
    }

    @Override
    public String toString() {
        return "Homework_Submit{" +
                "id=" + id +
                ", studentid='" + studentid + '\'' +
                ", classid=" + classid +
                ", homeworknumber=" + homeworknumber +
                ", content='" + content + '\'' +
                ", docname='" + docname + '\'' +
                ", flag=" + flag +
                ", uploadtime=" + uploadtime +
                ", tx_hash='" + tx_hash + '\'' +
                ", block_hash='" + block_hash + '\'' +
                ", block_number=" + block_number +
                '}';
    }

    public Integer getHomeworknumber() {
        return homeworknumber;
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

    public String getContent() {
        return content;
    }

    public Integer getFlag() {
        return flag;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public String getDocname() {
        return docname;
    }

    public void setHomeworknumber(Integer homeworknumber) {
        this.homeworknumber = homeworknumber;
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }
}
