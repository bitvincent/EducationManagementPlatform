package com.edu.education.helper.contractHelper;

import com.bsnbase.sdk.entity.res.fiscobcos.ResKeyEscrow;
import com.edu.education.common.TxReceipt;
import com.edu.education.config.ContractProperties;
import com.edu.education.contract.StuInfoContract;
import com.edu.education.contract.TeacherInfoContract;
import com.edu.education.contract.constants.GasConstants;
import com.edu.education.entity.Teacher_Info;
import com.edu.education.helper.encryptHelper.ECCEncryptHelper;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "com.teacher-info")
public class TeacherInfoHelper {
    @Autowired(required = false)
    private Web3j web3j;
    @Autowired(required = false)
    private Credentials credentials;
    private String address;

    @Autowired
    private ContractProperties contractProperties;
    @Autowired(required = false)
    private BsnContractHelper bsnContractHelper;

    public TxReceipt insert(Teacher_Info teacher_info) {
        try {
            List<String> args = new ArrayList<String>();
            ECCEncryptHelper eccEncryptHelper = new ECCEncryptHelper(teacher_info.getPrivkey(),teacher_info.getTeacherid());
            args.add(0,teacher_info.getTeacherid());
            args.add(1,eccEncryptHelper.Encrypt(teacher_info.getTeachername()));
            args.add(2,eccEncryptHelper.Encrypt(teacher_info.getGender().toString()));
            args.add(3,eccEncryptHelper.Encrypt(teacher_info.getIdcard()));
            args.add(4,eccEncryptHelper.Encrypt(teacher_info.getSchoolid().toString()));
            args.add(5,eccEncryptHelper.Encrypt(teacher_info.getTitle().toString()));


            String type = contractProperties.getBsn().getUsing();
            if(type.equals("true")){
                ResKeyEscrow resKeyEscrow = bsnContractHelper.insertTeacherInfo(args);
                if(resKeyEscrow == null) return null;
                return new TxReceipt(resKeyEscrow);
            }
            else {
                TeacherInfoContract mContract = TeacherInfoContract.load(address,web3j,credentials,new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
                TransactionReceipt receipt = mContract.insert(args).send();
                if(receipt == null) return null;
                return (new TxReceipt(receipt));
            }

        } catch (Exception e) {
            System.out.println("=======Teacher_Info Contract execute failed=======\n");
            e.printStackTrace();
        }
        return null;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
