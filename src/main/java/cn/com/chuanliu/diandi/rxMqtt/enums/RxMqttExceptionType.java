package cn.com.chuanliu.diandi.rxMqtt.enums;


/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public enum RxMqttExceptionType {
    CLIENT_INIT_ERROR(8568, "can not initial the client object"),
    CLIENT_NULL_ERROR(8569, "can not initial the client object"),
    SSL_CERT_INIT_ERROR(8570, "can not initial the ssl config");

    private int code;
    private String msg;

    RxMqttExceptionType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
