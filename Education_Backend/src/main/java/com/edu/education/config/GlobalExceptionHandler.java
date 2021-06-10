package com.edu.education.config;

import com.edu.education.common.ResponseEnum;
import com.edu.education.common.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import java.security.SignatureException;

@RestControllerAdvice
//错误请求处理
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理@RequestParam 缺少参数异常
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public ServerResponse missingParameterExceptionHandler(HttpServletRequest req, MissingServletRequestParameterException e){
        logger.error("缺少参数异常！原因是:",e);
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.MISSING_PARAM).data(e.getMessage());
    }

    /**
     * 处理@RequestParam(对File文件) 缺少参数异常
     */
    @ExceptionHandler(value = MissingServletRequestPartException.class)
    @ResponseBody
    public ServerResponse missingPartExceptionHandler(HttpServletRequest req, MissingServletRequestPartException e){
        logger.error("缺少参数异常！原因是:",e);
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.MISSING_PARAM).data(e.getMessage());
    }

    /**
     *  处理@RequestParam 参数类型不匹配异常
     *  */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ServerResponse parameterTypeMismatchExceptionHandler(HttpServletRequest req, MethodArgumentTypeMismatchException e){
        logger.error("参数类型不匹配！原因是:",e);
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.UNMATCHED_PARAM).data(e.getMessage());
    }

    /**
     *  处理@Valid 注解类型不匹配异常
     *  */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ServerResponse BindExceptionHandler(HttpServletRequest req, BindException e){
        try {
            logger.error("参数为空！原因是:",e);
            //获取注解自定义错误信息
            String message = e.getBindingResult().getFieldError().getDefaultMessage();
            //用户名或密码为空
            if(message.contains("用户名") || message.contains("密码"))
                return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INVALID_PARAM);
            else
                return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INVALID_PARAM).data(message);
        }catch (NullPointerException nullexception){
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INTERNAL_ERROR).data(e.getMessage());
        }

    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ServerResponse exceptionHandler(HttpServletRequest req, NullPointerException e){
        logger.error("发生空指针异常！原因是:",e);
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.FAILED).data("空指针异常");
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public ServerResponse exceptionHandler(HttpServletRequest req, Exception e){
        logger.error("其他异常！原因是:",e);
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INTERNAL_ERROR).data(e.getMessage());
    }

    /**
     * 处理Token异常
     */
    @ExceptionHandler(value = { SignatureException.class })
    @ResponseBody
    public ServerResponse authorizationException(SignatureException e){
        logger.error("发生验证错误！原因是：",e);
        if(e.getMessage().contains("为空"))
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.TOKEN_ERROR);
        else if(e.getMessage().contains("失效"))
            return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.TOKEN_EXPIRED);
        return ServerResponse.getInstance().failed().responseEnum(ResponseEnum.INTERNAL_ERROR);
    }
}
