package com.edu.education.service.impl;

import cn.hutool.json.JSONObject;
import com.edu.education.common.ResponseEnum;
import com.edu.education.common.ServerResponse;
import com.edu.education.common.TxReceipt;
import com.edu.education.dao.School_InfoDao;
import com.edu.education.dao.Stu_InfoDao;
import com.edu.education.dao.Teacher_InfoDao;
import com.edu.education.entity.Stu_Info;
import com.edu.education.entity.Teacher_Info;
import com.edu.education.helper.common.JwtTokenHelper;
import com.edu.education.helper.fileHelper.PictureHelper;
import com.edu.education.helper.contractHelper.StuInfoHelper;
import com.edu.education.helper.contractHelper.TeacherInfoHelper;
import com.edu.education.service.UserService;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private Stu_InfoDao stu_infoDao;
    @Autowired
    private StuInfoHelper stuInfoHelper;

    @Autowired
    private Teacher_InfoDao teacher_infoDao;
    @Autowired
    private TeacherInfoHelper teacherInfoHelper;

    @Autowired
    private School_InfoDao school_infoDao;

    @Resource
    private JwtTokenHelper jwtTokenHelper ;
    @Resource
    private PictureHelper pictureHelper;

    @Override
    public String checkUserType(String username) {
        boolean isTeacher = teacher_infoDao.existsByNumber(username);
        boolean isStudent = stu_infoDao.existsByNumber(username);

        if(!isTeacher && !isStudent)
            return USER_NOT_EXIST;
        else if (isTeacher && isStudent)
            return USER_BOTH_FAULT;
        else if (isTeacher)
            return TEACHER_TYPE;
        else if (isStudent)
            return STUDENT_TYPE;
        return USER_SUCCESS;
    }

    @Override
    public ServerResponse handleUserTypeFault(String type) {
        if(type.equals(USER_NOT_EXIST))
            return ServerResponse.getInstance().responseEnum(ResponseEnum.ACCOUNT_NOT_FOUND).failed();
        else if(type.equals(USER_BOTH_FAULT))
            return ServerResponse.getInstance().responseEnum(ResponseEnum.ACCOUT_BOTH).failed();

        return ServerResponse.getInstance().responseEnum(ResponseEnum.ACCOUT_TYPE_WRONG).failed();
    }

    @Override
    public ServerResponse loginTeacher(String username, String password) {
        Teacher_Info teacher_info = teacher_infoDao.findByNumber(username);
        //用户不存在
        if(teacher_info==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.ACCOUNT_NOT_FOUND);
        //密码错误
        else if(!teacher_info.getPassword().equals(password))
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.LOGIN_FAILED);
        else
        {
            String token = jwtTokenHelper.createToken(username);
            if (!StringUtils.isEmpty(token)){
                JSONObject json = new JSONObject().put("token",token);
                json.put("teacherid",teacher_info.getTeacherid());
                return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.LOGIN_SUCCESS).data(json);
            }
        }
        //内部错误
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INTERNAL_ERROR);
    }

    @Override
    public ServerResponse loginTeacherWithSMS(String username) {
        Teacher_Info teacher_info = teacher_infoDao.findByNumber(username);
        //用户不存在
        if(teacher_info==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.ACCOUNT_NOT_FOUND);
        else
        {
            String token = jwtTokenHelper.createToken(username);
            if (!StringUtils.isEmpty(token)){
                JSONObject json = new JSONObject().put("token",token);
                json.put("teacherid",teacher_info.getTeacherid());
                return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.LOGIN_SUCCESS).data(json);
            }
        }
        //内部错误
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INTERNAL_ERROR);
    }

    @Override
    public ServerResponse loginStudent(String username, String password) {
        Stu_Info stu_info = stu_infoDao.findByNumber(username);
        //用户不存在
        if(stu_info==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.ACCOUNT_NOT_FOUND);
        //密码错误
        else if(!stu_info.getPassword().equals(password))
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.LOGIN_FAILED);
        else
        {
            String token = jwtTokenHelper.createToken(username);
            if (!StringUtils.isEmpty(token)){
                JSONObject json = new JSONObject().put("token",token);
                json.put("studentid",stu_info.getStudentid());
                return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.LOGIN_SUCCESS).data(json);
            }
        }
        //内部错误
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INTERNAL_ERROR);
    }

    @Override
    public ServerResponse loginStudentWithSMS(String username) {
        Stu_Info stu_info = stu_infoDao.findByNumber(username);
        //用户不存在
        if(stu_info==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.ACCOUNT_NOT_FOUND);
            //密码错误
        else
        {
            String token = jwtTokenHelper.createToken(username);
            if (!StringUtils.isEmpty(token)){
                JSONObject json = new JSONObject().put("token",token);
                json.put("studentid",stu_info.getStudentid());
                return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.LOGIN_SUCCESS).data(json);
            }
        }
        //内部错误
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INTERNAL_ERROR);
    }

    @Override
    public ServerResponse registerStudent(Stu_Info stu_info) {
        //用户名已存在
        if( null != stu_infoDao.findByNumber(stu_info.getNumber()) || null != teacher_infoDao.findByNumber(stu_info.getNumber())  )
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.USERNAME_EXIST);

        //设置学生头像 1男2女
        if(stu_info.getGender()==0)
            stu_info.setStudentlogo(pictureHelper.getDeafultLogo(PictureHelper.STUDENT_BOY));
        else
            stu_info.setStudentlogo(pictureHelper.getDeafultLogo(PictureHelper.STUDENT_GIRL));

        ECKeyPair keyPair = GenCredential.createKeyPair();
        stu_info.setPrivkey(keyPair.getPrivateKey().toString());
        stu_info.setStudentid(keyPair.getPublicKey().toString());

        TxReceipt receipt = stuInfoHelper.insert(stu_info);
        //上链失败
        if(receipt==null || 0 == Integer.parseInt(receipt.getOutput().substring(2, receipt.getOutput().length()), 16)){
            System.out.println("StuInfo Insert Failed,Tx_Hash:"+receipt.getTransactionHash());
            System.out.println("                   Output :"+receipt.getOutput());
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_IN_BLOCKCHAIN_FAILED);
        }

        stu_info.setBlock_hash(receipt.getBlockHash());
        stu_info.setBlock_number(receipt.getBlockNumber());
        stu_info.setTx_hash(receipt.getTransactionHash());

        Stu_Info tmp = stu_infoDao.save(stu_info);
        if(tmp!=null)
            return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.REGISTER_SUCCESS).data(tmp);
        else
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.REGISTER_FAILED).data(tmp);
    }

    @Override
    public ServerResponse registerTeacher(Teacher_Info teacher_info) {
        //用户名已存在
        if( null != teacher_infoDao.findByNumber(teacher_info.getNumber()) ||  null != stu_infoDao.findByNumber(teacher_info.getNumber()))
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.USERNAME_EXIST);

        //设置老师头像 1男2女
        if(teacher_info.getGender()==0)
            teacher_info.setTeacherlogo(pictureHelper.getDeafultLogo(PictureHelper.TEACHER_MAN));
        else
            teacher_info.setTeacherlogo(pictureHelper.getDeafultLogo(PictureHelper.TEACHER_WOMAN));

        ECKeyPair keyPair = GenCredential.createKeyPair();
        teacher_info.setPrivkey(keyPair.getPrivateKey().toString());
        teacher_info.setTeacherid(keyPair.getPublicKey().toString());


        TxReceipt receipt = teacherInfoHelper.insert(teacher_info);
        //上链失败
        if(receipt==null || 0 == Integer.parseInt(receipt.getOutput().substring(2, receipt.getOutput().length()), 16)){
            System.out.println("TeacherInfo Insert Failed,Tx_Hash:"+receipt.getTransactionHash());
            System.out.println("                   Output :"+receipt.getOutput());
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_IN_BLOCKCHAIN_FAILED);
        }

        teacher_info.setBlock_hash(receipt.getBlockHash());
        teacher_info.setBlock_number(receipt.getBlockNumber());
        teacher_info.setTx_hash(receipt.getTransactionHash());

        Teacher_Info result = teacher_infoDao.save(teacher_info);
        if(result!=null)
            return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.REGISTER_SUCCESS).data(result);
        else
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.REGISTER_FAILED).data(result);
    }

    @Override
    public ServerResponse updateStudentPassword(String username, String oldpassword, String newpassword) {
        Stu_Info stu_info =  stu_infoDao.findByNumber(username);
        if(stu_info == null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.ACCOUNT_NOT_FOUND);
        //密码错误
        if(!stu_info.getPassword().equals(oldpassword))
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.UPDATE_PASSWORD_FAILED);

        stu_info.setPassword(newpassword);
        Stu_Info result = stu_infoDao.save(stu_info);
        if(result!=null)
            return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SAVE_SUCCESS);
        else
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_FAILED);
    }

    @Override
    public ServerResponse updateTeacherPassword(String username, String oldpassword, String newpassword) {
        Teacher_Info teacher_info =  teacher_infoDao.findByNumber(username);
        if(teacher_info == null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.ACCOUNT_NOT_FOUND);
        //密码错误
        if(!teacher_info.getPassword().equals(oldpassword))
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.UPDATE_PASSWORD_FAILED);

        teacher_info.setPassword(newpassword);
        Teacher_Info result = teacher_infoDao.save(teacher_info);
        if(result!=null)
            return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SAVE_SUCCESS);
        else
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_FAILED);
    }
}
