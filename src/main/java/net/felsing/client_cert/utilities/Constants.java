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


public final class Constants {

    static final String propertyFilename = "ejbca-new-ra.properties.xml";
    static final String propertyEnvname = "EJBCA_NEW_RA_PROPERTIES";
    public static final String propertyAuthRequired = "auth.required";


    static final String trustStoreFile = "trustStoreFile";
    static final String keyStoreFile = "keyStoreFile";
    static final String trustStorePassword = "trustStorePassword";

    static final String proxyHost = "proxyHost";
    static final String proxyPort = "proxyPort";
    static final String proxyUser = "proxyUser";
    static final String proxyPassword = "proxyPassword";

    static final String certificateBegin = "-----BEGIN CERTIFICATE-----";
    static final String certificateEnd = "-----END CERTIFICATE-----";

    static final String schemaFileName = "schemaFileName";
    public static final String title = "title";

    static final String DEBUG = "DEBUG";
    
    public static final String unsupportedRequest = "not supported request";
}
