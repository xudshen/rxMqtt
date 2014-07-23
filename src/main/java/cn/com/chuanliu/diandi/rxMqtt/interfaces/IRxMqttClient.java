package cn.com.chuanliu.diandi.rxMqtt.interfaces;

import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.regex.Pattern;

import cn.com.chuanliu.diandi.rxMqtt.impl.RxMqttClientStatus;
import cn.com.chuanliu.diandi.rxMqtt.impl.RxMqttMessage;
import rx.Observable;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public interface IRxMqttClient {

    public Observable<IMqttToken> connect();

    public Observable<IMqttToken> disConnect();

    public Observable<IMqttToken> subscribeTopic(String topic, int qos);

    public Observable<RxMqttMessage> subscribing(String regularExpression);

    public Observable<RxMqttMessage> subscribing(Pattern pattern);

    public Observable<IMqttToken> publish(String topic, String msg);

    public Observable<RxMqttClientStatus> statusReport();
}
