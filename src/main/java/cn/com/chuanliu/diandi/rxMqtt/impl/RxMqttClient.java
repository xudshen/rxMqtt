package cn.com.chuanliu.diandi.rxMqtt.impl;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;

import cn.com.chuanliu.diandi.rxMqtt.enums.RxMqttClientState;
import cn.com.chuanliu.diandi.rxMqtt.enums.RxMqttExceptionType;
import cn.com.chuanliu.diandi.rxMqtt.exceptions.RxMqttException;
import cn.com.chuanliu.diandi.rxMqtt.interfaces.IRxMqttClient;
import cn.com.chuanliu.diandi.rxMqtt.utils.SSLUtils;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public abstract class RxMqttClient implements IRxMqttClient {
    private MqttConnectOptions conOpt;
    private RxMqttClientStatus status;
    private PublishSubject<RxMqttClientStatus> statusSubject;

    protected RxMqttClient() {
        this.conOpt = new MqttConnectOptions();
        this.configOtherOptions(true, 60, 300);
        //init status
        status = new RxMqttClientStatus();
        status.setLogTime(new Timestamp(System.currentTimeMillis()));
        status.setState(RxMqttClientState.Init);
        statusSubject = PublishSubject.create();
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

    public void updateState(RxMqttClientState state) {
        if (this.status.getState() != state) {
            this.status.setState(state);
            this.status.setLogTime(new Timestamp(System.currentTimeMillis()));
            statusSubject.onNext((RxMqttClientStatus) status.clone());
        }
    }

    @Override
    public Observable<RxMqttClientStatus> statusReport() {
        return statusSubject;
    }
}
