package net.felsing.client_cert.utilities;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
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

public class CertificateFabricTest {

    @SuppressWarnings("all")
    private static String reqWrong = "" +
            "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "blah fasel test\n" +
            "-----END CERTIFICATE REQUEST-----";

    @SuppressWarnings("WeakerAccess")
    public static String reqRSAok = "" +
            "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIE9zCCAt8CAQAwaTEQMA4GA1UEAwwHSm9lIEpvYjEQMA4GA1UECgwHRXhhbXBs\n" +
            "ZTERMA8GA1UECwwIRGl2aXNpb24xEjAQBgNVBAcMCVdpZXNiYWRlbjEPMA0GA1UE\n" +
            "CAwGSGVzc2VuMQswCQYDVQQGEwJERTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCC\n" +
            "AgoCggIBAPfJfuO4F60VruM9g+CPzkUOQbU5KYMKUTO4lpnYR8L1+Zpx3Og6/EiR\n" +
            "10G8nZE1WwH389HwH9SNK+agWIZM9NYYt1kXOegzK2nKqoIFWN7cxUxZl6+vP6il\n" +
            "elFhGe9D/DWBqzhn6f2K53p1jC5z4TD5RuL9HMG6c4JjtX0Rd4fULqSMMUjXfNjH\n" +
            "s0bntD914anwFV9Y/Z0Dllq0CoilZZpU9OQfqyzzeTWjPrQ9S3dKSyiKsYUND14s\n" +
            "jXm7zeJDA2EnaMyfgArKFlm7VHmIpNOzv5quWX5s+BtcH555weCdFZKlhBNg7xhV\n" +
            "C72qmHXjgQh7R0KyROFqa3cOpEW027aPyDg4xoSkSwIlJ5BOEvlFbtbzy46kslKG\n" +
            "SxBw6crIX2tbwW6vT/uX7js7bCcWaJyvFvW/1D9bZw09q8peGX1Ga/0mg9SGQ/Mt\n" +
            "NdSX6qL23mcRX9oDgw7+TMlMhUV9tecnUipPN97iOHlFjFbtXXK9/+6jtBS0FFIc\n" +
            "m+xjpYwG/MVGXthzCrn5MHYJbbQK3Vxf5WYietEHiE+1ejkBGuIvHv2lWLf3gHhV\n" +
            "Zkd+8p2+/oI6fncp5GAdWI0hBA/r3Z1iCXhlKAZcJU77A3v9fj6WQqzR0oVrwt/+\n" +
            "3Mgx1tJOnhJK7cb65XxpZiG1+DcXsXcHZ8IuMCPE47sj1at1qpe9AgMBAAGgSTBH\n" +
            "BgkqhkiG9w0BCQ4xOjA4MB4GA1UdEQQXMBWBE2pvZS5qb2JAZXhhbXBsZS5jb20w\n" +
            "CQYDVR0TBAIwADALBgNVHQ8EBAMCBeAwDQYJKoZIhvcNAQELBQADggIBALtbi+ez\n" +
            "rxcqCUgTIK4xa9GciDyjzPrJOoIFis12GpcpIdt1cZKwR+ZOhnPSjbZkDVXATtgy\n" +
            "WSXwiQ8Rbk6ZmmLaBifxDn8iQpXn0ubCPtJ8GAClhUOAMcdgH0eCcMIwEcjp6kRY\n" +
            "ZvJXqTeMsT1MOzs/60WPlLuZSa7lORxe/hcnUhrb7LEn1Ua4JLDoF8mzf2Hpwkgd\n" +
            "zOVvfS85n6XwtzfEn+KvAu4Tj5pw1/uo/cKs53Zkkm7loYogHyBIVilikeGSRs8g\n" +
            "8jFRxmhnKMTEEveplIfdNrKEIzFTmUW+pp/NgzHKfHk07YOK7o4iek2OGSfjOuUo\n" +
            "ct5VnaSzA+TsAHhi4qKiqK7slyVlMLwQU3CImILA85uF5inPURK8O/ihDN7UX64h\n" +
            "s8SiyMsqBb2S9F0r5EgpJEfq1RzFoYkRAsldUIN2j4iPEt/2k7uo69ZToUt+FqJ8\n" +
            "1Brofbrvs4x+P7lx8q6whywgqoCg/FiYgsNnTn5FJmYKdp/21aDzAXpFUst/M/ov\n" +
            "1oh97465E3+hNpCDWS35XcEibjbbiGqpiveTdRnx7N9kbyFEiPT8dRzY+eePf8MU\n" +
            "dH4mub8lEW3jnkXd+kzedh6WfqMrvIVtfrF5pht4tRVb2ozIfyN0S/7R1dWk0sVH\n" +
            "Fo4Ql8E1FelAq5rZefG9Gq1vRF953aEPX/UK\n" +
            "-----END CERTIFICATE REQUEST-----\n";

