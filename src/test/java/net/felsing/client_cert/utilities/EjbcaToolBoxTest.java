package net.felsing.client_cert.utilities;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by cf on 29.12.15.
 *
 * Tests for EjbcaToolBox
 */

public class EjbcaToolBoxTest {

    Properties properties=null;
    EjbcaToolBox ejbcaToolBox=null;

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

    //@Test
    public void testEjbcaFindUser() throws Exception {
        init();
        JsonObject res=ejbcaToolBox.ejbcaFindUser("Joe Test");
        System.out.println("testEjbcaFindUser: "+res.toString());
    }

    //@Test
    public void testEjbcaEditUser() throws Exception {
        init();
        ejbcaToolBox.ejbcaEditUser("Joe Test","geheim","joe.test@example.net");
    }

    @Test
    public void testEjbcaGetLastCAChain() throws Exception {
        init();
        ArrayList<String> chain=ejbcaToolBox.ejbcaGetLastCAChain();
        chain.forEach((k)->{
            System.out.println(k);
        });
    }

} // class
