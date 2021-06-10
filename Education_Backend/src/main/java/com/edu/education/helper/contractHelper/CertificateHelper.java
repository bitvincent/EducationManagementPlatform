package com.edu.education.helper.contractHelper;

import com.bsnbase.sdk.entity.res.fiscobcos.ResKeyEscrow;
import com.edu.education.common.TxReceipt;
import com.edu.education.config.ContractProperties;
import com.edu.education.contract.CertificateContract;
import com.edu.education.contract.constants.GasConstants;
import com.edu.education.entity.Certificate;
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
@ConfigurationProperties(prefix = "com.certificate")
public class CertificateHelper {
    @Autowired(required = false)
    private Web3j web3j;
    @Autowired(required = false)
    private Credentials credentials;

    @Autowired
    private ContractProperties contractProperties;
    @Autowired(required = false)
    private BsnContractHelper bsnContractHelper;

    private String address;

    public TxReceipt insert(Certificate certificate,String teacherid) {
        try {
            List<String> args = new ArrayList<String>();
            args.add(0,certificate.getStudentid());
            args.add(1,teacherid);
            args.add(2,certificate.getClassid());
            args.add(3,certificate.getPasstime().getTime()+"");
            args.add(4,String.valueOf(certificate.getFinalgrade()));

            String type = contractProperties.getBsn().getUsing();
            if(type.equals("true")){
                ResKeyEscrow resKeyEscrow = bsnContractHelper.insertCertificate(args);
                if(resKeyEscrow == null) return null;
                return new TxReceipt(resKeyEscrow);
            }
            else {
                CertificateContract mCertificateContract = CertificateContract.load(address,web3j,credentials,new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
                TransactionReceipt receipt = mCertificateContract.insert(args).send();
                if(receipt == null) return null;
                return (new TxReceipt(receipt));
            }

        } catch (Exception e) {
            System.out.println("=======Certificate Contract execute failed=======\n");
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
