package net.felsing.client_cert.utilities;

import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(EjbcaToolBoxTest.class);

    private Properties properties=null;
    private EjbcaToolBox ejbcaToolBox=null;
    @SuppressWarnings("all")
    private static String testCnExists = "Joe Test";
    @SuppressWarnings("all")
    private static String testCnNotExists = "Not existing user";


    private String csr = "" +
            "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIBhzCCAS0CAQAwaTEQMA4GA1UEAwwHSm9lIEpvYjEQMA4GA1UECgwHRXhhbXBs\n" +
            "ZTERMA8GA1UECwwIRGl2aXNpb24xEjAQBgNVBAcMCVdpZXNiYWRlbjEPMA0GA1UE\n" +
            "CAwGSGVzc2VuMQswCQYDVQQGEwJERTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IA\n" +
            "BIY8QvOhZUGsuB4sdUzGRdkRFSQ0NfAVCvUV7FSzZoW2aWy2k+e9J7qZeHEJUsnd\n" +
            "qNwIaa16KPcKMOEOBdjjCK6gYjBgBgkqhkiG9w0BCQ4xUzBRMDcGA1UdEQQwMC6B\n" +
            "FWpvZS5qb2IuMUBleGFtcGxlLmNvbYEVam9lLmpvYi4yQGV4YW1wbGUuY29tMAkG\n" +
            "A1UdEwQCMAAwCwYDVR0PBAQDAgXgMAoGCCqGSM49BAMCA0gAMEUCIQCDgvDvEyAK\n" +
            "21luCpQQTV4VsSh9XkSBB2msrhzppctiJQIgU8Kt5IPabhvuGptawAx88J3xNw+R\n" +
            "971x0XlQTqPfn3I=\n" +
            "-----END CERTIFICATE REQUEST-----\n";


    private synchronized void init() {
        if (properties==null) {
            properties=PropertyLoader.getProperties();
        }

        if (ejbcaToolBox==null) {
            ejbcaToolBox=new EjbcaToolBox(properties);
        }
    }


    //@Test
    public void testGetAvailableCas() throws Exception {
        System.out.println ("testGetAvailableCas");
        init();
        JsonObject jsonObject=ejbcaToolBox.getAvailableCas();
        String caList=jsonObject.toString();
        assertNotNull(caList);
        System.out.println(caList);
    }

    //@Test
    public void testEjbcaUserExists() throws Exception {
        System.out.println ("testEjbcaUserExists: " + testCnExists);
        init();
        JsonObject res=ejbcaToolBox.ejbcaFindUser(testCnExists);
        System.out.println("testEjbcaUserExists: "+res.toString());
        assert (res.get("found").toString().equals("\"1\""));
    }


    //@Test
    public void testEjbcaUserNotExists() throws Exception {
        System.out.println ("testEjbcaUserNotExists: " + testCnNotExists);
        init();
        JsonObject res=ejbcaToolBox.ejbcaFindUser(testCnNotExists);
        System.out.println("testEjbcaUserNotExists: "+res.get("found").toString());
        assert (res.get("found").toString().equals("0"));
    }


    /*
     * Subject alternative names can be:
     * rfc822Name=<email>,
     * dNSName=<host name>,
     * uri=<http://host.com/>,
     * ipaddress=<address>,
     * upn=<MS UPN>,
     * guid=<MS globally unique id>,
     * directoryName=<LDAP escaped DN>,
     * krb5principal=<Krb5 principal name>,
     * permanentIdentifier=<Permanent Identifier values>
     */
    //@Test
    public void testEjbcaEditUser() throws Exception {
        final HashMap<String,Object> user = new HashMap<>();
        final HashMap<String,ArrayList<String>> sanMap = new HashMap<>();

        ArrayList<String> rfc822names = new ArrayList<>();
        rfc822names.add("joe.test.1@example.com");
        rfc822names.add("joe.test.2@example.com");
        sanMap.put("rfc822name",rfc822names);

        ArrayList<String> msguids = new ArrayList<>();
        msguids.add(new String (Hex.encodeHex("my gui identifier 1".getBytes())));
        //msguids.add(new String (Hex.encodeHex("my gui identifier 2".getBytes())));
        sanMap.put("guid", msguids);

        ArrayList<String> permanentIdentifiers = new ArrayList<>();
        permanentIdentifiers.add("my permanentIdentifier 1");
        //permanentIdentifiers.add("my permanentIdentifier 2");
        //sanMap.put("permanentIdentifier", permanentIdentifiers);
        sanMap.put("uri", permanentIdentifiers);

        user.put("username", "Joe Test");
        user.put("password", "geheim");
        user.put("email", "joe.test@example.net");
        user.put("entityStatus", Integer.toString(EjbcaToolBox.entNew));

        user.put("attr.cn", user.get("username"));
        user.put("attr.o", "junit test organization");
        user.put("attr.ou", "junit test organizational unit");
        user.put("attr.c", "DE");
        user.put("attr.san", sanMap);

        System.out.println ("testEjbcaEditUser: " + user.get("username"));
        init();
        System.out.println(Boolean.toString(ejbcaToolBox.ejbcaEditUser(user)));
    }


    @Test
    public void testCreateCertificate () throws Exception {
        CertificateFabric certificateFabric = new CertificateFabric();
        CertificateFabric.ReqData reqData = certificateFabric.getReqSubject(csr);

        System.out.println ("testCreateCertificate");
        System.out.println ("--------------------------------------------------------------------------");
        System.out.println ("" +
                "This test will fail, if EJBCA backend has constraints not matching provided" +
                "certificate request.\n" +
                "Certificate has following content:\n" +
                reqData.subject +
                "");

        final int sanOid[] = {0};
        reqData.subjectAlternativeNames.forEach((k) -> {
            final int[] count={0};
            k.forEach((k1) -> {
                System.out.println(CertificateFabric.getSan(sanOid[0]) +
                        "[" + Integer.toString(count[0]) + "] : " +
                        k1);
                count[0]++;
            });
            sanOid[0]++;
        });
        System.out.println ("--------------------------------------------------------------------------");

        init();
        String certificate = ejbcaToolBox.ejbcaCertificateRequest(csr);
        System.out.println (certificate);
    }


    //@Test
    public void testEjbcaGetLastCAChain() throws Exception {
        System.out.println ("testEjbcaGetLastCAChain");
        System.out.println ("--------------------------------------------------------------------------");
        init();
        ArrayList<String> chain=ejbcaToolBox.ejbcaGetLastCAChain();
        chain.forEach(System.out::println);
        System.out.println ("--------------------------------------------------------------------------");
    }

} // class
