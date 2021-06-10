package com.edu.education.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "contract")
public class ContractProperties {
    public Bsn bsn = new Bsn();
    public Local local = new Local();

    public Local getLocal() {
        return local;
    }

    public Bsn getBsn() {
        return bsn;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public void setBsn(Bsn bsn) {
        this.bsn = bsn;
    }

    public class Bsn{
        public String using;
        public String appCode;
        public String userCode;
        public String api;
        public Resource prk;
        public Resource puk;
        public String mspDir;
        public String userid;
        public String Certificate;
        public String CertificateAppCode;
        public String ClassInfo;
        public String ClassInfoAppCode;
        public String ClassGrade;
        public String ClassGradeAppCode;
        public String HomeworkGrade;
        public String HomeworkGradeAppCode;
        public String HomeworkSubmit;
        public String HomeworkSubmitAppCode;
        public String HomeworkPublish;
        public String HomeworkPublishAppCode;
        public String StuInfo;
        public String StuInfoAppCode;
        public String TeacherInfo;
        public String TeacherInfoAppCode;


        public String getUsing() {
            return using;
        }

        public void setUsing(String onGoing) {
            this.using = onGoing;
        }

        public void setCertificate(String certificate) {
            Certificate = certificate;
        }

        public void setClassGrade(String classGrade) {
            ClassGrade = classGrade;
        }

        public void setClassInfo(String classInfo) {
            ClassInfo = classInfo;
        }

        public void setHomeworkGrade(String homeworkGrade) {
            HomeworkGrade = homeworkGrade;
        }

        public void setHomeworkPublish(String homeworkPublish) {
            HomeworkPublish = homeworkPublish;
        }

        public void setHomeworkSubmit(String homeworkSubmit) {
            HomeworkSubmit = homeworkSubmit;
        }

        public void setStuInfo(String stuInfo) {
            StuInfo = stuInfo;
        }

        public void setTeacherInfo(String teacherInfo) {
            TeacherInfo = teacherInfo;
        }

        public String getCertificate() {
            return Certificate;
        }

        public String getClassGrade() {
            return ClassGrade;
        }

        public String getClassInfo() {
            return ClassInfo;
        }

        public String getHomeworkGrade() {
            return HomeworkGrade;
        }

        public String getHomeworkPublish() {
            return HomeworkPublish;
        }

        public String getHomeworkSubmit() {
            return HomeworkSubmit;
        }

        public String getStuInfo() {
            return StuInfo;
        }

        public String getTeacherInfo() {
            return TeacherInfo;
        }

        public String getUserid() {
            return userid;
        }

        public Resource getPuk() {
            return puk;
        }

        public Resource getPrk() {
            return prk;
        }

        public String getMspDir() {
            return mspDir;
        }

        public String getAppCode() {
            return appCode;
        }

        public String getApi() {
            return api;
        }

        public String getUserCode() {
            return userCode;
        }

        public String getCertificateAppCode() {
            return CertificateAppCode;
        }

        public String getClassGradeAppCode() {
            return ClassGradeAppCode;
        }

        public String getClassInfoAppCode() {
            return ClassInfoAppCode;
        }

        public String getHomeworkGradeAppCode() {
            return HomeworkGradeAppCode;
        }

        public String getHomeworkPublishAppCode() {
            return HomeworkPublishAppCode;
        }

        public String getHomeworkSubmitAppCode() {
            return HomeworkSubmitAppCode;
        }

        public String getStuInfoAppCode() {
            return StuInfoAppCode;
        }

        public String getTeacherInfoAppCode() {
            return TeacherInfoAppCode;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public void setPuk(Resource puk) {
            this.puk = puk;
        }

        public void setPrk(Resource prk) {
            this.prk = prk;
        }

        public void setMspDir(String mspDir) {
            this.mspDir = mspDir;
        }

        public void setAppCode(String appCode) {
            this.appCode = appCode;
        }

        public void setApi(String api) {
            this.api = api;
        }

        public void setCertificateAppCode(String certificateAppCode) {
            CertificateAppCode = certificateAppCode;
        }

        public void setClassGradeAppCode(String classGradeAppCode) {
            ClassGradeAppCode = classGradeAppCode;
        }

        public void setClassInfoAppCode(String classInfoAppCode) {
            ClassInfoAppCode = classInfoAppCode;
        }

        public void setHomeworkGradeAppCode(String homeworkGradeAppCode) {
            HomeworkGradeAppCode = homeworkGradeAppCode;
        }

        public void setHomeworkPublishAppCode(String homeworkPublishAppCode) {
            HomeworkPublishAppCode = homeworkPublishAppCode;
        }

        public void setHomeworkSubmitAppCode(String homeworkSubmitAppCode) {
            HomeworkSubmitAppCode = homeworkSubmitAppCode;
        }

        public void setStuInfoAppCode(String stuInfoAppCode) {
            StuInfoAppCode = stuInfoAppCode;
        }

        public void setTeacherInfoAppCode(String teacherInfoAppCode) {
            TeacherInfoAppCode = teacherInfoAppCode;
        }
    }

    public class Local{
        public String tortAddress;

        public String getTortAddress() {
            return tortAddress;
        }

        public void setTortAddress(String tortAddress) {
            this.tortAddress = tortAddress;
        }
    }
}