    @SuppressWarnings("WeakerAccess")
    public static String reqECDSA = "" +
            "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIB9jCCAVcCAQAwaTEQMA4GA1UEAwwHSm9lIEpvYjEQMA4GA1UECgwHRXhhbXBs\n" +
            "ZTERMA8GA1UECwwIRGl2aXNpb24xEjAQBgNVBAcMCVdpZXNiYWRlbjEPMA0GA1UE\n" +
            "CAwGSGVzc2VuMQswCQYDVQQGEwJERTCBmzAQBgcqhkjOPQIBBgUrgQQAIwOBhgAE\n" +
            "AeeLB+OoHY3BBgXAGca7gm9XLUurnjutaSA0dgqeuBdMPAcBhlwpM+SD0s+oz3uU\n" +
            "Uc9fL6SkuJ9DOeyf+/xvIiLcADAO73hHe/la4u19gMFP0eCHfT48r/t+7Kp8siTL\n" +
            "cTBzW5L15ngcug/y6jVsFAc4ye7zITacUFZub4AqhToPLfaKoEkwRwYJKoZIhvcN\n" +
            "AQkOMTowODAeBgNVHREEFzAVgRNqb2Uuam9iQGV4YW1wbGUuY29tMAkGA1UdEwQC\n" +
            "MAAwCwYDVR0PBAQDAgXgMAoGCCqGSM49BAMCA4GMADCBiAJCAKIUw7sNTie0A2dv\n" +
            "Rco6G7em4Kd47n8+PXaafkSAuXcq3l/vnFM/A9IUk+XD0F7QpnCXNajpxxF+QKEW\n" +
            "/IBURlg2AkIBBGnlB5dsjh9XJkC6ePwruFnBSberSt9UMkdUBaRRuaRZu120vbJm\n" +
            "JD2xsJ6DutmHFoMqQIeVIP76FKY+e/joHus=\n" +
            "-----END CERTIFICATE REQUEST-----\n";


    @Test
    public void testGetReqDataRSA() throws Exception {
        System.out.println("Test testGetReqDataRSA");
        System.out.println("======================");
        CertificateFabric certificateFabric = new CertificateFabric();
        String subject = certificateFabric.getReqSubject(reqRSAok).subject;
        System.out.println("subject (rsa): " + subject);
        System.out.println("");
    }


    @Test
    public void testGetReqDataECC() throws Exception {
        System.out.println("Test testGetReqDataECC");
        System.out.println("======================");
        CertificateFabric certificateFabric = new CertificateFabric();
        String subject = certificateFabric.getReqSubject(reqECDSA).subject;
        System.out.println("subject (ecc): " + subject);
        System.out.println("");
    }


    private void dumpSubjectAlternativeNames(CertificateFabric.ReqData reqData) {
        System.out.println("Subject Alternative Names");
        System.out.println("-------------------------");
        ArrayList<ArrayList<String>> csrSanList = reqData.subjectAlternativeNames;
        Object[] oids = csrSanList.toArray();
        final int[] i={0};
        while (i[0]<oids.length) {
            ArrayList<String> values = csrSanList.get(i[0]);
            values.forEach((v) -> System.out.println ("    " + CertificateFabric.getSan(i[0])+"="+v));
            i[0]++;
        }
    }


    private CertificateFabric.ReqData testCSR (String csr) {
        CertificateFabric certificateFabric = new CertificateFabric();
        CertificateFabric.ReqData reqData = certificateFabric.getReqSubject(reqRSAok);
        System.out.println("subject: " + reqData.subject);
        dumpSubjectAlternativeNames(reqData);
        return reqData;
    }


    @Test
    public void validRequestRSA() throws Exception {
        System.out.println("Test validRequest RSA");
        System.out.println("=====================");
        CertificateFabric.ReqData reqData = testCSR(reqRSAok);
        System.out.println("");
        assertNotNull(reqData.subject);
    }


    @Test
    public void validRequestECDSA() throws Exception {
        System.out.println("Test validRequest ECDSA");
        System.out.println("=======================");
        CertificateFabric.ReqData reqData = testCSR(reqECDSA);
        System.out.println("");
        assertNotNull(reqData.subject);
    }


    @Test
    public void invalidRequest() throws Exception {
        System.out.println("Test invalidRequest");
        System.out.println("===================");
        CertificateFabric certificateFabric = new CertificateFabric();
        String subject = certificateFabric.getReqSubject(reqWrong).subject;
        System.out.println("subject should be null");
        System.out.println("");
        assertNull(subject);
    }


} // class
