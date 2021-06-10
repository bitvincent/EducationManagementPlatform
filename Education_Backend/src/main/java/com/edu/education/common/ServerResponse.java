package com.edu.education.common;

/**
 * @ Description：统一返回类
 * @Version: 1.0
 */
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ServerResponse {
    Integer status;      //成功 or 失败
    Integer code;       //响应状态码
    String message;			//响应信息
    Object data;		//响应数据

    public static  ServerResponse getInstance() {
        return new ServerResponse();
    }

    public  ServerResponse code(Integer code){
        this.code = code;
        return (ServerResponse) this;
    }

    public  ServerResponse message(String message){
        this.message = message;
        return (ServerResponse) this;
    }

    public  ServerResponse data(Object data){
        this.data = data;
        return (ServerResponse) this;
    }

    public  ServerResponse responseEnum(ResponseEnum responseEnum){
        this.code = responseEnum.code;
        this.message = responseEnum.message;
        return (ServerResponse) this;
    }

    public ServerResponse ok(){
        this.status = 0;
        return (ServerResponse) this;
    }

    public ServerResponse failed(){
        this.status = 1;
        return (ServerResponse) this;
    }

    @Override
    public String toString() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", this.code);
        resultJson.put("message", this.message);
        resultJson.put("data", this.data);
        return resultJson.toString();
    }

    public Integer getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //测试
    public static void main(String[] args) {
    }
}

