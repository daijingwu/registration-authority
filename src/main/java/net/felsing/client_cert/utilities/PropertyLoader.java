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


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class PropertyLoader {
    private static final ArrayList<String> propertyfileLocations=new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);
    private static Properties properties = null;
    private static JsonObject jsonProperties = new JsonObject();


    /**
     * search properties file
     * NOTE: LAST wins!
     */
    private static void loadProperties() {
        propertyfileLocations.add("/etc/ejbca-new-ra/" + Constants.propertyFilename);
        propertyfileLocations.add("/usr/local/etc/ejbca-new-ra/" + Constants.propertyFilename);

        if (System.getenv(Constants.propertyEnvname)!=null) {
            propertyfileLocations.add(System.getenv(Constants.propertyEnvname));
        }

        propertyfileLocations.forEach((v)->{
            try {
                Properties test=new Properties();
                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(v));
                test.loadFromXML(stream);
                stream.close();
                properties=test;
                logger.info("Using properties file "+v);
            } catch (Exception e) {
                //logger.info("Tried properties file "+v+": Not found. This is not an error, if at least one time Using properties file occurs.");
            }
        });
        if (properties==null) {
            logger.error ("No Property file found");
        }

        assert (properties!=null);
        properties.forEach((k,v) -> {
            String pattern = "^js\\.(.+)$";
            if (k.toString().matches(pattern)) {
                try {
                    String configKey = k.toString().replaceAll(pattern, "$1");
                    jsonProperties.add(configKey, new JsonPrimitive(v.toString()));
                } catch (Exception e) {
                    logger.error("Exception: ", (Object[]) e.getStackTrace());
                }
            }
        });
    }


    public static Properties getProperties() {
        if (properties == null) {
            loadProperties();
        }
        return properties;
    }


    public static JsonObject getJavaScriptProperties () {

        return jsonProperties;
    }


    public static boolean getDebug() {
        try {
            return Boolean.parseBoolean(getProperties().getProperty(Constants.DEBUG));
        } catch (NullPointerException e) {
            return false;
        }
    }

} // class
