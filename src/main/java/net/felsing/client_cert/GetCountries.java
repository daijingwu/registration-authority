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
package net.felsing.client_cert;


import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Locale;


@WebServlet(urlPatterns = "/c", loadOnStartup = 1)
public class GetCountries extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(GetCountries.class);
    private String context;
    private JsonArray countries = null;


    private void loadFromJson(String classpath) {
        if (countries==null) {
            String countriesJson = "countries.json";
            JsonParser jsonParser = new JsonParser();
            countries = new JsonArray();
            try {
                String path = classpath + "/" + countriesJson;
                logger.info("path: " + path);

                JsonReader reader = new JsonReader(new FileReader(path));
                JsonArray rawCountries = jsonParser.parse(reader).getAsJsonArray();

                for (JsonElement jsonElement : rawCountries) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String cca2 = jsonObject.get("cca2").toString();
                    JsonObject country = new JsonObject();
                    country.add("cca2", new JsonPrimitive(cca2));
                    country.add("name", jsonObject.get("translations").getAsJsonObject());
                    countries.add(country);
                }
            } catch (Exception e) {
                countries = new JsonArray();
                countries.add(new JsonPrimitive("cannot parse " + countriesJson));
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String defaultLanguage = "eng";
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String bestLanguage = null;

        List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(req.getHeader("Accept-Language"));
        for (Locale.LanguageRange l : languageRanges) {
            Locale locale = new Locale(l.getRange());
            try {
                if (bestLanguage==null) {
                    bestLanguage=locale.getISO3Language();
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        if (bestLanguage==null) bestLanguage=defaultLanguage;
        PrintWriter pw = resp.getWriter();

        loadFromJson(context);


        JsonArray jsonArray = new JsonArray();
        for (JsonElement jsonElement: countries) {
            JsonObject jsonObject = new JsonObject();
            String cca2 = jsonElement.getAsJsonObject().get("cca2").toString().replaceAll("\\\"", "");
            cca2 = cca2.replaceAll("\\\\", "");
            logger.info ("cca2: " + cca2);
            jsonObject.add("cca2", new JsonPrimitive(cca2));
            try {
                jsonObject.add("name", jsonElement.getAsJsonObject().get("name").getAsJsonObject().get(bestLanguage).getAsJsonObject().get("common"));
            } catch (NullPointerException e) {
                jsonObject.add("name", jsonElement.getAsJsonObject().get("name").getAsJsonObject().get(defaultLanguage).getAsJsonObject().get("common"));
            }
            jsonArray.add(jsonObject);
        }
        pw.print(jsonArray.toString());
        pw.flush();
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        context = config.getServletContext().getRealPath("/WEB-INF/classes");
    }
} // class
