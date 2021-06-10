package com.edu.education.controller;

import com.edu.education.common.DisableToken;
import com.edu.education.common.ResponseEnum;
import com.edu.education.common.ServerResponse;
import com.edu.education.helper.common.JwtKaptchaHelper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Api(tags = {"kaptcha验证码"})
@RestController
@RequestMapping("/kaptcha")
public class KaptchaController {

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Resource
    JwtKaptchaHelper jwtKaptchaHelper;

    @DisableToken
    @PostMapping(value = "/getKaptcha")
    @ResponseBody
    public void getKaptcha(HttpServletResponse httpServletResponse)
            throws Exception {

        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生产验证码字符串并将生成的Token保存到返回头
            String createText = defaultKaptcha.createText();
            httpServletResponse.setHeader("Verify-Token", jwtKaptchaHelper.createToken(createText));

            // 使用生成的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    /**
     * Token校验验证码
     */
    @DisableToken
    @ApiImplicitParams({
            @ApiImplicitParam(name = "verifyCode",       paramType = "body",value = "验证码结果",    required = true),
            @ApiImplicitParam(name = "verifyToken",      paramType = "body",value = "验证码Token",   required = true),
    })
    @PostMapping(value = "/verifyKaptcha")
    @ResponseBody
    public ServerResponse verifyKaptcha(String verifyCode, String verifyToken) {
        Claims claims = jwtKaptchaHelper.getTokenClaim(verifyToken);
        if(claims == null || jwtKaptchaHelper.isTokenExpired(claims.getExpiration())){
             return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.KAPTCHA_EXPIRED);
        }
        if (StringUtils.isEmpty(verifyToken) || JwtKaptchaHelper.TOKEN_SUCCESS != jwtKaptchaHelper.checkToken(verifyToken,verifyCode)) {
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.KAPTCHA_ERROR);
        }
        return ServerResponse.getInstance().ok().responseEnum(ResponseEnum.SUCCESS);
    }

}
