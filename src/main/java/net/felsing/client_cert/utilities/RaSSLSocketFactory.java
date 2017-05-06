package net.felsing.client_cert.utilities;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RaSSLSocketFactory extends SSLSocketFactory {
    private static final Logger logger = LoggerFactory.getLogger(RaSSLSocketFactory.class);

    private SSLSocketFactory socketFactory;

    private RaSSLSocketFactory() {
        String trustStoreFile = PropertyLoader.getProperties().getProperty(Constants.ldapTruststoreFile);
        String trustStorePassword = PropertyLoader.getProperties().getProperty(Constants.ldapTruststorePassword);

        if ((trustStoreFile!=null) && (trustStorePassword!=null)) {
            logger.debug("Using " + trustStoreFile);
            TrustManagerFactory tmf;
            try {

                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

                trustStore.load(new FileInputStream(trustStoreFile),
                        trustStorePassword.toCharArray());

                tmf = TrustManagerFactory.getInstance("SunX509");
                tmf.init(trustStore);

                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
                socketFactory = sslContext.getSocketFactory();
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            /* handle exception */
            }
        } else {
            logger.error ("Properties not set. You must set " + Constants.ldapTruststoreFile + " and " + Constants.ldapTruststorePassword);
        }
    }

    public static SocketFactory getDefault() {
        return new RaSSLSocketFactory();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return socketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return socketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String string, int i, boolean bln) throws IOException {
        return socketFactory.createSocket(socket, string, i, bln);
    }

    @Override
    public Socket createSocket(String string, int i) throws IOException {
        return socketFactory.createSocket(string, i);
    }

    @Override
    public Socket createSocket(String string, int i, InetAddress ia, int i1) throws IOException {
        return socketFactory.createSocket(string, i, ia, i1);
    }

    @Override
    public Socket createSocket(InetAddress ia, int i) throws IOException {
        return socketFactory.createSocket(ia, i);
    }

    @Override
    public Socket createSocket(InetAddress ia, int i, InetAddress ia1, int i1) throws IOException {
        return socketFactory.createSocket(ia, i, ia1, i1);
    }
} // class
