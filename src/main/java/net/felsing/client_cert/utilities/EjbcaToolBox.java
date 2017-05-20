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

import com.google.gson.JsonObject;
import net.felsing.client_cert.ejbca.*;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.MessageTrustDecider;
import org.apache.cxf.transport.http.URLConnectionInfo;
import org.apache.cxf.transport.http.UntrustedURLConnectionIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EjbcaToolBox {
    private static final Logger logger = LoggerFactory.getLogger(EjbcaToolBox.class);
    private static final TLSClientParameters tlsParams = new TLSClientParameters();
    private final EjbcaWS port;
    private final Properties properties;
    private static final QName SERVICE_NAME = new QName("http://ws.protocol.core.ejbca.org/", "EjbcaWSService");

    @SuppressWarnings("unused WeakerAccess")
    public final static int entNew = 10;
    @SuppressWarnings("unused WeakerAccess")
    public final static int entFailed = 11;
    @SuppressWarnings("unused WeakerAccess")
    public final static int entInitialized = 20;
    @SuppressWarnings("unused WeakerAccess")
    public final static int entInprocess = 30;
    @SuppressWarnings("unused WeakerAccess")
    public final static int entGenerated = 50;


    public EjbcaToolBox(Properties properties) {
        this.properties = properties;

        // proxy stuff
        String proxyHost = properties.getProperty(Constants.proxyHost, null);
        String proxyPort = properties.getProperty(Constants.proxyPort, null);
        if ((proxyHost != null) && (proxyPort != null)) {
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", proxyPort);
        }
        String proxyUser = properties.getProperty(Constants.proxyUser, null);
        String proxyPassword = properties.getProperty(Constants.proxyPassword, null);
        if (proxyUser != null) {
            System.setProperty("http.proxyUser", proxyUser);
        }
        if (proxyPassword != null) {
            System.setProperty("http.proxyPassword", proxyPassword);
        }

        URL wsdlURL = EjbcaWSService.WSDL_LOCATION;
        try {
            URL newWsdlURL = new URL(properties.getProperty("wsdlLocationUrl"));
            File wsdlFile = new File(newWsdlURL.getFile());
            if (!wsdlFile.canRead())
                throw new FileNotFoundException("File " + wsdlFile.getAbsolutePath() + " not found");
            wsdlURL = newWsdlURL;
        } catch (MalformedURLException e) {
            logger.warn("Not an URL: " + e.getMessage());
        } catch (FileNotFoundException e) {
            logger.warn("WSDL not found on specified location: " + e.getMessage());
        }
        EjbcaWSService ss = new EjbcaWSService(wsdlURL, SERVICE_NAME);
        port = ss.getEjbcaWSPort();

        Client client = ClientProxy.getClient(port);
        HTTPConduit http = (HTTPConduit) client.getConduit();
        try {
            initializeConduitForSSL(properties);
            if (properties.getProperty("debug.initializeTrustDecider") != null) {
                initializeTrustDecider(http);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        http.setTlsClientParameters(tlsParams);
    }


    private static void initializeConduitForSSL(Properties properties) throws Exception {
        final String tlsVersion = properties.getProperty(Constants.ejbcaTlsProtocol);

        // set truststore
        TrustManager[] trustManagers = PropertyLoader.getTrustManagers();

        // set keystore
        KeyManager[] keyManagers = PropertyLoader.getKeyManagers();

        // configure sslcontext
        SSLContext sslContext = SSLContext.getInstance(tlsVersion);
        sslContext.init(keyManagers, trustManagers, new SecureRandom());
        tlsParams.setSSLSocketFactory(sslContext.getSocketFactory());
    }


    // used for debugging only
    private void initializeTrustDecider(HTTPConduit httpConduit) {
        MessageTrustDecider messageTrustDecider = new MessageTrustDecider() {
            @Override
            public void establishTrust(String s, URLConnectionInfo urlConnectionInfo, Message message) throws UntrustedURLConnectionIOException {
                logger.debug(s);
                logger.debug(urlConnectionInfo.getURI().toASCIIString());
                logger.debug(message.toString());
            }
        };

        httpConduit.setTrustDecider(messageTrustDecider);
    }


    JsonObject getAvailableCas() throws AuthorizationDeniedException_Exception, EjbcaException_Exception {
        JsonObject jsonObject = new JsonObject();
        try {
            java.util.List<NameAndId> _getAvailableCAs__return = port.getAvailableCAs();
            _getAvailableCAs__return.forEach((k) -> {
                int id = k.getId();
                String name = k.getName();
                jsonObject.addProperty(Integer.toString(id), name);
            });

        } catch (AuthorizationDeniedException_Exception e) {
            logger.warn("Expected exception: AuthorizationDeniedException has occurred: " + e.getMessage());
            throw e;
        } catch (EjbcaException_Exception e) {
            logger.warn("Expected exception: EjbcaException has occurred: " + e.getMessage());
            throw e;
        }

        return jsonObject;
    }


    JsonObject ejbcaFindUser(String username) {

        UserMatch userMatch = new UserMatch();
        userMatch.setMatchtype(0);
        userMatch.setMatchvalue(username);
        userMatch.setMatchwith(0);

        JsonObject jsonObject = new JsonObject();
        int count;
        try {
            List<UserDataVOWS> found = port.findUser(userMatch);
            count = found.size();
            jsonObject.addProperty("found", count);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("result", "failed");
        }

        return jsonObject;
    }


    @SuppressWarnings("all")
    boolean ejbcaEditUser(Map<String, Object> user) {
        final String caName = "caName";
        final String username = "username";
        final String password = "password";
        final String email = "email";
        final String tokenType = "USERGENERATED";

        final StringBuilder subject = new StringBuilder();

        boolean success = false;

        StringBuilder ejbcaSubjectAlternativeName = new StringBuilder();
        Pattern r = Pattern.compile("^attr\\.(.*)");
        user.forEach((k, v) -> {
            Matcher m = r.matcher(k);
            if (m.find()) {
                String attr = m.group(1);
                if (v instanceof String) {
                    if (subject.length() > 0) subject.append(",");
                    subject.append(attr).append("=").append(v);
                } else if (k.equals("attr.san") && (v instanceof HashMap)) {
                    ((Map<String, ArrayList<String>>) v).forEach((kSan, vSan) -> {
                        vSan.forEach((entry) -> {
                            if (ejbcaSubjectAlternativeName.length() > 0) ejbcaSubjectAlternativeName.append(",");
                            ejbcaSubjectAlternativeName.append(kSan).append("=").append(entry);
                        });
                    });
                }
            }
        });

        UserDataVOWS userDataVOWS = new UserDataVOWS();
        userDataVOWS.setCaName(properties.getProperty(caName));
        userDataVOWS.setCertificateProfileName(properties.getProperty(Constants.propertyCertificateProfileName));
        userDataVOWS.setEmail((String) user.get(email));
        userDataVOWS.setEndEntityProfileName(properties.getProperty(Constants.propertyEndEntityProfileName));
        userDataVOWS.setUsername((String) user.get(username));
        userDataVOWS.setPassword((String) user.get(password));
        userDataVOWS.setStatus(entNew);
        userDataVOWS.setSubjectAltName(ejbcaSubjectAlternativeName.toString());
        userDataVOWS.setSubjectDN(subject.toString());
        userDataVOWS.setTokenType(tokenType);

        try {
            port.editUser(userDataVOWS);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }


    private String findNewUsername() {
        int maxCount = 10;
        String username = null;
        while ((username == null) && (maxCount > 0)) {
            username = Utilities.generateUsername();
            if (!ejbcaFindUser(username).get("found").toString().equals("0")) {
                username = null;
                maxCount--;
            }
        }

        return username;
    }


    public String ejbcaCertificateRequest(String pkcs10req) {
        CertificateFabric certificateFabric = new CertificateFabric();
        CertificateFabric.ReqData subject = certificateFabric.getReqSubject(pkcs10req);
        HashMap<String, String> attributes = CertificateFabric.getAttributes(subject.subject);
        String pemData = null;

        String username = findNewUsername();
        if (username == null) return "BADUSERNAME";

        String password = Utilities.generatePassword();

        UserDataVOWS userDataVOWS = new UserDataVOWS();

        StringBuilder ejbcaSubjectAlternativeNames = new StringBuilder();
        // RFC 4122
        ejbcaSubjectAlternativeNames.append("uri").append("=").append("urn:uuid:").append(username);
        ArrayList<ArrayList<String>> csrSanList = certificateFabric.getSubjectAlternativeNames();
        Object[] oids = csrSanList.toArray();
        final StringBuilder firstRfc822Name = new StringBuilder();
        for (int i = 0; i < oids.length; i++) {
            final String sanId = CertificateFabric.getSan(i);
            ArrayList<String> values = csrSanList.get(i);
            values.forEach((v) -> {
                if (ejbcaSubjectAlternativeNames.length() > 0) {
                    ejbcaSubjectAlternativeNames.append(",");
                }
                ejbcaSubjectAlternativeNames.append(sanId).append("=").append(v);
                if ((firstRfc822Name.length() == 0) &&
                        sanId.equals(SubjectAlternativeName.rfc822Name)) {
                    firstRfc822Name.append(v);
                }
            });
        }
        userDataVOWS.setSubjectAltName(ejbcaSubjectAlternativeNames.toString());

        String email = attributes.get("e");
        if (email == null) {
            // try subjectAlternativeNames
            email = firstRfc822Name.toString();
        }

        userDataVOWS.setCaName(properties.getProperty(Constants.propertyCaName));
        userDataVOWS.setCertificateProfileName(properties.getProperty(Constants.propertyCertificateProfileName));
        userDataVOWS.setEmail(email);
        userDataVOWS.setEndEntityProfileName(properties.getProperty(Constants.propertyEndEntityProfileName));
        userDataVOWS.setPassword(password);
        userDataVOWS.setStatus(10);

        checkAttributes(attributes);

        StringBuilder sb = new StringBuilder();
        attributes.forEach((k, v) -> {
            if (sb.length() != 0) sb.append(",");
            switch (k) {
                case "e":
                    sb.append("EMAILADDRESS").append("=").append(v);
                    break;
                default:
                    sb.append(k.toUpperCase()).append("=").append(v);
                    break;
            }
        });
        userDataVOWS.setSubjectDN(sb.toString());

        userDataVOWS.setTokenType("USERGENERATED");
        userDataVOWS.setUsername(username);

        final String hardTokenSn = null; // no hard token
        final String responseType = "CERTIFICATE";
        StringBuilder pem = new StringBuilder();
        try {
            CertificateResponse crt = port.certificateRequest(userDataVOWS, pkcs10req, 0, hardTokenSn, responseType);
            String pemCertificate = new String(crt.getData());
            pem.append(Constants.certificateBegin);
            pem.append("\n");
            pem.append(pemCertificate);
            pem.append("\n");
            pem.append(Constants.certificateEnd);
            pem.append("\n");
            pemData = pem.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return pemData;
    }


    public ArrayList<String> ejbcaGetLastCAChain() {
        ArrayList<String> certChain = new ArrayList<>();
        try {
            List<Certificate> chain = port.getLastCAChain(properties.getProperty("caName"));
            chain.forEach((k) -> {
                StringBuilder pem = new StringBuilder();
                pem.append(Constants.certificateBegin);
                pem.append("\n");
                pem.append(new String(k.getCertificateData()));
                pem.append("\n");
                pem.append(Constants.certificateEnd);
                pem.append("\n");
                certChain.add(pem.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return certChain;
    }


    @SuppressWarnings("UnusedReturnValue")
    private boolean checkAttributes(HashMap<String, String> attributes) {

        boolean changed = false;

        if (properties.getProperty("o") != null) {
            if (attributes.get("o") == null) {
                attributes.put("o", properties.getProperty("o"));
                changed = true;
            }
        }

        if (properties.getProperty("ou") != null) {
            if (attributes.get("ou") == null) {
                attributes.put("ou", properties.getProperty("ou"));
                changed = true;
            }
        }

        return changed;
    }

} // class
