package com.edu.education.helper.contractHelper;

import com.bsnbase.sdk.entity.res.fiscobcos.ResKeyEscrow;
import com.edu.education.common.TxReceipt;
import com.edu.education.config.ContractProperties;
import com.edu.education.contract.HomeworkPublishContract;
import com.edu.education.contract.HomeworkSubmitContract;
import com.edu.education.contract.constants.GasConstants;
import com.edu.education.entity.Homework_Submit;
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
@ConfigurationProperties(prefix = "com.homework-submit")
public class HomeworkSubmitHelper {
    @Autowired(required = false)
    private Web3j web3j;
    @Autowired(required = false)
    private Credentials credentials;
    private String address;

    @Autowired
    private ContractProperties contractProperties;
    @Autowired(required = false)
    private BsnContractHelper bsnContractHelper;

    public TxReceipt insert(Homework_Submit homework_submit, String classname) {
        try {

            List<String> args = new ArrayList<String>();
            args.add(0,homework_submit.getStudentid());
            ECCEncryptHelper eccEncryptHelper = new ECCEncryptHelper();
            args.add(1,eccEncryptHelper.Encrypt(new BigInteger(homework_submit.getStudentid()),homework_submit.getContent()));
            args.add(2,homework_submit.getClassid());
            args.add(3,classname);
            args.add(4,homework_submit.getHomeworknumber().toString());
            args.add(5,homework_submit.getUploadtime().getTime()+"");


            String type = contractProperties.getBsn().getUsing();
            if(type.equals("true")){
                ResKeyEscrow resKeyEscrow = bsnContractHelper.insertHomeworkSubmit(args);
                if(resKeyEscrow == null) return null;
                return new TxReceipt(resKeyEscrow);
            }
            else {
                HomeworkSubmitContract mContract = HomeworkSubmitContract.load(address,web3j,credentials,new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
                TransactionReceipt receipt = mContract.insert(args).send();
                if(receipt == null) return null;
                return (new TxReceipt(receipt));
            }

        } catch (Exception e) {
            System.out.println("=======Homework_Submit Contract execute failed=======\n");
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
