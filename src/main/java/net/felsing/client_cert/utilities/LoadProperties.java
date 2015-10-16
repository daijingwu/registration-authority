package net.felsing.client_cert.utilities;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by cf on 25.08.15.
 */
public final class LoadProperties {

    private static Properties properties=null;

    public final static Properties load(String realPath) {

        if (properties == null) {
            String propsFile = realPath;
            properties = new Properties();
            try {
                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(propsFile));
                properties.loadFromXML(stream);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return properties;
    }

}
