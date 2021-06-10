package com.edu.education.helper.smsHelper;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;;

@Component
@ConfigurationProperties(prefix = "sms")
public class SMSHelper {
    private String secretId;
    private String secretKey;
    private String SmsSdkAppid;
    private String TemplateID;


    public String sendSMS(String number,String content){
        try{

            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "", clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {number};
            req.setPhoneNumberSet(phoneNumberSet1);

            String[] templateParamSet1 = {content};
            req.setTemplateParamSet(templateParamSet1);

            req.setSmsSdkAppid(SmsSdkAppid);
            req.setTemplateID(TemplateID);
            req.setSign("奕江科技");

            SendSmsResponse resp = client.SendSms(req);

            System.out.println(SendSmsResponse.toJsonString(resp));
            return resp.getSendStatusSet()[0].getCode();

        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return null;
        }
    }


    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSmsSdkAppid() {
        return SmsSdkAppid;
    }

    public void setSmsSdkAppid(String smsSdkAppid) {
        SmsSdkAppid = smsSdkAppid;
    }

    public String getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(String templateID) {
        TemplateID = templateID;
    }
}
