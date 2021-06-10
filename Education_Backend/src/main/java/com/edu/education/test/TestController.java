package com.edu.education.test;

import com.edu.education.common.DisableToken;
import com.edu.education.common.ServerResponse;
import com.edu.education.entity.Teacher_Info;
import com.edu.education.helper.fileHelper.IpfsFileHelper;
import com.edu.education.helper.common.JwtKaptchaHelper;
import com.edu.education.helper.fileHelper.PictureHelper;
import com.edu.education.service.SchoolInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
@Api(tags = "测试相关操作接口")
public class TestController {

    @Value(value = "${com.tempPath}")
    private String fileDir;

    @Value(value = "${com.ipfsPath}")
    private String ipfsPath;

    @Resource
    JwtKaptchaHelper jwtKaptchaHelper;

    @Resource
    PictureHelper pictureHelper;
    @Resource
    IpfsFileHelper ipfsFileHelper;
    @Autowired
    SchoolInfoService schoolInfoService;

    @DisableToken
    @PostMapping("/test")
    @ResponseBody
    public ServerResponse test (@RequestBody  Teacher_Info teacher_info){
        System.out.println("test:"+teacher_info.toString());
        return ServerResponse.getInstance().ok().data(teacher_info.toString());
    }

    @DisableToken
    @PostMapping("/test2")
    @ResponseBody
    public ServerResponse test2 (String username,Integer number){
        System.out.println("test2:username:"+username + ", nuumber:"+number);
        return ServerResponse.getInstance().ok();
    }

    @DisableToken
    @PostMapping("/uploadIpfs")
    @ResponseBody
    public ServerResponse uploadIpfs (MultipartFile file){
        String content = null;
        try {
            content = ipfsFileHelper.upload(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ServerResponse.getInstance().ok().data(content);
    }

    @DisableToken
    @PostMapping("/uploadPic")
    @ResponseBody
    public ServerResponse uploadPic (@RequestParam(value = "file") MultipartFile file){
        try {
            return ServerResponse.getInstance().ok().data(pictureHelper.savePicture(file,"student","Joe"));
        }catch (Exception e){
            return ServerResponse.getInstance().failed();
        }

    }

    @DisableToken
    @PostMapping("/uploadSchoolLogo")
    @ResponseBody
    public ServerResponse uploadSchoolLogo(@RequestParam(value = "file") MultipartFile file,String schoolname){
        return schoolInfoService.uploadSchoolLogo(file,schoolname);
    }

    @DisableToken
    @PostMapping("/initSchoolLogo")
    @ResponseBody
    public ServerResponse initSchoolLogo(){
        return schoolInfoService.saveDefaultSchoolLogo();
    }



}
