package cn.com.chuanliu.diandi.rxMqtt.utils;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;

import java.io.Reader;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by xudshen@hotmail.com on 14-7-21.
 */
public class SSLUtils {
    public static SSLSocketFactory getSocketFactory(final Reader reader) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // load CA certificate
        PEMParser parser = new PEMParser(reader);
        X509CertificateHolder holder = (X509CertificateHolder) parser.readObject();
        parser.close();

        Certificate caCert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(holder);

        // CA certificate is used to authenticate server
        KeyStore caks = KeyStore.getInstance(KeyStore.getDefaultType());
        caks.load(null, null);
        caks.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caks);

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1.2");

        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }
}
