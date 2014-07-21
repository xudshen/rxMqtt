package cn.com.chuanliu.diandi.rxMqtt.enums;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public enum ClientType {
    Async(0),
    Wait(1);

    private int code;

    ClientType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
