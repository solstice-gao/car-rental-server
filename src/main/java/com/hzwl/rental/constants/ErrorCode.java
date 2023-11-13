package com.hzwl.rental.constants;

import lombok.Getter;

/**
 * @Author GA666666
 * @Date 2023/8/09 21:42
 */
@Getter
public enum ErrorCode {

    TOKEN_EXPIRATION(403, "Token失效"),
    REGISTRATION_FAILED(50001, "注册失败"),
    VERIFY_CODE_FAILED(50002, "验证码错误"),
    TENNAT_PASS_NOT_BLANK(50003, "租户密码不能为空"),
    TENNAT_USERNAME_NOT_BLANK(50004, "租户用户名不能为空"),
    TENNAT_COMPANY_ID_NOT_BLANK(50005, "公司ID不能为空"),
    TENNAT_IS_BLANK(50006, "租户不存在"),
    VERIFY_PASSWORD_FAILED(50007, "密码错误"),
    NOT_FOUND_CAR_INFO(50008, "找不到车辆信息"),
    NOT_FOUND_CAR_STATUS(50009, "车辆状态有误信息"),
    INSERT_CAR_IMAGE_ERROR(50010, "新增车辆图片失败"),
    TENNAT_VERIFY_ERROR(50011, "租户校验失败"),
    INSERT_CAR_MODEL_ERROR(50012, "新增车辆模块失败"),
    IMAGE_UPLOAD_ERROR(50013, "图片上传失败"),
    SMS_CODE_SEND_ERROR(50014, "验证码发送失败"),
    CREATE_PRE_ORDER_ERROR(50015, "创建预付单异常"),
    NOT_FOUND_CAR_MODELS(50016, "找不到车辆型号信息"),
    NOT_FOUND_USER_ADRESS(50017, "找不到用户地址"),
    QUERY_ORDER_STATUS_PARAMS_ERROR(50018, "获取支付状态参数异常"),
    NOT_FOUND_ORDER(50019, "找不到订单信息"),
    QUERY_ORDER_STATUS_ERROR(50020, "获取支付状态异常"),
    QUERY_LOCATION_BY_IP_ERROR(50021, "获取地址信息状态异常"),
    NOT_FOUND_COUPON(50022, "未找到优惠券"),
    ALREADY_RECEIVED_COUPON(50023, "已经领取过该优惠券"),
    CAR_PRICE_MODELS_NOT_EQUALS(50024, "车辆型号与价格数量不匹配"),
    COMPANY_NAME_EXITES(50025, "公司名称已经存在，请更换"),
    ICON_NOT_EXIST(50026, "找不到公司图标，请重新上传"),
    LICENSE_NOT_EXIST(50027, "找不到公司营业执照，请重新上传"),


    PARAMS_REQUIRED(12000, "请求参数不能为空"),
    CHANCODE_REQUIRED(12001, "chanCode 不能为空"),
    ABILITYCODE_REQUIRED(12002, "abilityCode 不能为空"),
    TRANSACTIONID_REQUIRED(12003, "transactionId 不能为空"),
    TIMESTAMP_REQUIRED(12004, "timestamp 不能为空"),
    RANDOMSTR_REQUIRED(12005, "randomstr 不能为空"),
    SIGN_REQUIRED(12006, "sign 不能为空"),
    BODY_REQUIRED(12007, "body 不能为空"),
    SECRET_REQUIRED(12008, "secret 不能为空"),
    CHAN_AND_SECRET_VALIDATION_FAILED(30033, "渠道号和密钥验证失败"),
    SIGNATURE_ERROR(30034, "签名错误"),
    NO_INTERFACE_PERMISSION(30035, "无此接口权限"),
    INTERNAL_CALL_ERROR(40000, "内部调用错误"),
    BODY_PARAM_REQUIRED(40001, "body 内参数%s不能为空"),
    /**
     * Service
     */
    EQUITY_NOT_EXIST(60001, "权益记录不存在"),
    NOT_AUTO_RENEW(60001, "非自动订阅产品"),
    ;
    private int errorCode;
    private String errorMsg;

    ErrorCode(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public static ErrorCode findByErrorCode(int errorCode) {
        for (ErrorCode error : ErrorCode.values()) {
            if (error.errorCode == errorCode) {
                return error;
            }
        }
        return null; // 如果找不到对应的错误码
    }
}