package com.edu.education.helper.contractHelper;

import com.bsnbase.sdk.entity.res.fiscobcos.ResKeyEscrow;
import com.edu.education.common.TxReceipt;
import com.edu.education.config.ContractProperties;
import com.edu.education.contract.CertificateContract;
import com.edu.education.contract.ClassGradeContract;
import com.edu.education.contract.constants.GasConstants;
import com.edu.education.entity.Class_Grade;
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
@ConfigurationProperties(prefix = "com.class-grade")
public class ClassGradeHelper {
    @Autowired(required = false)
    private Web3j web3j;
    @Autowired(required = false)
    private Credentials credentials;
    private String address;

    @Autowired
    private ContractProperties contractProperties;
    @Autowired(required = false)
    private BsnContractHelper bsnContractHelper;

    public TxReceipt insert(Class_Grade class_grade, String teacherid, String classname) {
        try {
            List<String> args = new ArrayList<String>();
            args.add(0,class_grade.getStudentid());
            args.add(1,teacherid);
            args.add(2,class_grade.getClassid());
            args.add(3,classname);
            args.add(4,class_grade.getPasstime().getTime()+"");
            args.add(5,class_grade.getFinalgrade().toString());
            args.add(6,class_grade.getPass().toString());

            String type = contractProperties.getBsn().getUsing();
            if(type.equals("true")){
                ResKeyEscrow resKeyEscrow = bsnContractHelper.insertClassGrade(args);
                if(resKeyEscrow == null) return null;
                return new TxReceipt(resKeyEscrow);
            }
            else {
                ClassGradeContract mContract = ClassGradeContract.load(address,web3j,credentials,new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
                TransactionReceipt receipt = mContract.insert(args).send();
                if(receipt == null) return null;
                return (new TxReceipt(receipt));
            }

        } catch (Exception e) {
            System.out.println("=======Class_Grade Contract execute failed=======\n");
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
