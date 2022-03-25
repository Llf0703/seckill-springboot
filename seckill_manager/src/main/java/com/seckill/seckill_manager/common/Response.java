package com.seckill.seckill_manager.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

interface Code {
    int SUCCESS = 200;
    int AUTH_ERR = 401;
    int SYSTEM_ERR = 500;
    int DATA_NOT_FOUND = 404;
    int PARAMS_ERR = 400;
    int DATA_ERR = 402;
}

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName Response
 * @description: 接口统一返回格式封装
 * @date 2022/3/18 23:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private int code; //业务状态码
    private Boolean status;//是否成功执行业务
    private Object data;//数据
    private String message;//信息
    private String dateTime;//完成时间
    private long timeStamp;//完成时间

    /*
     * @MethodName success
     * @author Wky1742095859
     * @Description 无数据成功返回
     * @Date 2022/3/18 23:32
     * @Param [msg]
     * @Return com.example.seckill_guest.common.Response
     **/
    public static Response success(String msg) {
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Response(Code.SUCCESS, true, null, msg, sdf.format(nowTime), nowTime.getTime());
    }

    /*
     * @MethodName success
     * @author Wky1742095859
     * @Description 有数据成功返回
     * @Date 2022/3/18 23:35
     * @Param [data, msg]
     * @Return com.example.seckill_guest.common.Response
     **/
    public static Response success(Object data, String msg) {
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Response(Code.SUCCESS, true, data, msg, sdf.format(nowTime), nowTime.getTime());
    }

    /*
     * @MethodName authErr
     * @author Wky1742095859
     * @Description 鉴权失败返回
     * @Date 2022/3/18 23:36
     * @Param [msg]
     * @Return com.example.seckill_guest.common.Response
     **/
    public static Response authErr(String msg) {
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Response(Code.AUTH_ERR, false, null, msg, sdf.format(nowTime), nowTime.getTime());
    }

    /*
     * @MethodName systemErr
     * @author Wky1742095859
     * @Description 系统,业务处理异常返回
     * @Date 2022/3/18 23:37
     * @Param [msg]
     * @Return com.example.seckill_guest.common.Response
     **/
    public static Response systemErr(String msg) {
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Response(Code.SYSTEM_ERR, false, null, msg, sdf.format(nowTime), nowTime.getTime());
    }

    /*
     * @MethodName paramsErr
     * @author Wky1742095859
     * @Description 参数异常返回
     * @Date 2022/3/19 15:54
     * @Param [msg]
     * @Return com.example.seckill_manager.common.Response
     **/
    public static Response paramsErr(String msg) {
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Response(Code.PARAMS_ERR, false, null, msg, sdf.format(nowTime), nowTime.getTime());
    }

    /*
     * @MethodName dataErr
     * @author Wky1742095859
     * @Description 数据库,redis异常返回
     * @Date 2022/3/25 2:32
     * @Param [msg]
     * @Return com.seckill.seckill_manager.common.Response
     **/
    public static Response dataErr(String msg) {
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Response(Code.DATA_ERR, false, null, msg, sdf.format(nowTime), nowTime.getTime());
    }

    /*
     * @MethodName dataNotFoundErr
     * @author Wky1742095859
     * @Description 查询数据不存在返回
     * @Date 2022/3/25 2:32
     * @Param [msg]
     * @Return com.seckill.seckill_manager.common.Response
     **/
    public static Response dataNotFoundErr(String msg) {
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Response(Code.DATA_NOT_FOUND, false, null, msg, sdf.format(nowTime), nowTime.getTime());
    }
}
