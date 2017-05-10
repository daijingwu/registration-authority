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
import net.felsing.client_cert.utilities.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.Collator;
import java.util.*;


@WebServlet(urlPatterns = "/c", loadOnStartup = 1)
public class GetCountries extends HttpServlet {
    private Locale usedLocale = null;

    private class Country implements Comparable<Country> {
        public String cca2;
        public String name;

        @Override
        public int compareTo(Country o) {
            Collator collator = Collator.getInstance(usedLocale);
            return collator.compare (name, o.name);
        }
    }


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

                JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(path), Constants.utf8));
                JsonArray rawCountries = jsonParser.parse(reader).getAsJsonArray();

                for (JsonElement jsonElement : rawCountries) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String cca2 = jsonObject.get("cca2").toString();
                    JsonObject country = new JsonObject();
                    country.add("cca2", new JsonPrimitive(cca2));
                    country.add("name_", jsonObject.get("name").getAsJsonObject().get("common"));
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
        final Locale defaultLanguage = Locale.ENGLISH;
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String bestLanguage = null;

        List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(req.getHeader("Accept-Language"));
        for (Locale.LanguageRange l : languageRanges) {
            Locale locale = new Locale(l.getRange());
            try {
                if (bestLanguage==null) {
                    bestLanguage=locale.getISO3Language();
                    usedLocale = locale;
                }
            } catch (Exception e) {
                logger.debug("No sufficient language found for " + languageRanges.toString());
            }
        }

        if (bestLanguage==null) {
            bestLanguage=defaultLanguage.getISO3Language();
            usedLocale = Locale.ENGLISH;
        }

        logger.info("usedLocale: " + usedLocale.getISO3Language());

        PrintWriter pw = resp.getWriter();
        loadFromJson(context);
        List<Country> countriesList = new ArrayList<>();
        for (JsonElement jsonElement: countries) {
            String cca2 = jsonElement.getAsJsonObject().get("cca2").toString().replaceAll("\\\"", "");
            cca2 = cca2.replaceAll("\\\\", "");

            Country countryItem = new Country();
            try {
                countryItem.name =
                        jsonElement.getAsJsonObject().get("name").
                                getAsJsonObject().get(bestLanguage).
                                getAsJsonObject().get("common").getAsString();
            } catch (NullPointerException e) {
                countryItem.name =
                        jsonElement.getAsJsonObject().get("name_").getAsString();
            }
            countryItem.cca2 = cca2;
            countriesList.add(countryItem);
        }
        Collator coll = Collator.getInstance(usedLocale);
        coll.setStrength(Collator.PRIMARY);
        Collections.sort(countriesList);
        pw.print(new Gson().toJson(countriesList));
        pw.flush();
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        context = config.getServletContext().getRealPath("/WEB-INF/classes");
    }
} // class
