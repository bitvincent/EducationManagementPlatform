package com.edu.education.config.Interceptor;

import com.edu.education.common.DisableToken;
import com.edu.education.helper.common.JwtTokenHelper;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private JwtTokenHelper jwtTokenHelper;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws SignatureException {

        /** 允许option请求通过 **/
        if(request.getMethod().toUpperCase().equals("OPTIONS")){
            return true;
        }

        /** 设置返回头跨域 **/
        //crossDomain(request, response);

        /** DisableToken注解检验 **/
        if (handler instanceof HandlerMethod){
            HandlerMethod method = (HandlerMethod) handler;
            DisableToken auth = method.getMethod().getAnnotation(DisableToken.class);
            System.out.println("TokenInterceptor:" +request.getRequestURI() +" || "+ method.getMethod());
            if(auth != null){
                return true;
            }
        }
        else {
            System.out.println("TokenInterceptor:" +request.getRequestURI());
        }

        /** 获取 Token */
        String token = request.getHeader(jwtTokenHelper.getHeader());
        if(StringUtils.isEmpty(token))
            token = request.getParameter(jwtTokenHelper.getHeader());

        if(StringUtils.isEmpty(token)){
            throw new SignatureException(jwtTokenHelper.getHeader()+ "不能为空");
        }

        /** Token 验证 */
        Claims claims = null;
        try{
            claims = jwtTokenHelper.getTokenClaim(token);
            if(claims == null || jwtTokenHelper.isTokenExpired(claims.getExpiration())){
                throw new SignatureException(jwtTokenHelper.getHeader() + "失效，请重新登录。");
            }
        }catch (Exception e){
            throw new SignatureException(jwtTokenHelper.getHeader() + "失效，请重新登录。");
        }


        /** 设置 username 用户名 */
        request.setAttribute("username", jwtTokenHelper.getTokenSubject(token));
        return true;
    }

    public void crossDomain(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization");
        response.setHeader("Access-Control-Expose-Headers", "Verify-Token");
    }
}
