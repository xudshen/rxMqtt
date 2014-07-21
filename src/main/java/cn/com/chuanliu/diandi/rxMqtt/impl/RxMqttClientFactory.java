package cn.com.chuanliu.diandi.rxMqtt.impl;

import org.eclipse.paho.client.mqttv3.MqttClientPersistence;

import cn.com.chuanliu.diandi.rxMqtt.constants.Constants;
import cn.com.chuanliu.diandi.rxMqtt.enums.ClientType;
import cn.com.chuanliu.diandi.rxMqtt.exceptions.RxMqttException;
import cn.com.chuanliu.diandi.rxMqtt.interfaces.IRxMqttClient;
import cn.com.chuanliu.diandi.rxMqtt.interfaces.IRxMqttClientFactory;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public class RxMqttClientFactory implements IRxMqttClientFactory {
    @Override
    public IRxMqttClient create(String host, Integer port, String clientId, Boolean useSSL,
                                ClientType type, MqttClientPersistence persistence)
            throws RxMqttException {
        IRxMqttClient client = null;
        String brokerUrl = getBrokerUrl(host, port, useSSL);
        switch (type) {
            case Async: {
                client = new RxMqttAsyncClient(brokerUrl, clientId, persistence);
                break;
            }
            case Wait: {
                break;
            }
        }
        return client;
    }

    private String getBrokerUrl(String host, int port, Boolean useSSL) {
        return String.format("%s://%s:%d", useSSL ? Constants.SSL : Constants.TCP, host, port);
    }
}
