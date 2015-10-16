package net.felsing.client_cert.utilities;


import org.apache.cxf.configuration.jsse.TLSClientParameters;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by cf on 25.08.15.
 *
 */

public class TLS_Utils {
    private static final TLSClientParameters tlsParams = new TLSClientParameters();
    private static volatile boolean isTlsSetForSSL = false;
    private static final Lock setTlsParamsLock = new ReentrantLock();


    // ----
    // Some other methods and things
    // ---
    public static final TLSClientParameters getTlsParams() {
        return tlsParams;
    }


    public static final void initializeConduitForSSL(Properties properties) throws Exception {

        String trustStoreFile = properties.getProperty("trustStoreFile");
        String keyStoreFile = properties.getProperty("keyStoreFile");
        TrustManagerFactory tmf = null;
        KeyManagerFactory kmf = null;

        // set truststore
        if (trustStoreFile!=null) {
            String trustStorePassword = properties.getProperty("trustStorePassword");
            KeyStore trustStore = KeyStore.getInstance(properties.getProperty("trustStoreType"));
            trustStore.load(new FileInputStream(trustStoreFile),
                    trustStorePassword.toCharArray());

            tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);
        }

        // set keystore
        if (keyStoreFile!=null) {
            String keyStorePassword = properties.getProperty("keyStorePassword");
            KeyStore keyStore = KeyStore.getInstance(properties.getProperty("keyStoreType"));
            keyStore.load(new FileInputStream(keyStoreFile),
                    keyStorePassword.toCharArray());

            kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, keyStorePassword.toCharArray());
        }

        // configure sslcontext
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        tlsParams.setSSLSocketFactory(sslContext.getSocketFactory());

    }
}
