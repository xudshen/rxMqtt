package cn.com.chuanliu.diandi.rxMqtt;

import android.test.InstrumentationTestCase;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttToken;

import cn.com.chuanliu.diandi.rxMqtt.enums.ClientType;
import cn.com.chuanliu.diandi.rxMqtt.exceptions.RxMqttException;
import cn.com.chuanliu.diandi.rxMqtt.impl.RxMqttAsyncClient;
import cn.com.chuanliu.diandi.rxMqtt.impl.RxMqttClientFactory;
import cn.com.chuanliu.diandi.rxMqtt.impl.RxMqttClientStatus;
import cn.com.chuanliu.diandi.rxMqtt.impl.RxMqttMessage;
import rx.Observer;
import rx.functions.Action1;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public class RxMqttAsyncClientTest extends InstrumentationTestCase {
    public void test() throws InterruptedException {
        final String certContent = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDoTCCAomgAwIBAgIJANogtWGR/PToMA0GCSqGSIb3DQEBBQUAMGcxCzAJBgNV\n" +
                "BAYTAkNOMREwDwYDVQQIDAhTaGFuZ2hhaTEPMA0GA1UECgwGU2VsZkNBMRMwEQYD\n" +
                "VQQDDApzZWxmY2EuY29tMR8wHQYJKoZIhvcNAQkBFhBhZG1pbkBzZWxmY2EuY29t\n" +
                "MB4XDTE0MDUxNjA1MjAzMloXDTI0MDUxMzA1MjAzMlowZzELMAkGA1UEBhMCQ04x\n" +
                "ETAPBgNVBAgMCFNoYW5naGFpMQ8wDQYDVQQKDAZTZWxmQ0ExEzARBgNVBAMMCnNl\n" +
                "bGZjYS5jb20xHzAdBgkqhkiG9w0BCQEWEGFkbWluQHNlbGZjYS5jb20wggEiMA0G\n" +
                "CSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDqJJvnTpsWXvPfViXiG7hkAo5W8AsF\n" +
                "uCwcG+jz1q00953STxGhOllfLXdgwOAnM7V5fpMFoncMO2HTgjLrWlCrY6+ALtV0\n" +
                "mBrosD0mNLH5u87D5QldXPQytg0zvu/lAExvPWkPgSjtT6utxyoKmLtm6PxAy2PP\n" +
                "JEbBHWJjutKS+5cLmUv0mRYqXA0wDp2qz0swwdgNUX3fRiTOXCe7FEJcxgwN3sJ8\n" +
                "uA+Ihg1Wu8xN+U6VmSixGKHEM7apeuTrJWQEp7eCiIi2KAYybiKEuei75di6dwGF\n" +
                "YdxpJJivOlhLouVlLo226CWyM0i4RuHK9dwU8aKdpdHojQgAhb0DO523AgMBAAGj\n" +
                "UDBOMB0GA1UdDgQWBBR7CGA/scB+cwOrv3Qmk4Ds50hoBDAfBgNVHSMEGDAWgBR7\n" +
                "CGA/scB+cwOrv3Qmk4Ds50hoBDAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUA\n" +
                "A4IBAQC8Z6fcFGiM9q4VcI6Ismt8HTOX0WgJRIwueDjxlVHq7aTVQg4OCgwpp++o\n" +
                "TYagtwkaVAn1+tdr06zomRgLFzbf1EKcHV16ZDDYJ8vy4PMiiQ+KcNMOJt4r6twC\n" +
                "PhHHv+yJVvJA5mDRz1yc141wuKRji6sRlReMH1bpuA7jTQlstTGq1Kmpu0LySFA8\n" +
                "iMckFSSreyw46NP84dOBNSDMQJQSKKK7i7sZfuVRI1Kc3aRXLkytdfe6XRefuCmK\n" +
                "BdyPHCutbD/LeNgHqZ29zRuoKg6pPTwXqQJYyuJaxIpJLKvGD7+O8/4qz7npzJRz\n" +
                "81JLzU6jcRYX7kKVtXAAAeq7X2X0\n" +
                "-----END CERTIFICATE-----\n";

        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final RxMqttAsyncClient client = (RxMqttAsyncClient) (new RxMqttClientFactory()).create(
                            "192.168.1.157", 8883, "id", true, ClientType.Async, null);
                    client.configSSLCert(certContent);
                    client.configUserPassword("b81d036a-801a-4e62-8b93-75f4c5ca9262", "chuanliu");
                    client.connect().subscribe(new Observer<IMqttToken>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(Thread.currentThread().getName(), "connected failed");
                        }

                        @Override
                        public void onNext(IMqttToken iMqttToken) {
                            Log.d(Thread.currentThread().getName(), "connected");
                            client.subscribeTopic("hehe", 2).subscribe(new Observer<IMqttToken>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d(Thread.currentThread().getName(), "subscribed failed");
                                }

                                @Override
                                public void onNext(IMqttToken iMqttToken) {
                                    MqttToken token = (MqttToken) iMqttToken;
                                    Log.d(Thread.currentThread().getName(),
                                            "subscribed" + token.getTopics()[0]);

                                    client.publish("hehe", "wocao").subscribe(new Action1<IMqttToken>() {
                                        @Override
                                        public void call(IMqttToken iMqttToken) {
                                            Log.d(Thread.currentThread().getName(), "published");
                                        }
                                    });

                                }
                            });
                        }
                    });

                    client.subscribing("hehe").subscribe(new Action1<RxMqttMessage>() {
                        @Override
                        public void call(RxMqttMessage rxMqttMessage) {
                            Log.d(Thread.currentThread().getName(), rxMqttMessage.toString());
                        }
                    });
                    client.statusReport().subscribe(new Action1<RxMqttClientStatus>() {
                        @Override
                        public void call(RxMqttClientStatus rxMqttClientStatus) {
                            Log.d(Thread.currentThread().getName(), rxMqttClientStatus.toString());
                        }
                    });

                } catch (RxMqttException ex) {

                }
            }
        })).start();
        Thread.sleep(1000000L);
    }
}
