package cn.com.chuanliu.diandi.rxMqtt.impl;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.regex.Pattern;

import cn.com.chuanliu.diandi.rxMqtt.enums.RxMqttExceptionType;
import cn.com.chuanliu.diandi.rxMqtt.exceptions.RxMqttException;
import cn.com.chuanliu.diandi.rxMqtt.exceptions.RxMqttTokenException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public class RxMqttAsyncClient extends RxMqttClient {
    private MqttAsyncClient client;
    private Observable<RxMqttMessage> messageObservable;

    public RxMqttAsyncClient(String brokerUrl, String clientId, MqttClientPersistence persistence)
            throws RxMqttException {
        super();
        try {
            client = new MqttAsyncClient(brokerUrl, clientId, persistence);
        } catch (MqttException ex) {
            Log.e("at new", ex.getMessage());
            throw new RxMqttException(RxMqttExceptionType.CLIENT_INIT_ERROR);
        }
    }

    @Override
    public Observable<IMqttToken> connect() {
        final MqttConnectOptions conOpt = this.getConOpt();
        return Observable.create(new Observable.OnSubscribe<IMqttToken>() {
            @Override
            public void call(final Subscriber<? super IMqttToken> subscriber) {
                if (null == client) subscriber.onError(new
                        RxMqttException(RxMqttExceptionType.CLIENT_NULL_ERROR));

                try {
                    client.connect(conOpt, "Context", new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            subscriber.onNext(asyncActionToken);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            subscriber.onError(new RxMqttTokenException(exception, asyncActionToken));
                        }
                    });
                } catch (MqttException ex) {
                    subscriber.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<IMqttToken> publish(final String topic, final String msg) {
        final MqttMessage message = new MqttMessage();
        message.setQos(2);
        message.setPayload(msg.getBytes());

        return Observable.create(new Observable.OnSubscribe<IMqttToken>() {
            @Override
            public void call(final Subscriber<? super IMqttToken> subscriber) {
                if (null == client) subscriber.onError(new
                        RxMqttException(RxMqttExceptionType.CLIENT_NULL_ERROR));

                try {
                    client.publish(topic, message, "Context", new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            subscriber.onNext(asyncActionToken);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            subscriber.onError(new RxMqttTokenException(exception, asyncActionToken));
                        }
                    });
                } catch (MqttException ex) {
                    subscriber.onError(ex);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public Observable<IMqttToken> subscribeTopic(final String topic, final int qos) {
        return Observable.create(new Observable.OnSubscribe<IMqttToken>() {
            @Override
            public void call(final Subscriber<? super IMqttToken> subscriber) {
                if (null == client) subscriber.onError(new
                        RxMqttException(RxMqttExceptionType.CLIENT_NULL_ERROR));

                try {
                    client.subscribe(topic, qos, "Context", new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            subscriber.onNext(asyncActionToken);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            subscriber.onError(new RxMqttTokenException(exception, asyncActionToken));
                        }
                    });
                } catch (MqttException ex) {
                    subscriber.onError(ex);
                }
            }
        });
    }

    @Override
    public Observable<RxMqttMessage> subscribing(String regularExpression) {
        return subscribing(Pattern.compile(regularExpression));
    }

    @Override
    public Observable<RxMqttMessage> subscribing(final Pattern pattern) {
        return getMessageObservable().filter(new Func1<RxMqttMessage, Boolean>() {
            //        return subject.filter(new Func1<RxMqttMessage, Boolean>() {
            @Override
            public Boolean call(RxMqttMessage rxMqttMessage) {
                return pattern.matcher(rxMqttMessage.getTopic()).matches();
            }
        });
    }

    private Observable<RxMqttMessage> getMessageObservable() {
        if (null == messageObservable) {
            messageObservable = Observable.create(new Observable.OnSubscribe<RxMqttMessage>() {
                @Override
                public void call(final Subscriber<? super RxMqttMessage> subscriber) {
                    if (null == client) subscriber.onError(new
                            RxMqttException(RxMqttExceptionType.CLIENT_NULL_ERROR));

                    client.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {

                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) {
                            Log.d(Thread.currentThread().getName(),
                                    "[" + topic + "," + new String(message.getPayload())
                                            + "," + message.getQos() + "]"
                            );
                            subscriber.onNext(new RxMqttMessage(topic, message));
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });
                }
            });
        }
        return messageObservable;
    }
}
