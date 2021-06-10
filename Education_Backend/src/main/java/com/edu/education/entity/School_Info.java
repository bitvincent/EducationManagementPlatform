package com.edu.education.entity;


import javax.persistence.*;

@Entity
@Table(name = "school_info")
public class School_Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer schoolid;

    private String schoolname;
    private String schoollogo;

    public School_Info(){
        super();
    }

    @Override
    public String toString() {
        return "School_Info{" +
                "schoolid=" + schoolid +
                ", schoolname='" + schoolname + '\'' +
                ", schoollogo='" + schoollogo + '\'' +
                '}';
    }

    public Integer getSchoolid() {
        return schoolid;
    }

    public String getSchoollogo() {
        return schoollogo;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolid(Integer schoolid) {
        this.schoolid = schoolid;
    }

    public void setSchoollogo(String schoollogo) {
        this.schoollogo = schoollogo;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }
}
