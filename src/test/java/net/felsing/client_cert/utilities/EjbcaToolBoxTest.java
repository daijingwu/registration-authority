package net.felsing.client_cert.utilities;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by cf on 29.12.15.
 *
 * Tests for EjbcaToolBox
 */

public class EjbcaToolBoxTest {

    @Test
    public void testGetAvailableCas() throws Exception {
        Properties properties=PropertyLoader.getProperties();
        EjbcaToolBox ejbcaToolBox=new EjbcaToolBox(properties);
        JsonObject jsonObject=ejbcaToolBox.getAvailableCas();
        String caList=jsonObject.toString();
        assertNotNull(caList);
        System.out.println(caList);
    }
}