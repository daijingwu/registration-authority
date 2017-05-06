/*
 * Copyright (c) 2016. by Christian Felsing
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.felsing.client_cert.utilities;


import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;


class TLS_Utils {
    private static final Logger logger = LoggerFactory.getLogger(TLS_Utils.class);
    private static final TLSClientParameters tlsParams = new TLSClientParameters();


    static TLSClientParameters getTlsParams() {
        return tlsParams;
    }


    static void initializeConduitForSSL(Properties properties) throws Exception {
        final String keyManagerType = "SunX509";
        final String tlsVersion = properties.getProperty(Constants.ejbcaTlsProtocol);

        final String trustStoreFile = properties.getProperty(Constants.ejbcaTrustStoreFile);
        final String trustStorePassword = properties.getProperty(Constants.ejbcaTrustStorePassword);

        final String keyStoreType = properties.getProperty(Constants.ejbcaKeyStoreType);
        final String keyStoreFile = properties.getProperty(Constants.ejbcaKeyStoreFile);
        final String keyStorePassword = properties.getProperty(Constants.ejbcaKeyStorePassword);

        TrustManagerFactory tmf = null;
        KeyManagerFactory kmf = null;

        // set truststore
        if (trustStoreFile!=null) {
            KeyStore trustStore = KeyStore.getInstance(properties.getProperty(Constants.ejbcaTrustStoreType));

            trustStore.load(new FileInputStream(trustStoreFile),
                    trustStorePassword.toCharArray());

            tmf = TrustManagerFactory.getInstance(keyManagerType);
            tmf.init(trustStore);
        }

        // set keystore
        if (keyStoreFile!=null) {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(new FileInputStream(keyStoreFile),
                    keyStorePassword.toCharArray());

            kmf = KeyManagerFactory.getInstance(keyManagerType);
            kmf.init(keyStore, keyStorePassword.toCharArray());
        }

        // configure sslcontext
        SSLContext sslContext = SSLContext.getInstance(tlsVersion);
        if (kmf!=null && tmf!=null) {
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        } else {
            logger.error("initializeConduitForSSL: KeyManagerFactory or TrustManagerFactory is null");
        }
        tlsParams.setSSLSocketFactory(sslContext.getSocketFactory());
    }

} // class
