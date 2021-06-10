package com.edu.education.helper.smsHelper;

import com.edu.education.common.ResponseEnum;
import com.edu.education.common.ServerResponse;
import com.edu.education.helper.common.JwtPublicHelper;
import com.edu.education.helper.encryptHelper.DESEncryptHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

/**
 * JWT的token，区分大小写
 */
@ConfigurationProperties(prefix = "config.sms")
@Component
public class SMSTokenHelper extends JwtPublicHelper {

    /**
     * 生成token
     */
    public String createToken (String subject,String number){
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);//过期时间

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(DESEncryptHelper.DESEncrpyt(subject,secret))
                .setId(number)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     *  Token 验证
     *  使用DES加密算法
     **/
    public int checkToken(String token,String code,String number){

        //token为空
        if(token == null || "".equals(token)){ return(TOKEN_NULL); }

        Claims claims = null;
        try{
            claims = getTokenClaim(token);
            //签名错误
            if(claims == null){ return (SIGN_FAULT); }
            //过期
            if(isTokenExpired(claims.getExpiration())){ return(TOKEN_EXPIRED); }
            //手机号不符合
            if( !claims.getId().equals(number)){ return (TOKEN_WRONG); }

        }catch (Exception e){
            return(TOKEN_WRONG);
        }
        //检验答案
        if(DESEncryptHelper.DESDecrpyt(claims.getSubject(),secret).equals(code))
            return(TOKEN_SUCCESS);
        else
            return (TOKEN_WRONG);
    }

    public String createNumber(int digit){
        String text = "1234567890";
        String result = "";
        Random random = new Random();
        for (int i = 0; i < digit; i++) {
            int index = random.nextInt(text.length());
            char c = text.charAt(index);
            result += c;
        }
        return result;
    }

    public ServerResponse handleResult(int result){
        if(SMSTokenHelper.TOKEN_EXPIRED == result)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SMS_EXPIRED);
        else if(SMSTokenHelper.TOKEN_WRONG == result)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SMS_ERROR);
        else if(SMSTokenHelper.SIGN_FAULT == result)
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.SIGN_ERROR);
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.FAILED);
    }

}
