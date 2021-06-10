package com.edu.education.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edu.education.common.ResponseEnum;
import com.edu.education.common.ServerResponse;
import com.edu.education.common.TxReceipt;
import com.edu.education.dao.*;
import com.edu.education.entity.*;
import com.edu.education.helper.fileHelper.PictureHelper;
import com.edu.education.helper.contractHelper.*;
import com.edu.education.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private Teacher_InfoDao teacher_infoDao;

    @Autowired
    private Class_InfoDao class_infoDao;
    @Autowired
    private ClassInfoHelper classInfoHelper;

    @Autowired
    private Homework_PublishDao homework_publishDao;
    @Autowired
    private HomeworkPublsihHelper homeworkPublsihHelper;

    @Autowired
    private Homework_SubmitDao homework_submitDao;


    @Autowired
    private Homework_GradeDao homework_gradeDao;
    @Autowired
    private HomeworkGradeHelper homeworkGradeHelper;

    @Autowired
    private Class_GradeDao class_gradeDao;
    @Autowired
    private ClassGradeHelper classGradeHelper;

    @Autowired
    private CertificateDao certificateDao;
    @Autowired
    private CertificateHelper certificateHelper;

    @Autowired
    private Select_ClassDao select_classDao;

    @Autowired
    private Stu_InfoDao stu_infoDao;

    @Autowired
    PictureHelper pictureHelper;

    /**
     * classid查 Homework_Submit,获得 studentid,homeworknumber,content,docname,uploadtime
     * studentid查 Stu_Info，获得 studentname
     * classid查 Homework_Publish，获得 homeworkname,ddl(根据homeworknumber匹配)
     *
     * List<
     *   homeworknumber(作业次数，int)
     *   homeworkname(作业题目)
     *   studentname(学生姓名)
     *   uploadtime(作业上传时间 YYYY-MM-DD HH:MM:SS)
     *   ddl(截止时间 YYYY-MM-DD HH:MM:SS)
     *   description(作业详情，批改作业页面需要)
     *   docname(学生作业的文档名称，批改作业页面需要)
     *   docaddress(学生作业的文档下载地址，批改作业页面需要)
     * >
     * **/
    @Override
    public ServerResponse getUnMarkedHomework(String username, String classid) {
        String teacherid = teacher_infoDao.findTeacheridByNumber(username);
        List<Homework_Submit> homework_submits = homework_submitDao.findUnMarkedHomeworkTeacher(classid);
        JSONArray resultArray = new JSONArray();
        for(Homework_Submit homework_submit:homework_submits){
            String studentname = stu_infoDao.findStudentnameByStudentid(homework_submit.getStudentid());
            Homework_Publish homework_publish = homework_publishDao.findByClassidAndHomeworknumber(classid,homework_submit.getHomeworknumber());
            JSONObject object = new JSONObject();
            object.put("homeworknumber",homework_submit.getHomeworknumber());
            object.put("homeworkname",homework_publish.getHomeworkname());
            object.put("studentname",studentname);
            object.put("uploadtime",homework_submit.getUploadtime());
            object.put("description",homework_publish.getDescription());
            object.put("ddl",homework_publish.getDdl());
            object.put("docname",homework_submit.getDocname());
            object.put("docaddress",homework_submit.getContent());
            resultArray.add(object);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultArray);
    }

    /**
     * 需要根据 classid 查 homework_grade表提供 homeworknumber,docaddress,marktime,grade,studentid
     * 需要根据 studentid 查 stu_info，获取 studentname
     * 需要根据 classid,homeworknumber 查 homework_publsih，获取desctiption，homeworkname
     * 需要根据 studentid,homeworknumber,classid, 联查 homework_submit，获取docname
     *
     * List<
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

        String teacherid = teacher_infoDao.findTeacheridByNumber(username);
        List<Homework_Submit> homework_submits = homework_submitDao.findMarkedHomeworkTeacher(classid);
        JSONArray resultArray = new JSONArray();
        for(Homework_Submit homework_submit:homework_submits){

            String studentname = stu_infoDao.findStudentnameByStudentid(homework_submit.getStudentid());
            Homework_Publish homework_publish = homework_publishDao.findByClassidAndHomeworknumber(classid,homework_submit.getHomeworknumber());
            Homework_Grade homework_grade = homework_gradeDao.findByStudentidAndClassidAndHomeworknumber(homework_submit.getStudentid(),homework_submit.getClassid(),homework_submit.getHomeworknumber());

            JSONObject object = new JSONObject();
            object.put("homeworknumber",homework_submit.getHomeworknumber());
            object.put("homeworkname",homework_publish.getHomeworkname());
            object.put("studentname",studentname);
            object.put("uploadtime",homework_submit.getUploadtime());
            object.put("marktime",homework_grade.getMarktime());
            object.put("description",homework_publish.getDescription());
            object.put("docname",homework_submit.getDocname());
            object.put("docaddress",homework_submit.getContent());
            object.put("grade",homework_grade.getGrade());
            resultArray.add(object);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultArray);
    }

    /**
     *   List<
     *     studentid(学生id)
     *     studentname(学生姓名)
     *     grades：（成绩
     *       List<
     *         作业次数 成绩
     *         1      ：80
     *         2      ：100
     *         3      ：0
     *         ..
     *         ..
     *       >
     *     ）
     *   >
     *   **/
    @Override
    public ServerResponse getAllGrade(String username, String classid) {
        JSONArray resultArray = new JSONArray();
        List<Select_Class> select_classes = select_classDao.findByClassid(classid);
        for(Select_Class select_class:select_classes){

            String studentid = select_class.getStudentid();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentid",studentid);
            jsonObject.put("studentname",stu_infoDao.findStudentnameByStudentid(studentid));
            jsonObject.put("finalgrade",class_gradeDao.findFinalgradeByStudentidAndClassid(studentid,classid));
            List<Homework_Grade> homework_grades =  homework_gradeDao.findByStudentidAndClassid(studentid,classid);

            JSONArray gradeArray = new JSONArray();
            Integer homeworknum = homework_publishDao.getHomeworkNumberByClassid(classid);
            for(int i =0;i<homeworknum;i++){
                gradeArray.add(i,null);
            }
            for(Homework_Grade homework_grade:homework_grades){
                gradeArray.set(homework_grade.getHomeworknumber()-1,homework_grade.getGrade());
            }
            jsonObject.put("grade",gradeArray);
            jsonObject.put("pass",class_gradeDao.findPassByStudentidAndClassid(studentid,classid));
            resultArray.add(jsonObject);
        }
        return ServerResponse.getInstance().data(resultArray).responseEnum(ResponseEnum.GET_SUCCESS).ok();
    }

    /**
     * List<
     * classid（课程id,int）,
     * classname（课程名）,
     * schoolid（课程学校,int）,
     * number（参加人数,int）,
     * description(课程描述),
     * classpic（课程图片）>
     * **/
    @Override
    public ServerResponse  getPublishedClass(String username) {
        String teacherid = teacher_infoDao.findTeacheridByNumber(username);
        List<Class_Info> class_infos = class_infoDao.findByTeacherid(teacherid);
        JSONArray resultJson = new JSONArray();
        for(Class_Info class_info : class_infos){
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
     * List<
     * homeworknumber(作业次数，int)
     * homeworkname(作业名称)
     * description(作业详情)
     * starttime(发布时间 YYYY-MM-DD HH:MM:SS)
     * ddl(截止时间 YYYY-MM-DD HH:MM:SS)
     * >
     * **/
    @Override
    public ServerResponse getPublishedHomework(String username, String classid) {
        List<Homework_Publish> homework_publishes = homework_publishDao.findAllByClassid(classid);
        JSONArray resultArray = new JSONArray();
        for(Homework_Publish homework_publish:homework_publishes){
            JSONObject object = new JSONObject();
            object.put("homeworknumber",homework_publish.getHomeworknumber());
            object.put("homeworkname",homework_publish.getHomeworkname());
            object.put("description",homework_publish.getDescription());
            object.put("starttime",homework_publish.getStarttime());
            object.put("ddl",homework_publish.getDdl());
            resultArray.add(object);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultArray);
    }

    /**
     *   classname(课程名称)
     *   category (课程门类，int)
     *   homeworknumber(作业次数, int)
     *   homeworkname（作业名称）
     *   description（作业详情）
     *   starttime（发布时间 YYYY-MM-DD HH:MM:SS）
     *   ddl（截止时间 YYYY-MM-DD HH:MM:SS）
     *   **/
    @Override
    public ServerResponse getTeacherHomeworkInfo(String username, String classid, Integer homeworknumber) {
        Homework_Publish homework_publish = homework_publishDao.findByClassidAndHomeworknumber(classid,homeworknumber);
        Class_Info class_info = class_infoDao.findByClassid(classid);
        JSONObject resultobject = new JSONObject();
        resultobject.put("classname",class_info.getClassname());
        resultobject.put("category",class_info.getCategory());
        resultobject.put("homeworknumber",homework_publish.getHomeworknumber());
        resultobject.put("homeworkname",homework_publish.getHomeworkname());
        resultobject.put("description",homework_publish.getDescription());
        resultobject.put("starttime",homework_publish.getStarttime());
        resultobject.put("ddl",homework_publish.getDdl());
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(resultobject);
    }

    /**
     * number(手机号)
     * teachername(老师名称)
     * gender（性别，int）
     * idcard(身份证号码)
     * schoolid(学校id，int)
     * title (职称，int)
     * teacherlogo(老师头像,url)
     * **/
    @Override
    public ServerResponse getTeacherInfo(String username) {
        Teacher_Info teacher_info = teacher_infoDao.findByNumber(username);
        if(teacher_info==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.ACCOUNT_NOT_FOUND);
        JSONObject result = new JSONObject();
        result.put("number", teacher_info.getNumber());
        result.put("teacherid",teacher_info.getTeacherid());
        result.put("teachername", teacher_info.getTeachername());
        result.put("gender", teacher_info.getGender());
        result.put("idcard", teacher_info.getIdcard());
        result.put("schoolid", teacher_info.getSchoolid());
        result.put("title", teacher_info.getTitle());
        result.put("teacherlogo", teacher_info.getTeacherlogo());
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.GET_SUCCESS).data(result);
    }

    @Override
    public ServerResponse publishHomework(String username, String classid, Integer homeworknum, String homeworkname, String description, Date ddl) {
        //课程不存在
        Class_Info class_info = class_infoDao.findByClassid(classid);
        if(class_info==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_EXIST).data("课程不存在");

        //课程是否属于老师
        String teacherid = teacher_infoDao.findTeacheridByNumber(username);
        if(!teacherid.equals(class_info.getTeacherid()))
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_MATCH).data("课程不属于该老师");

        Homework_Publish homework_publish = new Homework_Publish();
        Integer homeworknumber = homework_publishDao.getHomeworkNumberByClassid(classid)+1;
        homework_publish.setClassid(classid);
        homework_publish.setHomeworkname(homeworkname);
        homework_publish.setHomeworknumber(homeworknumber);
        homework_publish.setDescription(description);
        homework_publish.setDdl(ddl);

        homework_publish.setStarttime(new Date());
        String classname = class_info.getClassname();

        TxReceipt receipt = homeworkPublsihHelper.insert(homework_publish,classname);
        //上链失败
        if(receipt==null || 0 == Integer.parseInt(receipt.getOutput().substring(2, receipt.getOutput().length()), 16)){
            System.out.println("HomeworkGrade Insert Failed,Tx_Hash:"+receipt.getTransactionHash());
            System.out.println("                   Output :"+receipt.getOutput());
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_IN_BLOCKCHAIN_FAILED);
        }

        homework_publish.setTx_hash(receipt.getTransactionHash());
        homework_publish.setBlock_hash(receipt.getBlockHash());
        homework_publish.setBlock_number(receipt.getBlockNumber());

        Homework_Publish result = homework_publishDao.save(homework_publish);

        if(result!=null)
            return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SAVE_SUCCESS).data(result);
        else
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_FAILED);
    }

    @Override
    public ServerResponse markHomework(String username, String classid, Integer grade,Integer homeworknumber,String docaddress) {
        //判断作业是否存在
        Homework_Submit homework_submit = homework_submitDao.findByContent(docaddress);
        if(homework_submit==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_EXIST).data("作业不存在");

        //判断是否批改过
        Homework_Grade homework_grade = homework_gradeDao.findByContent(docaddress);
        if(homework_grade!=null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.HOMEWORK_ALREADY_MARK);

        //课程是否属于老师
        String teacherid = teacher_infoDao.findTeacheridByNumber(username);
        if(!teacherid.equals(class_infoDao.findTeacheridByClassid(classid)))
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_MATCH).data("课程不属于该老师");

        homework_grade = new Homework_Grade();

        homework_grade.setClassid(classid);
        homework_grade.setGrade(grade);
        homework_grade.setHomeworknumber(homework_submit.getHomeworknumber());
        homework_grade.setMarktime(new Date());

        homework_grade.setContent(homework_submit.getContent());
        homework_grade.setStudentid(homework_submit.getStudentid());
        homework_grade.setDocname(homework_submit.getDocname());

        String classname = class_infoDao.findClassnameByClassid(classid);

        //上链

        TxReceipt receipt = homeworkGradeHelper.insert(homework_grade,classname,teacherid);
        //上链失败
        if(receipt==null || 0 == Integer.parseInt(receipt.getOutput().substring(2, receipt.getOutput().length()), 16)){
            System.out.println("HomeworkGrade Insert Failed,Tx_Hash:"+receipt.getTransactionHash());
            System.out.println("                   Output :"+receipt.getOutput());
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_IN_BLOCKCHAIN_FAILED);
        }

        homework_grade.setBlock_hash(receipt.getBlockHash());
        homework_grade.setTx_hash(receipt.getTransactionHash());
        homework_grade.setBlock_number(receipt.getBlockNumber());

        Homework_Grade result = homework_gradeDao.save(homework_grade);
        homework_submit.setFlag(1);
        homework_submitDao.save(homework_submit);

        if(result!=null)
            return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SAVE_SUCCESS).data(result);
        else
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_FAILED);
    }

    @Override
    public ServerResponse publishClass(String username,String classname, Integer category, Date starttime, Date endtime,
                                       Integer learnhours, String description, MultipartFile file) {
        Class_Info class_info = new Class_Info();

        class_info.setClassname(classname);
        class_info.setCategory(category);
        class_info.setStarttime(starttime);
        class_info.setEndtime(endtime);
        class_info.setLearnhours(learnhours);
        class_info.setDescription(description);

        Teacher_Info teacher = teacher_infoDao.findByNumber(username);
        class_info.setTeacherid(teacher.getTeacherid());
        class_info.setSchoolid(teacher.getSchoolid());
        String current_classid = UUID.randomUUID().toString();

        //TODO 将file转化为url
        String url = pictureHelper.savePicture(file,"course",current_classid);
        class_info.setClasspic(url);
        class_info.setNumber(0);
        class_info.setClassid(current_classid);


        TxReceipt receipt = classInfoHelper.insert(class_info);
        //上链失败
        if(receipt==null || 0 == Integer.parseInt(receipt.getOutput().substring(2, receipt.getOutput().length()), 16)){
            System.out.println("ClassInfo Insert Failed,Tx_Hash:"+receipt.getTransactionHash());
            System.out.println("                   Output :"+receipt.getOutput());
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_IN_BLOCKCHAIN_FAILED);
        }

        class_info.setTx_hash(receipt.getTransactionHash());
        class_info.setBlock_hash(receipt.getBlockHash());
        class_info.setBlock_number(receipt.getBlockNumber());

        Class_Info result = class_infoDao.save(class_info);
        if(result!=null)
            return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SAVE_SUCCESS).data(class_info);
        else
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.FAILED);
    }

    @Override
    public ServerResponse setFinalGrade(String username, String classid, String studentid, Integer finalgrade, Integer pass) {
        //课程是否存在
        Class_Info class_info = class_infoDao.findByClassid(classid);
        if(class_info == null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_EXIST).data("课程不存在");

        //课程是否属于老师
        String teacherid = teacher_infoDao.findTeacheridByNumber(username);
        if(!teacherid.equals(class_info.getTeacherid()))
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.DATA_NOT_MATCH).data("课程不属于该老师");

        //是否已经打分
        Class_Grade class_grade = class_gradeDao.findByStudentidAndClassid(studentid,classid);
        if(class_grade!=null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.GRADE_ALREADY_SET);

        class_grade = new Class_Grade();

        class_grade.setStudentid(studentid);
        class_grade.setClassid(classid);
        class_grade.setFinalgrade(finalgrade);
        class_grade.setPass(pass);
        class_grade.setPasstime(new Date());

        //上链

        TxReceipt receipt = classGradeHelper.insert(class_grade,class_info.getTeacherid(),class_info.getClassname());
        //上链失败
        if(receipt==null || 0 == Integer.parseInt(receipt.getOutput().substring(2, receipt.getOutput().length()), 16)){
            System.out.println("ClassGrade Insert Failed,Tx_Hash:"+receipt.getTransactionHash());
            System.out.println("                   Output :"+receipt.getOutput());
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_IN_BLOCKCHAIN_FAILED);
        }

        class_grade.setBlock_hash(receipt.getBlockHash());
        class_grade.setTx_hash(receipt.getTransactionHash());
        class_grade.setBlock_number(receipt.getBlockNumber());

        Class_Grade result = class_gradeDao.save(class_grade);

        if(result==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_FAILED).data("课程成绩保存失败");

        //发放证书
        Certificate certificate = new Certificate();

        certificate.setStudentid(studentid);
        certificate.setClassid(classid);
        certificate.setPasstime(new Date());
        certificate.setFinalgrade(finalgrade);

        //上链

        TxReceipt receipt2 = certificateHelper.insert(certificate,class_info.getTeacherid());
        //上链失败
        if(receipt2==null || 0 == Integer.parseInt(receipt2.getOutput().substring(2, receipt2.getOutput().length()), 16)){
            System.out.println("Certificate Insert Failed,Tx_Hash:"+receipt2.getTransactionHash());
            System.out.println("                   Output :"+receipt2.getOutput());
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_IN_BLOCKCHAIN_FAILED).data("证书上链失败");
        }

        certificate.setBlock_hash(receipt2.getBlockHash());
        certificate.setBlock_number(receipt2.getBlockNumber());
        certificate.setTx_hash(receipt2.getTransactionHash());

        Certificate result2 = certificateDao.save(certificate);

        if(result2==null)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SAVE_FAILED).data("证书保存失败");
        else
            return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SAVE_SUCCESS).data(result2);
    }
}
