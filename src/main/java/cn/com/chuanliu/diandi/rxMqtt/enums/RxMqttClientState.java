package cn.com.chuanliu.diandi.rxMqtt.enums;

/**
 * Created by xudshen@hotmail.com on 14-7-23.
 */
public enum RxMqttClientState {
    Init(0),
    Connecting(1),
    ConnectingFailed(2),
    Connected(3),
    ConnectionLost(4),
    TryDisconnect(5),
    Disconnected(6);

    private int code;

    RxMqttClientState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
