package com.edu.education.helper.fileHelper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.UUID;

/**
 * PictureHelper
 * 上传图片的相关操作
 */

@Component
public class PictureHelper {

    public static String STUDENT_BOY = "STUDENT_BOY";
    public static String STUDENT_GIRL = "STUDENT_GIRL";
    public static String TEACHER_MAN = "TEACHER_MAN";
    public static String TEACHER_WOMAN = "TEACHER_WOMAN";

    @Value(value = "${com.visit_path}")
    private String visit_path;
    @Value(value = "${com.ip}")
    private String ip_port;

    //@Value(value = "${com.storage_path}")
    private String storage_path = System.getProperty("user.dir")+"/static";

    /**
     * @apiNote 存储的目录结构举例：/education/picture/student/tom/tomAvatar.jpg
     * @param multipartFile 照片的内容
     * @param identity 图片属于：学生(student)、老师(teacher)、课程(course)
     * @param idName 暂以学生、老师、课程的name作为识别
     * @return 返回可以直接访问的url
     */
    public String savePicture(MultipartFile multipartFile, String identity, String idName) {
        System.out.println("File:"+multipartFile);
        if(multipartFile.isEmpty()){
            return "";
        }
        // 获取图片名
        String fileName = multipartFile.getOriginalFilename();
        // 获取后缀，变成小写
        String suffix = multipartFile.getOriginalFilename().substring(fileName.lastIndexOf(".")).toLowerCase();
        // 用uuid作为文件名
        String savedName = UUID.randomUUID().toString() + suffix;
        // 存到服务器上
        String picturePath = saveToServer(savedName, identity, idName, multipartFile);
        return picturePath;
    }

    private String saveToServer(String savedName, String identity, String idName, MultipartFile multipartFile) {
        // 指定path的位置
        String path = storage_path + "/" + identity + "/" + idName;
        File targetFile = new File(path, savedName);
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
        try {
            // 存到指定位置
            multipartFile.transferTo(targetFile);
            return ip_port + visit_path + "/" + identity + "/" + idName + "/" + savedName;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private boolean saveToDateBase(String pictureName, String url) {
        return false;
    }

    public String getDeafultLogo(String type){
        if(type.equals(STUDENT_BOY))
            return ip_port + visit_path + "/student/student_boy1.jpg";
        else if(type.equals(STUDENT_GIRL))
            return ip_port + visit_path + "/student/student_girl1.jpg";
        else if(type.equals(TEACHER_MAN))
            return ip_port + visit_path + "/teacher/teacher_man.png";
        else if(type.equals(TEACHER_WOMAN))
            return ip_port + visit_path + "/teacher/teacher_woman.png";
        return null;
    }

    public String saveSchoolLogo(MultipartFile multipartFile, Integer schoolid){

        // 获取图片名
        String fileName = multipartFile.getOriginalFilename();
        // 获取后缀，变成小写
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        //生成文件名
        String savedName = schoolid.toString() + suffix;

        // 指定path的位置
        String path = storage_path + "/school";
        File targetFile = new File(path, savedName);
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
        try {
            // 存到指定位置
            multipartFile.transferTo(targetFile);
            return ip_port + visit_path + "/school/" + savedName;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }



}
