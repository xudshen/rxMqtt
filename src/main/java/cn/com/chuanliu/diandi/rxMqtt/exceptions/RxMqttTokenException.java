package cn.com.chuanliu.diandi.rxMqtt.exceptions;

import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public class RxMqttTokenException extends Throwable {
    private IMqttToken mqttToken;

    public RxMqttTokenException(Throwable cause, IMqttToken mqttToken) {
        super(cause);
        this.mqttToken = mqttToken;
    }

    public IMqttToken getMqttToken() {
        return mqttToken;
    }

    public void setMqttToken(IMqttToken mqttToken) {
        this.mqttToken = mqttToken;
    }
}
