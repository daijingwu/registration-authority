package net.felsing.client_cert.utilities;

/**
 * Created by cf on 27.07.15.
 * <p>
 * Load Properties
 */


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class PropertyLoader {
    private static final ArrayList<String> propertyfileLocations=new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(PropertyLoader.class);
    private static Properties properties = null;


    /**
     * search properties file
     * NOTE: LAST wins!
     */
    private static void loadProperties() {
        propertyfileLocations.add(Constants.emergencyPropertiesFile);
        propertyfileLocations.add("/media/cf/Development/ejbca-new-ra.properties.xml");
        propertyfileLocations.add("/home/newuser/ejbca-new-ra.properties.xml");
        propertyfileLocations.add("/Users/cf/Development/SignOn-Config/ejbca-new-ra.properties.xml");

        propertyfileLocations.forEach((v)->{
            try {
                Properties test=new Properties();
                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(v));
                test.loadFromXML(stream);
                stream.close();
                properties=test;
                logger.info("Using properties file "+v);
            } catch (Exception e) {
                logger.info("Tried properties file "+v+": Not found. This is not an error, if at least one time Using properties file occurs.");
            }
        });
        if (properties==null) {
            logger.fatal("No Property file found");
        }
    }


    public static Properties getProperties() {
        if (properties == null) {
            loadProperties();
        }
        return properties;
    }


    public static boolean getDebug() {
        try {
            return Boolean.parseBoolean(getProperties().getProperty(Constants.DEBUG));
        } catch (NullPointerException e) {
            return false;
        }
    }

} // class
