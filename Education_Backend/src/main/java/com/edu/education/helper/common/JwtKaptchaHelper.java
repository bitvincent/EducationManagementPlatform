package com.edu.education.helper.common;

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
@ConfigurationProperties(prefix = "config.kaptcha")
@Component
public class JwtKaptchaHelper extends JwtPublicHelper {

}
