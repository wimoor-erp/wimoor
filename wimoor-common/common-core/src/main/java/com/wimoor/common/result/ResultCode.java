package com.wimoor.common.result;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
 
@AllArgsConstructor
@NoArgsConstructor
public enum ResultCode implements IResultCode, Serializable {

    SUCCESS(HttpStatus.SUCCESS, "一切ok"),

    USER_ERROR(HttpStatus.BAD_REQUEST, "用户端错误"),
    USER_LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "用户登录异常"),

    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "用户不存在"),
    USER_ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "用户账户被冻结"),
    USER_ACCOUNT_INVALID(HttpStatus.FORBIDDEN, "用户账户已作废"),

    USERNAME_OR_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, "用户名或密码错误"),
    PASSWORD_ENTER_EXCEED_LIMIT(HttpStatus.BAD_REQUEST, "用户输入密码次数超限"),
    CLIENT_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "客户端认证失败"),
    TOKEN_INVALID_OR_EXPIRED(HttpStatus.UNAUTHORIZED, "token无效或已过期"),
    TOKEN_ACCESS_FORBIDDEN(HttpStatus.UNAUTHORIZED, "token已被禁止访问"),

    AUTHORIZED_ERROR(HttpStatus.FORBIDDEN, "访问权限异常"),
    ACCESS_UNAUTHORIZED(HttpStatus.FORBIDDEN, "访问未授权"),
    FORBIDDEN_OPERATION(HttpStatus.FORBIDDEN, "演示环境禁止修改、删除重要数据，请本地部署后测试"),


    PARAM_ERROR(HttpStatus.BAD_REQUEST, "用户请求参数错误"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "请求资源不存在"),
    PARAM_IS_NULL(HttpStatus.BAD_REQUEST, "请求必填参数为空"),



    USER_UPLOAD_FILE_ERROR(HttpStatus.BAD_REQUEST, "用户上传文件异常"),
    USER_UPLOAD_FILE_TYPE_NOT_MATCH(HttpStatus.BAD_REQUEST, "用户上传文件类型不匹配"),
    USER_UPLOAD_FILE_SIZE_EXCEEDS(HttpStatus.BAD_REQUEST, "用户上传文件太大"),
    USER_UPLOAD_IMAGE_SIZE_EXCEEDS(HttpStatus.BAD_REQUEST, "用户上传图片太大"),

    SYSTEM_EXECUTION_ERROR(HttpStatus.BAD_REQUEST, "系统执行出错"),
    SYSTEM_EXECUTION_TIMEOUT(HttpStatus.BAD_REQUEST, "系统执行超时"),
    SYSTEM_ORDER_PROCESSING_TIMEOUT(HttpStatus.BAD_REQUEST, "系统订单处理超时"),

    SYSTEM_DISASTER_RECOVERY_TRIGGER(HttpStatus.FORBIDDEN, "系统容灾功能被出发"),
    FLOW_LIMITING(HttpStatus.BAD_REQUEST, "系统限流"),
    DEGRADATION(HttpStatus.BAD_REQUEST, "系统功能降级"),

    SYSTEM_RESOURCE_ERROR(HttpStatus.BAD_REQUEST, "系统资源异常"),
    SYSTEM_RESOURCE_EXHAUSTION(HttpStatus.BAD_REQUEST, "系统资源耗尽"),
    SYSTEM_RESOURCE_ACCESS_ERROR(HttpStatus.BAD_REQUEST, "系统资源访问异常"),
    SYSTEM_READ_DISK_FILE_ERROR(HttpStatus.BAD_REQUEST, "系统读取磁盘文件失败"),

    CALL_THIRD_PARTY_SERVICE_ERROR(HttpStatus.BAD_REQUEST, "调用第三方服务出错"),
    MIDDLEWARE_SERVICE_ERROR(HttpStatus.BAD_REQUEST, "中间件服务出错"),
    INTERFACE_NOT_EXIST(HttpStatus.NOT_FOUND, "接口不存在"),

    MESSAGE_SERVICE_ERROR(HttpStatus.BAD_REQUEST, "消息服务出错"),
    MESSAGE_DELIVERY_ERROR(HttpStatus.BAD_REQUEST, "消息投递出错"),
    MESSAGE_CONSUMPTION_ERROR(HttpStatus.BAD_REQUEST, "消息消费出错"),
    MESSAGE_SUBSCRIPTION_ERROR(HttpStatus.BAD_REQUEST, "消息订阅出错"),
    MESSAGE_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "消息分组未查到"),



    DATABASE_ERROR(HttpStatus.BAD_REQUEST, "数据库服务出错"),
    DATABASE_TABLE_NOT_EXIST(HttpStatus.NOT_FOUND, "表不存在"),
    DATABASE_COLUMN_NOT_EXIST(HttpStatus.NOT_FOUND, "列不存在"),
    DATABASE_DUPLICATE_COLUMN_NAME(HttpStatus.CONFLICT, "多表关联中存在多个相同名称的列"),
    DATABASE_DEADLOCK(HttpStatus.CONFLICT, "数据库死锁"),
    DATABASE_PRIMARY_KEY_CONFLICT(HttpStatus.CONFLICT, "主键冲突"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"Token超时"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Token无效"),
    TOKEN_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "token签名无效"),
    UNKNOWN_ERROR(HttpStatus.NOT_FOUND, "未知错误"),
    GENERATE_CODE_JDBC_ERROR(HttpStatus.NOT_FOUND, "数据库表生成错误");


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    private int code;

    private String msg;

    @Override
    public String toString() {
        return "{" +
                "\"code\":\"" + code + '\"' +
                ", \"msg\":\"" + msg + '\"' +
                '}';
    }


    public static ResultCode getValue(int code){
        for (ResultCode value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return SYSTEM_EXECUTION_ERROR; // 默认系统执行错误
    }
}
