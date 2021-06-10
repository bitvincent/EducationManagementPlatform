package com.edu.education.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edu.education.common.ResponseEnum;
import com.edu.education.common.ServerResponse;
import com.edu.education.common.TxReceipt;
import com.edu.education.dao.*;
import com.edu.education.entity.*;
import com.edu.education.entity.Stu_Info;
import com.edu.education.helper.fileHelper.IpfsFileHelper;
import com.edu.education.helper.contractHelper.HomeworkSubmitHelper;
import com.edu.education.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private Stu_InfoDao stu_infoDao;

    @Autowired
    private CertificateDao certificateDao;

    @Autowired
    private Select_ClassDao select_classDao;

    @Autowired
    private Class_InfoDao class_infoDao;

    @Autowired
    private Teacher_InfoDao teacher_infoDao;

    @Autowired
    private Homework_PublishDao homework_publishDao;

    @Autowired
    private Homework_SubmitDao homework_submitDao;
    @Autowired
    private HomeworkSubmitHelper homeworkSubmitHelper;

    @Autowired
    private Homework_GradeDao homework_gradeDao;

    @Autowired
    private Class_GradeDao class_gradeDao;

    @Autowired
    private IpfsFileHelper ipfsFileHelper;

    @Value("${com.blockchainid}")
    private String blockchainid;

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     *   List<
     *     classid(课程id)
     *     classstatus(通过状态 int 学习中 0 ； 已通过 1 ；未通过 2)
     *     classname（课程名）
     *     passtime（发证时间，即打分时间，YYYY-MM-DD）
     *   >
     *   **/
    @Override
    public ServerResponse getAllCertification(String username) {
        String studentid = stu_infoDao.findByNumber(username).getStudentid();

        // 拿到学生所有证书
        List<Certificate> certificates = certificateDao.findAllByStudentid(studentid);
        Map<String, Certificate> certificatesMap = new HashMap<>();
        for (Certificate c : certificates) {
            certificatesMap.put(c.getClassid(), c);
        }
        // 拿到学生所有选课
        List<Select_Class> allSelectClasses = select_classDao.findAllByStudentid(studentid);
        // 拿到学生所有成绩
        List<Class_Grade> allGradeClasses = class_gradeDao.findAllByStudentid(studentid);
        Map<String, Class_Grade> gradeMap = new HashMap<>();
        for (Class_Grade g : allGradeClasses) {
            gradeMap.put(g.getClassid(), g);
        }

        JSONArray resultJson = new JSONArray();
        for(Select_Class select_class : allSelectClasses){
            JSONObject temp = new JSONObject();

            String classid = select_class.getClassid();
            temp.put("classid", classid);

            Certificate certificate = certificatesMap.get(classid);
            Class_Grade class_grade = gradeMap.get(classid);
            if (certificate != null) {
                temp.put("classstatus", 1);
            } else if (class_grade != null) {
                temp.put("classstatus", 2);
            } else {
                temp.put("classstatus", 0);
            }

            temp.put("classname", class_infoDao.findByClassid(classid).getClassname());
            String passTime = "";
            if (certificate != null) {
                passTime = dateFormatter.format(certificate.getPasstime());
            }
            temp.put("passtime", passTime);

            resultJson.add(temp);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultJson);
    }

    /**
     *   studentname(学生名)
     *   schoolspecial（学生学校 String）
     *   institute(院系)
     *   sno(学号）
     *   classname（课程名）
     *   teachername（教师名）
     *   finalgrade（课程成绩）
     *   blockchainid(区块链ID String)
     *   block_number（成绩块号）
     *   tx_hash（交易哈希）
     *   tx_url（二维码url）
     *   passtime（发证时间 YYYY:MM:DD）
     *   **/
    @Value(value = "${com.tx_url_prefix}")
    private String tx_url_prefix;

    @Override
    public ServerResponse getCertification(String username, String classid) {
        Stu_Info stu_info = stu_infoDao.findByNumber(username);
        Certificate certificate = certificateDao.findByStudentidAndClassid(stu_info.getStudentid(), classid);
        if(certificate==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_EXIST);
        // 从Class_Info中classname和teacherid
        Class_Info class_info = class_infoDao.findByClassid(classid);
        // 从Teacher_Info拿到teachername
        String teachername = teacher_infoDao.findByTeacherid(class_info.getTeacherid()).getTeachername();

        JSONObject result = new JSONObject();
        result.put("studentname", stu_info.getStudentname());
        result.put("schoolspecial", stu_info.getSchoolspecial());
        result.put("institute", stu_info.getInstitute());
        result.put("sno", stu_info.getSno());
        result.put("classname", class_info.getClassname());
        result.put("teachername", teachername);
        result.put("finalgrade", certificate.getFinalgrade());
        result.put("blockchainid", blockchainid);
        result.put("block_number", certificate.getBlock_number());
        result.put("tx_hash", certificate.getTx_hash());
        // TODO tx_url
        result.put("tx_url", tx_url_prefix + certificate.getTx_hash());
        result.put("passtime", dateFormatter.format(certificate.getPasstime()));

        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(result);
    }

    @Override
    public ServerResponse getLearnProcess(String username, String classid) {
        Stu_Info student  = stu_infoDao.findByNumber(username);
        String studentid = student.getStudentid();
        List<Homework_Grade> allMarkedHomework = homework_gradeDao.findByStudentidAndClassid(studentid, classid);
        JSONArray resultJson = new JSONArray();
        for (Homework_Grade homework_grade : allMarkedHomework) {
            JSONObject temp = new JSONObject();

            // 拿到这次作业的publish和submit
            Integer homeworknumber = homework_grade.getHomeworknumber();
            Homework_Publish homework_publish = homework_publishDao.findByClassidAndHomeworknumber(classid, homeworknumber);
            Homework_Submit homework_submit = homework_submitDao.findByStudentidAndClassidAndHomeworknumber(studentid, classid, homeworknumber);

            temp.put("homeworknumber", homeworknumber);
            temp.put("homeworkname", homework_publish.getHomeworkname());
            temp.put("uploadtime", timeFormatter.format(homework_submit.getUploadtime()));
            temp.put("marktime", timeFormatter.format(homework_grade.getMarktime()));
            temp.put("docaddress", homework_submit.getContent());
            temp.put("grade", homework_grade.getGrade());
            temp.put("tx_hash", homework_grade.getTx_hash());
            temp.put("block_number", homework_grade.getBlock_number());
            temp.put("block_hash", homework_grade.getBlock_hash());

            resultJson.add(temp);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultJson);
    }

    /**
     *   List<
     *   homeworknumber(作业次数，int)
     *   homeworkname(作业题目)
     *   studentname(学生姓名)
     *   uploadtime(作业上传时间 YYYY-MM-DD HH:MM:SS)
     *   marktime(作业批改时间 YYYY-MM-DD HH:MM:SS)
     *   description(作业详情，批改作业页面需要)
     *   docname(学生作业的文档名称，批改作业页面需要)
     *   docaddress(学生作业的文档下载地址，批改作业页面需要)
     *   grade(分数)
     * >
     * **/
    @Override
    public ServerResponse getMarkedHomework(String username, String classid) {
        Stu_Info student  = stu_infoDao.findByNumber(username);
        String studentid = student.getStudentid();
        List<Homework_Grade> allMarkedHomework = homework_gradeDao.findByStudentidAndClassid(studentid, classid);
        JSONArray resultJson = new JSONArray();
        for (Homework_Grade homework_grade : allMarkedHomework) {
            JSONObject temp = new JSONObject();

            // 拿到这次作业的publish和submit
            Integer homeworknumber = homework_grade.getHomeworknumber();
            Homework_Publish homework_publish = homework_publishDao.findByClassidAndHomeworknumber(classid, homeworknumber);
            Homework_Submit homework_submit = homework_submitDao.findByStudentidAndClassidAndHomeworknumber(studentid, classid, homeworknumber);

            temp.put("homeworknumber", homeworknumber);
            temp.put("homeworkname", homework_publish.getHomeworkname());
            temp.put("studentname", student.getStudentname());
            temp.put("uploadtime", timeFormatter.format(homework_submit.getUploadtime()));
            temp.put("marktime", timeFormatter.format(homework_grade.getMarktime()));
            temp.put("description", homework_publish.getDescription());
            temp.put("docname", homework_submit.getDocname());
            temp.put("docaddress", homework_submit.getContent());
            temp.put("grade", homework_grade.getGrade());

            resultJson.add(temp);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultJson);
    }

    /**
     *     List<
     *     classid（课程id,int）,
     *     classname（课程名）,
     *     schoolid（课程学校,int）,
     *     number（参加人数,int）,
     *     description(课程描述),
     *     classpic（课程图片）
     *     **/
    @Override
    public ServerResponse getSelectedClass(String username) {
        String studentid = stu_infoDao.findByNumber(username).getStudentid();
        List<Select_Class> select_classes = select_classDao.findAllByStudentid(studentid);
        JSONArray resultJson = new JSONArray();
        for(Select_Class select_class : select_classes) {
            Class_Info class_info = class_infoDao.findByClassid(select_class.getClassid());
            JSONObject temp = new JSONObject();
            temp.put("classid", class_info.getClassid());
            temp.put("classname", class_info.getClassname());
            temp.put("schoolid", class_info.getSchoolid());
            temp.put("number", class_info.getNumber());
            temp.put("description", class_info.getDescription());
            temp.put("classpic", class_info.getClasspic());
            resultJson.add(temp);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultJson);
    }

    /**
     *      number(手机号)
     *      password(密码)
     *      studentname(姓名)
     *      gender (性别 int 男-1、女-2)
     *      idcard (身份证号码 String)
     *      schoolspecial(学校名称 String)
     *      institute(院系 String)
     *      major(专业 String)
     *      sno(学号 String)
     *      intime（入学年月 YYYY：MM：DD）
     *      outtime（毕业年月 YYYY：）
     *      degree (类别 int，小学-1、初中-2、高中-3、专科-4、本科-5、硕士-6、博士-7)
     *      studentlogo (头像，url)
     *      **/
    @Override
    public ServerResponse getStudentInfo(String username) {
        Stu_Info stu_info = stu_infoDao.findByNumber(username);
        if(stu_info==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.ACCOUNT_NOT_FOUND);
        JSONObject result = new JSONObject();
        result.put("number", stu_info.getNumber());
        result.put("studentname", stu_info.getStudentname());
        result.put("gender", stu_info.getGender());
        result.put("idcard", stu_info.getIdcard());
        result.put("schoolspecial", stu_info.getSchoolspecial());
        result.put("institute", stu_info.getInstitute());
        result.put("major", stu_info.getMajor());
        result.put("sno", stu_info.getSno());
        result.put("intime", dateFormatter.format(stu_info.getIntime()));
        result.put("outtime", dateFormatter.format(stu_info.getOuttime()));
        result.put("degree", stu_info.getDegree());
        result.put("studentlogo", stu_info.getStudentlogo());

        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(result);
    }

    /**
     *   List<
     *   homeworknumber(作业次数，int)
     *   homeworkname(作业题目)
     *   uploadtime(作业上传时间 YYYY-MM-DD HH:MM:SS)
     *   ddl(截止时间 YYYY-MM-DD HH:MM:SS)
     *   description(作业详情，批改作业页面需要)
     *   docname(学生作业的文档名称，批改作业页面需要)
     *   docaddress(学生作业的文档下载地址，批改作业页面需要)
     *   >
     *   **/
    @Override
        public ServerResponse getUnMarkedHomework(String username, String classid) {
        String studentid = stu_infoDao.findByNumber(username).getStudentid();
        List<Homework_Submit> allUnMarkedHomework = homework_submitDao.findUnMarkedHomeworkStudent(studentid, classid);
        JSONArray resultJson = new JSONArray();
        for (Homework_Submit homework_submit : allUnMarkedHomework) {
            JSONObject temp = new JSONObject();

            // 本次作业 homework_publish
            Homework_Publish homework_publish = homework_publishDao.findByClassidAndHomeworknumber(classid, homework_submit.getHomeworknumber());

            temp.put("homeworknumber", homework_submit.getHomeworknumber());
            temp.put("homeworkname", homework_publish.getHomeworkname());
            temp.put("uploadtime", timeFormatter.format(homework_submit.getUploadtime()));
            temp.put("ddl", timeFormatter.format(homework_publish.getDdl()));
            temp.put("description", homework_publish.getDescription());
            temp.put("docname", homework_submit.getDocname());
            temp.put("docaddress", homework_submit.getContent());

            resultJson.add(temp);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultJson);
    }

    /**
     *   List<
     *   homeworknumber(作业次数，int)
     *   homeworkname(作业名称)
     *   description(作业详情)
     *   starttime(发布时间 YYYY-MM-DD HH:MM:SS)
     *   ddl(截止时间 YYYY-MM-DD HH:MM:SS)
     *   >
     *   **/
    @Override
    public ServerResponse getUnUploadedHomework(String username, String classid) {
        // 拿到所有Homework_Publish
        List<Homework_Publish> homeworks = homework_publishDao.findAllByClassid(classid);
        String studentid = stu_infoDao.findByNumber(username).getStudentid();
        // 拿到学生这门课所有提交的作业
        List<Homework_Submit> allSubmiteHomeworks = homework_submitDao.findHomeworkSubmitStudent(studentid, classid);
        Map<Integer, Homework_Submit> submitMap = new HashMap<>();
        // 第number次作业
        for (Homework_Submit h : allSubmiteHomeworks) {
            submitMap.put(h.getHomeworknumber(), h);
        }

        JSONArray resultJson = new JSONArray();
        for (Homework_Publish homework : homeworks) {
            Integer homeworknumber = homework.getHomeworknumber();
            Homework_Submit submit = submitMap.get(homeworknumber);
            if (submit == null) { // 显示未提交的作业
                JSONObject temp = new JSONObject();
                temp.put("homeworknumber", homework.getHomeworknumber());
                temp.put("homeworkname", homework.getHomeworkname());
                temp.put("description", homework.getDescription());
                temp.put("starttime", timeFormatter.format(homework.getStarttime()));
                temp.put("ddl", timeFormatter.format(homework.getDdl()));
                resultJson.add(temp);
            }
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultJson);
    }

    /**
     *
     * @return status:(success 0/ failed 1)
     */
    @Override
    public ServerResponse selectClass(String username,String classid) {
        //判断是否重复选课
        String studentid = stu_infoDao.findByNumber(username).getStudentid();
        Select_Class select_class = select_classDao.findByClassidAndStudentid(classid,studentid);
        if(select_class!=null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.CLASS_ALREADY_SELECT);

        //判断是否存在这门课
        Class_Info class_info = class_infoDao.findByClassid(classid);
        if(class_info==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_EXIST).data("课程不存在");

        select_class = new Select_Class();
        select_class.setClassid(classid);
        select_class.setStudentid(studentid);

        Select_Class result = select_classDao.save(select_class);
        if(result!=null){
            class_info.setNumber(class_info.getNumber()+1);
            class_infoDao.save(class_info);
            return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SAVE_SUCCESS).data(result);
        }

        else
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.FAILED);
    }

    @Override
    public ServerResponse uploadHomework(String username, String classid, Integer homeworknumber, MultipartFile file) {
        String studentid = stu_infoDao.findByNumber(username).getStudentid();
        //判断是否已经选课
        Select_Class select_class = select_classDao.findByClassidAndStudentid(classid,studentid);
        if(select_class == null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_EXIST).data("您没选课");

        //判断需要提交的作业是否存在
        Homework_Publish homework_publish = homework_publishDao.findByClassidAndHomeworknumber(classid,homeworknumber);
        if(homework_publish==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_EXIST).data("作业不存在");

        //判断是否已经提交
        Homework_Submit homework_submit = homework_submitDao.findByStudentidAndClassidAndHomeworknumber(studentid,classid,homeworknumber);
        if(homework_submit!=null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.HOMEWORK_ALREADY_SUBMIT);

        String ipfsFileHash = null;

        //上传文件
        try{ ipfsFileHash = ipfsFileHelper.upload(file); }
        catch (Exception e) { e.printStackTrace(); }

        //文件地址返回为空
        if(ipfsFileHash == null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_FILE_FAILED);
        if(homework_submitDao.findByContent(ipfsFileHash)!=null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.FILE_ALREADY_EXIST);

        homework_submit = new Homework_Submit();
        homework_submit.setDocname(file.getOriginalFilename());
        homework_submit.setStudentid(studentid);
        homework_submit.setClassid(classid);
        homework_submit.setHomeworknumber(homeworknumber);
        homework_submit.setContent(ipfsFileHash);
        homework_submit.setFlag(0);
        homework_submit.setUploadtime(new Date());

        //上链

        TxReceipt receipt = homeworkSubmitHelper.insert(homework_submit,class_infoDao.findClassnameByClassid(classid));
        //上链失败
        if(receipt==null || 0 == Integer.parseInt(receipt.getOutput().substring(2, receipt.getOutput().length()), 16)){
            System.out.println("HomeworkSubmit Insert Failed,Tx_Hash:"+receipt.getTransactionHash());
            System.out.println("                   Output :"+receipt.getOutput());
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_IN_BLOCKCHAIN_FAILED);
        }

        homework_submit.setTx_hash(receipt.getTransactionHash());
        homework_submit.setBlock_hash(receipt.getBlockHash());
        homework_submit.setBlock_number(receipt.getBlockNumber());

        Homework_Submit result = homework_submitDao.save(homework_submit);
        if(result!=null)
            return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SAVE_SUCCESS).data(homework_submit);
        else
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.FAILED);
    }

}
