package com.bsg.api.util;

/**
 * Created by zhang on 2017/3/29.
 */
public class RespJsonFactory {

    public static RespJson buildSuccess(){
        return  buildInfo(RespJson.SUCCESS,null,null,null);
    }

    public static RespJson buildSuccess(String msg){
        return buildInfo(RespJson.SUCCESS,null,msg,null);
    }

    public static RespJson buildWarning(String msg) {
        return buildInfo(RespJson.WARNING,null, msg,  null);
    }

    public static  RespJson buildSuccess(Object data){
        return  buildInfo(RespJson.SUCCESS,null,null,data);
    }

    public static RespJson buildFailure(String msg) {
        return buildInfo(RespJson.FAIL, null,msg, null);
    }

    public static RespJson buildNotLogin() {
        return buildInfo(RespJson.NOLOGIN, null, null, null);
    }

    public static RespJson buildInfo(int result,Integer code,String msg,Object data){
        RespJson respJson = new RespJson();
        respJson.setResult(result);
        respJson.setCode(code);
        respJson.setMsg(msg);
        respJson.setData(data);
        return respJson;
    }
}
