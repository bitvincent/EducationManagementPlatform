package com.edu.education.common;

public enum ResponseEnum {
    SUCCESS(1000,"成功"),
    FAILED(1001,"失败"),

    INTERNAL_ERROR(2001, "内部错误"),
    LOGIN_SUCCESS(2002, "登录成功"),
    LOGIN_FAILED(2003, "密码错误"),
    ACCOUNT_NOT_FOUND(2004, "账号不存在，请先注册"),
    ACCOUT_NOT_LOGIN(2005, "账号未登录"),
    ACCOUT_TYPE_WRONG(2006, "账号类型错误"),
    ACCOUT_BOTH(2007,"账号不能同时为老师和学生"),

    REGISTER_SUCCESS(3000, "注册成功"),
    REGISTER_FAILED(3001, "注册失败"),
    INVALID_PARAM(3002, "参数非法"),
    USERNAME_EXIST(3003, "用户名已存在"),
    UPDATE_PASSWORD_FAILED(3004, "修改失败，原密码错误"),
    MISSING_PARAM(3005, "参数缺失"),
    UNMATCHED_PARAM(3006, "参数类型不匹配"),

    TOKEN_EXPIRED(4001, "Token失效"),
    TOKEN_ERROR(4002, "Token不能为空"),
    KAPTCHA_EXPIRED(4003, "验证码失效"),
    KAPTCHA_ERROR(4004, "验证码错误"),
    SMS_EXPIRED(4005, "手机验证码失效"),
    SMS_ERROR(4006, "手机验证码错误"),
    SIGN_ERROR(4007, "签名错误"),

    GET_SUCCESS(20000, "数据获取成功"),
    SAVE_SUCCESS(20001, "数据上传成功"),
    SAVE_FAILED(20002, "数据上传失败"),
    DATA_NOT_EXIST(20003, "数据不存在"),
    DATA_NOT_MATCH(20004,"数据不匹配"),

    SAVE_IN_BLOCKCHAIN_FAILED(30001,"数据上链失败"),
    SAVE_FILE_FAILED(30002,"文件保存失败"),
    FILE_ALREADY_EXIST(30003,"文件已存在"),

    CLASS_ALREADY_SELECT(30004,"您已选课"),
    HOMEWORK_ALREADY_SUBMIT(30005,"您已提交作业"),
    HOMEWORK_ALREADY_MARK(30006,"您已批改作业"),
    GRADE_ALREADY_SET(30007,"您已打分"),
    ;

    Integer code;		//响应码
    String message;		//响应信息

    ResponseEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}