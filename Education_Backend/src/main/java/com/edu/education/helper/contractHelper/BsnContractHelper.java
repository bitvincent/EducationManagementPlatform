package com.edu.education.helper.contractHelper;

import com.alibaba.fastjson.JSONArray;
import com.bsnbase.sdk.client.fiscobcos.FiscobcosClient;
import com.bsnbase.sdk.client.fiscobcos.service.UserService;
import com.bsnbase.sdk.entity.config.Config;
import com.bsnbase.sdk.entity.req.fiscobcos.ReqKeyEscrow;
import com.bsnbase.sdk.entity.req.fiscobcos.ReqUserRegister;
import com.bsnbase.sdk.entity.res.fiscobcos.ResKeyEscrow;
import com.bsnbase.sdk.util.exception.GlobalException;
import com.edu.education.config.ContractProperties;
import org.fisco.bcos.web3j.tx.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "contract.bsn", name = "using", havingValue = "true", matchIfMissing = false)
public class BsnContractHelper {

    @Autowired
    private ContractProperties contractProperties;

    //初始化config。
    @PostConstruct
    public void initConfig() throws IOException {
        try {
            Config config = new Config();
            config.setAppCode(contractProperties.bsn.appCode);
            config.setUserCode(contractProperties.bsn.userCode);
            config.setApi(contractProperties.bsn.api);
            String storage_path = System.getProperty("user.dir")+"/config/";
            config.setMspDir(storage_path+contractProperties.getBsn().mspDir);
            config.setPrk(readResource(contractProperties.getBsn().getPrk()));
            config.setPuk(readResource(contractProperties.getBsn().getPuk()));
            config.setTestServerIdn(true);
            config.initConfig(config);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public String readResource(Resource resource) {
        String txt = "";
        try {
            //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
            InputStream stream = resource.getInputStream();
            StringBuilder buffer = new StringBuilder();
            byte[] bytes = new byte[1024];
            try {
                for (int n; (n = stream.read(bytes)) != -1; ) {
                    buffer.append(new String(bytes, 0, n));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            txt = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return txt;
    }

    //调用用户注册接口
    public void userRegister() {

        ReqUserRegister register = new ReqUserRegister();
        register.setUserId("test22");
        try {
            UserService.userRegister(register);
        } catch (GlobalException g) {
            g.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    //调用发送交易接口
    public ResKeyEscrow sendTransaction(String userId, String contractName, String funcName, String funcParam) {
        try {
            ReqKeyEscrow reqKeyEscrow = new ReqKeyEscrow();
            reqKeyEscrow.setUserId(userId);
            reqKeyEscrow.setContractName(contractName);
            reqKeyEscrow.setFuncName(funcName);
            reqKeyEscrow.setFuncParam(funcParam);
            return FiscobcosClient.reqChainCode(reqKeyEscrow);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    //将string数组转化成 json
//    public String convertArrayToJson(String[] param){
//        JSONArray jsonArray;
//        jsonArray=new JSONArray(Arrays.asList(param));
//        JSONArray jsonArgs =  new JSONArray();
//        jsonArgs.add(jsonArray);
//        return jsonArgs.toString();
//    }

    //将string List转化成 json
    public String convertArrayToJson(List<String> param) {
        JSONArray jsonArray;
        jsonArray = new JSONArray(Arrays.asList(param));
        return jsonArray.toString();
    }

    public ResKeyEscrow insertClassGrade(List<String> param) {

        ResKeyEscrow resKeyEscrow = sendTransaction(contractProperties.bsn.userid, contractProperties.bsn.ClassGrade, "insert", convertArrayToJson(param));
        return resKeyEscrow;
    }

    public ResKeyEscrow insertClassInfo(List<String> param) {
        ResKeyEscrow resKeyEscrow = sendTransaction(contractProperties.bsn.userid, contractProperties.bsn.ClassInfo, "insert", convertArrayToJson(param));
        return resKeyEscrow;
    }

    public ResKeyEscrow insertHomeworkGrade(List<String> param) {
        ResKeyEscrow resKeyEscrow = sendTransaction(contractProperties.bsn.userid, contractProperties.bsn.HomeworkGrade, "insert", convertArrayToJson(param));
        return resKeyEscrow;
    }

    public ResKeyEscrow insertHomeworkPublish(List<String> param) {
        ResKeyEscrow resKeyEscrow = sendTransaction(contractProperties.bsn.userid, contractProperties.bsn.HomeworkPublish, "insert", convertArrayToJson(param));
        return resKeyEscrow;
    }

    public ResKeyEscrow insertHomeworkSubmit(List<String> param) {
        ResKeyEscrow resKeyEscrow = sendTransaction(contractProperties.bsn.userid, contractProperties.bsn.HomeworkSubmit, "insert", convertArrayToJson(param));
        return resKeyEscrow;
    }

    public ResKeyEscrow insertStuInfo(List<String> param) {
        ResKeyEscrow resKeyEscrow = sendTransaction(contractProperties.bsn.userid, contractProperties.bsn.StuInfo, "insert", convertArrayToJson(param));
        return resKeyEscrow;
    }

    public ResKeyEscrow insertTeacherInfo(List<String> param) {
        ResKeyEscrow resKeyEscrow = sendTransaction(contractProperties.bsn.userid, contractProperties.bsn.TeacherInfo, "insert", convertArrayToJson(param));
        return resKeyEscrow;
    }

    public ResKeyEscrow insertCertificate(List<String> param) {
        ResKeyEscrow resKeyEscrow = sendTransaction(contractProperties.bsn.userid, contractProperties.bsn.Certificate, "insert", convertArrayToJson(param));
        return resKeyEscrow;
    }


}
