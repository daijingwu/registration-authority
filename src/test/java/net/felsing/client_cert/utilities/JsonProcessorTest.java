package net.felsing.client_cert.utilities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by cf on 28.12.15.
 */
public class JsonProcessorTest {


    private static String req="{\"pkcs10\":\"-----BEGIN CERTIFICATE REQUEST-----\\r\\nMIICkjCCAXwCAQAwHzEdMAkGA1UEBhMCQUQwEAYDVQQDDAlmZGdkc2dkZmcwggEi\\r\\nMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDI6AaNW+nNIfyE1uNm+iHXYBbG\\r\\n22b2bLsWgcTcXPTI7sj69YU5JsQu2wK2gEL6LDFpj0JCaEsa5y9RVIk+G699THwi\\r\\nsvBbuHcCB1cFsWwBjdRpnDyfJ9J57Zo7yMI6OsZrShi8aG+CeAmqwFMCox66jube\\r\\ngTWutMXfNcdMgL89xVibNprZVzmJ5uaWqL4RN1bnRBNyLl1LlmKsFUfZGn/1T41w\\r\\ncbIQCMY21GaaA8YvlWOPPO2GmF2QzxPybW3Tk8yrtCxdqg8OHxEzfT3VCOIXL4oK\\r\\n0oI5Wa+8LGrDy8NjA3Zsmi5VJ/xm3LyGYoK7Gke1W/aZfF3YBrePC027EPVrAgMB\\r\\nAAGgMDAuBgkqhkiG9w0BCQ4xITAfMB0GA1UdDgQWBBTFne4O5b1TP6njNAClTMqi\\r\\nw703azALBgkqhkiG9w0BAQsDggEBAL3pIvXsQPaFdfJ1L1mUgzkjUOarT4oSAm1L\\r\\ncRvrylTwvpJQuf1rR+uaMqVpMpynr0G6Gwog0Ko8OW4i5+nEu0xCopMgBvLzsNNv\\r\\nERwNTxVQ/sQkBND1DU326WjTrdwYhwJ2LHspvCk+o47LibucYaxBSVTW5GO+MOGH\\r\\n4bJ0vOWjx44xOFAdrNOUnCt/A8zGeTvng8Rw0eZgmkqiAJt/WaspYa8VG4sBij6G\\r\\ncw0qJ5E15CqPFy9/eSIEsSfYR7KsWjoNaelrF8lgEwYAedGwu5wSqhibA4Va20ZK\\r\\nB9OmtbYx4+eLu80rhP5laCGa+UgpEwOAYBoO9I2P4pYalXH2+Gs=\\r\\n-----END CERTIFICATE REQUEST-----\\r\\n\"}\n";

    @Test
    public void testGetSubject() throws Exception {
        JsonProcessor jsonProcessor=new JsonProcessor();
        String subject=jsonProcessor.getSubject(req).subject;
        System.out.println("subject: "+subject);
    }
}