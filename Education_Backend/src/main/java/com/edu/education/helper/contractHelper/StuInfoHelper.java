package com.edu.education.helper.contractHelper;

import com.bsnbase.sdk.entity.res.fiscobcos.ResKeyEscrow;
import com.edu.education.common.TxReceipt;
import com.edu.education.config.ContractProperties;
import com.edu.education.contract.HomeworkSubmitContract;
import com.edu.education.contract.StuInfoContract;
import com.edu.education.contract.constants.GasConstants;
import com.edu.education.entity.Stu_Info;
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
@ConfigurationProperties(prefix = "com.stu-info")
public class StuInfoHelper {
    @Autowired(required = false)
    private Web3j web3j;
    @Autowired(required = false)
    private Credentials credentials;
    private String address;

    @Autowired
    private ContractProperties contractProperties;
    @Autowired(required = false)
    private BsnContractHelper bsnContractHelper;

    public TxReceipt insert(Stu_Info stu_info) {
        try {

            List<String> args = new ArrayList<String>();
            //个人信息加密
            ECCEncryptHelper eccEncryptHelper = new ECCEncryptHelper(stu_info.getPrivkey(),stu_info.getStudentid());
            args.add(0,stu_info.getStudentid());
            args.add(1,eccEncryptHelper.Encrypt(stu_info.getStudentname()));
            args.add(2,eccEncryptHelper.Encrypt(stu_info.getGender().toString()));
            args.add(3,eccEncryptHelper.Encrypt(stu_info.getIdcard()));
            args.add(4,eccEncryptHelper.Encrypt(stu_info.getSchoolspecial()));
            args.add(5,eccEncryptHelper.Encrypt(stu_info.getInstitute()));
            args.add(6,eccEncryptHelper.Encrypt(stu_info.getMajor()));
            args.add(7,eccEncryptHelper.Encrypt(stu_info.getSno()));
            args.add(8,eccEncryptHelper.Encrypt(stu_info.getIntime().getTime()+""));
            args.add(9,eccEncryptHelper.Encrypt(stu_info.getOuttime().getTime()+""));
            args.add(10,eccEncryptHelper.Encrypt(stu_info.getDegree().toString()));

            String type = contractProperties.getBsn().getUsing();
            if(type.equals("true")){
                ResKeyEscrow resKeyEscrow = bsnContractHelper.insertStuInfo(args);
                if(resKeyEscrow == null) return null;
                return new TxReceipt(resKeyEscrow);
            }
            else {
                StuInfoContract mContract = StuInfoContract.load(address,web3j,credentials,new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
                TransactionReceipt receipt = mContract.insert(args).send();
                if(receipt == null) return null;
                return (new TxReceipt(receipt));
            }
        } catch (Exception e) {
            System.out.println("=======Stu_Info Contract execute failed=======\n");
            e.printStackTrace();
        }
        return null;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
