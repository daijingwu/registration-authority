package net.felsing.client_cert.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.SecurityUtils;


/**
 * Created by cf on 30.12.15.
 * Beans for CSR
 */
public final class CsrBeans {
    private static final Logger logger = LogManager.getLogger(EjbcaToolBox.class);
    private static HashMap<String, String> labels;


    public String getSubject () {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new JsonPrimitive("Authenticated").toString();
        } else {
            return new JsonPrimitive("Not Authenticated").toString();
        }
    }


    public static String getAttr(String k) {
        if (labels==null) {
            CertificateAttributes certificateAttributes = new CertificateAttributes();
            labels = certificateAttributes.getLabels();
        }
        return labels.get(k);
    }


    public static ArrayList<String> getAttributeList() {
        ArrayList<String> arrayList=new ArrayList<>();
        labels.forEach((k,v)->{
            arrayList.add(k);
        });
        return arrayList;
    }


    public String getForm() {
        if (labels==null) {
            CertificateAttributes certificateAttributes = new CertificateAttributes();
            labels = certificateAttributes.getLabels();
        }
        StringBuilder form = new StringBuilder();
        labels.forEach((k, v) -> {
            v=v.replaceAll("\"","");
            switch (k) {
                case "c":
                    form.append("<div id=\"").append(k).append("_group\" class=\"input_group\">\n");
                    form.append("  <label for=\"").append(k).append("\">").append(v).append("</label>\n");
                    form.append("  <select id=\"c\"></select>\n");
                    form.append("</div>\n");
                    break;
                case "st":
                    form.append("<div id=\"").append(k).append("_group\" class=\"input_group\">\n");
                    form.append("  <label for=\"").append(k).append("\">").append(v).append("</label>\n");
                    form.append("  <select id=\"st\"></select>\n");
                    form.append("</div>\n");
                    break;
                default:
                    form.append("<div id=\"").append(k).append("_group\" class=\"input_group\">\n");
                    form.append("  <label for=\"").append(k).append("\">").append(v).append("</label>\n");
                    form.append("  <input type=\"text\" id=\"").append(k).append("\"/>\n");
                    form.append("</div>\n");
                    break;
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
    
    
    public boolean getLoginStatus () {
        return PropertyLoader.getProperties().getProperty(Constants.propertyAuthRequired)==null || 
                SecurityUtils.getSubject().isAuthenticated();

    }


    public String getTitle() {
        return PropertyLoader.getProperties().getProperty(Constants.title);
    }

} // class

