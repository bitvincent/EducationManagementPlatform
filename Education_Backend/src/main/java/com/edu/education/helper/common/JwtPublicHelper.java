package com.edu.education.helper.common;

import com.edu.education.helper.encryptHelper.DESEncryptHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtPublicHelper {

    protected String secret;
    protected long expire;
    protected String header;

    public static int TOKEN_SUCCESS = 0;
    public static int TOKEN_NULL    = 1;
    public static int TOKEN_EXPIRED = 2;
    public static int SIGN_FAULT    = 3;
    public static int TOKEN_WRONG   = 4;

    /**
     * 生成token
     */
    public String createToken (String subject){
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);//过期时间

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(DESEncryptHelper.DESEncrpyt(subject,secret))
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 获取token subject
     */
    public String getTokenSubject (String token) {
        try {
            return DESEncryptHelper.DESDecrpyt(getTokenClaim(token).getSubject(),secret);
        }catch (Exception e){
            return null;
        }
    }

    /**
     *  Token 验证
     *  使用DES加密算法
     **/
    public int checkToken(String token,String code){

        //token为空
        if(token == null || "".equals(token)){ return(TOKEN_NULL); }

        Claims claims = null;
        try{
            claims = getTokenClaim(token);
            //签名错误
            if(claims == null){ return (SIGN_FAULT); }
            //过期
            if(isTokenExpired(claims.getExpiration())){ return(TOKEN_EXPIRED); }

        }catch (Exception e){
            return(TOKEN_WRONG);
        }
        //检验答案
        if(DESEncryptHelper.DESDecrpyt(claims.getSubject(),secret).equals(code))
            return(TOKEN_SUCCESS);
        else
            return (TOKEN_WRONG);
    }

    /**
     * 获取token Claim
     */
    public Claims getTokenClaim (String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 验证token是否过期失效
     */
    public boolean isTokenExpired (Date expirationTime) {
        return expirationTime.before(new Date());
    }

    /**
     * 获取token失效时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getTokenClaim(token).getExpiration();
    }
    /**
     * 获取jwt发布时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getTokenClaim(token).getIssuedAt();
    }


    // --------------------- getter & setter ---------------------

    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public long getExpire() {
        return expire;
    }
    public void setExpire(long expire) {
        this.expire = expire;
    }
    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }
}
