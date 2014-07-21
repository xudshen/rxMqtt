package cn.com.chuanliu.diandi.rxMqtt.impl;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import cn.com.chuanliu.diandi.rxMqtt.enums.RxMqttExceptionType;
import cn.com.chuanliu.diandi.rxMqtt.exceptions.RxMqttException;
import cn.com.chuanliu.diandi.rxMqtt.interfaces.IRxMqttClient;
import cn.com.chuanliu.diandi.rxMqtt.utils.SSLUtils;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public abstract class RxMqttClient implements IRxMqttClient {
    private MqttConnectOptions conOpt;

    protected RxMqttClient() {
        this.conOpt = new MqttConnectOptions();
        this.configOtherOptions(true, 60, 60);
    }

    public MqttConnectOptions getConOpt() {
        return conOpt;
    }

    public void setConOpt(MqttConnectOptions conOpt) {
        this.conOpt = conOpt;
    }

    public void configOtherOptions(boolean cleanSession, int connectionTimeout,
                                   int keepAliveInterval) {
        conOpt.setCleanSession(cleanSession);
        conOpt.setConnectionTimeout(connectionTimeout);
        conOpt.setKeepAliveInterval(keepAliveInterval);
    }

    public void configUserPassword(String username, String password) {
        conOpt.setUserName(username);
        if (null != password)
            conOpt.setPassword(password.toCharArray());
    }

    public void configSSLCert(String certContent) throws RxMqttException {
        if (null == certContent)
            throw new RxMqttException(RxMqttExceptionType.SSL_CERT_INIT_ERROR);
        try {
            conOpt.setSocketFactory(SSLUtils.getSocketFactory(new InputStreamReader(
                    new ByteArrayInputStream(certContent.getBytes()))));
        } catch (Exception ex) {
            throw new RxMqttException(RxMqttExceptionType.SSL_CERT_INIT_ERROR, ex);
        }
    }
}
