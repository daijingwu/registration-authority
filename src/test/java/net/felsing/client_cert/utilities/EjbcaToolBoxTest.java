package net.felsing.client_cert.utilities;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by cf on 29.12.15.
 *
 * Tests for EjbcaToolBox
 */

public class EjbcaToolBoxTest {

    private Properties properties=null;
    private EjbcaToolBox ejbcaToolBox=null;

    private synchronized void init() {
        if (properties==null) {
            properties=PropertyLoader.getProperties();
        }

        if (ejbcaToolBox==null) {
            ejbcaToolBox=new EjbcaToolBox(properties);
        }
    }


    @Test
    public void testGetAvailableCas() throws Exception {
        init();
        JsonObject jsonObject=ejbcaToolBox.getAvailableCas();
        String caList=jsonObject.toString();
        assertNotNull(caList);
        System.out.println(caList);
    }

    @Test
    public void testEjbcaFindUser() throws Exception {
        init();
        JsonObject res=ejbcaToolBox.ejbcaFindUser("Joe Test");
        System.out.println("testEjbcaFindUser: "+res.toString());
    }

    @Test
    public void testEjbcaEditUser() throws Exception {
        init();
        final HashMap<String,String> user = new HashMap<>();
        user.put("username", "Joe Test");
        user.put("password", "geheim");
        user.put("email", "joe.test@example.net");
        user.put("entityStatus", Integer.toString(EjbcaToolBox.entNew));
        System.out.println(ejbcaToolBox.ejbcaEditUser(user).toString());
    }

    @Test
    public void testEjbcaGetLastCAChain() throws Exception {
        init();
        ArrayList<String> chain=ejbcaToolBox.ejbcaGetLastCAChain();
        chain.forEach(System.out::println);
    }

} // class
