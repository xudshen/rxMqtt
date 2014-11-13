package cn.com.chuanliu.diandi.rxMqtt.impl;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import cn.com.chuanliu.diandi.rxMqtt.interfaces.IRxMqttMessage;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public class RxMqttMessage implements IRxMqttMessage {
    private String topic;
    private MqttMessage rxMessage;
    private String decrypted_payload = null;

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
        return null == rxMessage ? null : (decrypted_payload != null ? decrypted_payload : new String(rxMessage.getPayload()));
    }

    public String decrypt(Cipher dcipher, SecretKey key) {
        try {
            byte[] payload = rxMessage.getPayload();
            dcipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Arrays.copyOfRange(payload, 0, 16)));
            decrypted_payload = new String(dcipher.doFinal(Arrays.copyOfRange(payload, 16, payload.length)));
            return decrypted_payload;
        } catch (Exception e) {
            return null;
        }
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
