package com.edu.education.helper.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * JWT的token，区分大小写
 */
@ConfigurationProperties(prefix = "config.token")
@Component
public class JwtTokenHelper extends JwtPublicHelper{

}
