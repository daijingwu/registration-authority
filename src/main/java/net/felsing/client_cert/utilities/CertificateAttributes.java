package net.felsing.client_cert.utilities;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cf on 30.12.15.
 *
 */

public class CertificateAttributes {
    private static final Logger logger = LogManager.getLogger(EjbcaToolBox.class);
    private HashMap<String,String> attributes=new HashMap<>();
    private HashMap<String,String> labels=new HashMap<>();


    public CertificateAttributes () {
        String schemaFile=PropertyLoader.getProperties().getProperty(Constants.schemaFileName);
        try {
            JsonParser jsonParser=new JsonParser();
            JsonObject jsonObject=jsonParser.parse(new JsonReader(new FileReader(schemaFile))).getAsJsonObject();

            for (Map.Entry<String,JsonElement> entry : jsonObject.entrySet()) {
                attributes.put(entry.getKey().toLowerCase(),"");
                labels.put(entry.getKey().toLowerCase(),entry.getValue().toString());
            }

        } catch (FileNotFoundException e) {
            logger.fatal("File not found: "+e.getMessage());
        } catch (IllegalStateException e) {
            logger.fatal("Cannot read JSON file: "+e.getMessage());
        }
    }


    public boolean add (String k,String v) {
        if (attributes.get(k.toLowerCase())!=null) {
            attributes.put(k.toLowerCase(), v);
            return true;
        } else {
            return false;
        }
    }


    public String getValue (String k) {

        return attributes.get(k.toLowerCase());
    }


    public HashMap<String,String> getLabels () {

        return labels;
    }

} // class
