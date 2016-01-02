package net.felsing.client_cert.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;

/**
 * Created by cf on 30.12.15.
 * Beans for CSR
 */
public final class CsrBeans {
    private static final Logger logger = LogManager.getLogger(EjbcaToolBox.class);

    static HashMap<String, String> labels;


    public static String getAttr(String k) {
        if (labels==null) {
            CertificateAttributes certificateAttributes = new CertificateAttributes();
            labels = certificateAttributes.getLabels();
        }
        return labels.get(k);
    }


    public String getForm() {
        if (labels==null) {
            CertificateAttributes certificateAttributes = new CertificateAttributes();
            labels = certificateAttributes.getLabels();
        }
        StringBuffer form = new StringBuffer();
        labels.forEach((k, v) -> {
            v=v.replaceAll("\"","");
            if (k.equals("c")) {
                form.append("<div id=\"" + k + "_group\" class=\"input_group\">\n");
                form.append("  <label for=\"" + k + "\">" + v + "</label>\n");
                form.append("  <select id=\"country\"></select>\n");
                form.append("</div>\n");
            } else if (k.equals("c")) {
                form.append("<div id=\"" + k + "_group\" class=\"input_group\">\n");
                form.append("  <label for=\"" + k + "\">" + v + "</label>\n");
                form.append("  <select id=\"state\"></select>\n");
                form.append("</div>\n");
            } else {
                form.append("<div id=\"" + k + "_group\" class=\"input_group\">\n");
                form.append("  <label for=\"" + k + "\">" + v + "</label>\n");
                form.append("  <input type=\"text\" id=\"" + k + "\"/>\n");
                form.append("</div>\n");
            }
        });

        return form.toString();
    }


    public String getFormSchema() {
        String schemaFile=PropertyLoader.getProperties().getProperty(Constants.schemaFileName);
        JsonParser jsonParser=new JsonParser();
        try {
            JsonObject jsonObject=jsonParser.parse(new JsonReader(new FileReader(schemaFile))).getAsJsonObject();
            return jsonObject.toString();
        } catch (FileNotFoundException e) {
            logger.error("Cannot open file: "+e.getMessage());
            return null;
        }
    }


    public String getTitle() {

        return PropertyLoader.getProperties().getProperty(Constants.title);
    }

} // class

