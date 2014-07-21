package cn.com.chuanliu.diandi.rxMqtt.impl;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import cn.com.chuanliu.diandi.rxMqtt.interfaces.IRxMqttMessage;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public class RxMqttMessage implements IRxMqttMessage {
    private String topic;
    private MqttMessage rxMessage;

    public RxMqttMessage(String topic, MqttMessage rxMessage) {
        this.topic = topic;
        this.rxMessage = rxMessage;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getMessage() {
        return null == rxMessage ? null : new String(rxMessage.getPayload());
    }

    @Override
    public int getQos() {
        return null == rxMessage ? -1 : rxMessage.getQos();
    }

    @Override
    public String toString() {
        return String.format("topic: %s; msg: %s; qos: %d", getTopic(), getMessage(), getQos());
    }
}
