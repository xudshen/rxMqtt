package cn.com.chuanliu.diandi.rxMqtt.interfaces;

import org.eclipse.paho.client.mqttv3.MqttClientPersistence;

import cn.com.chuanliu.diandi.rxMqtt.enums.ClientType;
import cn.com.chuanliu.diandi.rxMqtt.exceptions.RxMqttException;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public interface IRxMqttClientFactory {
    public IRxMqttClient create(String host, Integer port, String clientId, Boolean useSSL,
                                ClientType type, MqttClientPersistence persistence)
            throws RxMqttException;
}
