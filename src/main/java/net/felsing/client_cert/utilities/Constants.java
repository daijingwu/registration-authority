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


@SuppressWarnings("WeakerAccess")
public final class Constants {

    public static final String ldapTruststoreFile = "ldap.truststore.file";
    public static final String ldapTruststorePassword = "ldap.truststore.password";

    public static final String propertyFilename = "ejbca-new-ra.properties.xml";
    public static final String propertyEnvname = "EJBCA_NEW_RA_PROPERTIES";
    public static final String propertyAuthRequired = "auth.required";
    public static final String propertyRaGroup = "group.ra";
    public static final String propertyCertificateProfileName = "CertificateProfileName";
    public static final String propertyEndEntityProfileName = "EndEntityProfileName";
    public static final String propertyCaName = "caName";

    public static final String propertyDummyUser ="auth.dummy.user";
    public static final String propertyDummyPassword ="auth.dummy.password";

    public static final String formUsername = "loginusername";
    public static final String formPassword = "loginpassword";

    public static final String ejbcaKeyStoreType = "ejbca.keystore.type";
    public static final String ejbcaKeyStoreFile = "ejbca.keystore.file";
    public static final String ejbcaKeyStorePassword = "ejbca.keystore.password";
    public static final String ejbcaTrustStoreType = "ejbca.truststore.type";
    public static final String ejbcaTrustStoreFile = "ejbca.truststore.file";
    public static final String ejbcaTrustStorePassword = "ejbca.truststore.password";
    public static final String ejbcaTlsProtocol = "ejbca.tls";

    public static final String proxyHost = "proxyHost";
    public static final String proxyPort = "proxyPort";
    public static final String proxyUser = "proxyUser";
    public static final String proxyPassword = "proxyPassword";

    public static final String certificateBegin = "-----BEGIN CERTIFICATE-----";
    public static final String certificateEnd = "-----END CERTIFICATE-----";
    public static final String csrBegin = "-----BEGIN CERTIFICATE REQUEST-----";
    public static final String csrEnd = "-----END CERTIFICATE REQUEST-----";

    public static final String schemaFileName = "schemaFileName";
    public static final String title = "title";

    public static final String DEBUG = "DEBUG";
    
    public static final String unsupportedRequest = "not supported request";

    public static final int numBits = 384;

    public static final String utf8 = "UTF-8";

    public static long threadJoinTimeout = 1000; // ms
    public static long threadInterval = 5000; //ms

}
