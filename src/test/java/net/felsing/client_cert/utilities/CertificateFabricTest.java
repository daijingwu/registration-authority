package net.felsing.client_cert.utilities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by cf on 28.12.15.
 * <p>
 * Tests for CertificateFabric
 */

public class CertificateFabricTest {

    private static String reqWrong = "" +
            "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "blah fasel test\n" +
            "-----END CERTIFICATE REQUEST-----";

    private static String reqRSA = "" +
            "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIEzjCCArYCAQAwgYgxCzAJBgNVBAYTAkRFMQ8wDQYDVQQIDAZIZXNzZW4xFDAS\n" +
            "BgNVBAcMC1RhdW51c3N0ZWluMRgwFgYDVQQKDA90YXVudXNzdGVpbi5uZXQxDTAL\n" +
            "BgNVBAMMBHRlc3QxKTAnBgkqhkiG9w0BCQEWGmhvc3RtYXN0ZXJAdGF1bnVzc3Rl\n" +
            "aW4ubmV0MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA5ysWBatXJwDF\n" +
            "HiWGTF2AYLDTTebPlJEkPEJHHJ9BuJaZyi6I0p7awru+YNdafRjWBEyjuXoZUGIK\n" +
            "BRR5/YnmKvW4dHPWhXt6j+XXwUN9z1zP0YhrT3XRgBmBT4R9tfuSXkTAnyrFHWU+\n" +
            "WSwmah13JrZPYHLOGg9wi2ZtQ8xlSqEBpiaeBJ6IrW5mXwBCp9xA4/HoZXigIrzi\n" +
            "roMy8xUyW+hb7QCWM5nMgOW0hqm8i8m/dw8DSo7oizdIYR/GUPjtI7N4/q3QB6Cj\n" +
            "/CHH+Np+nB0CFki8da0gN5PsFkDkzhSRgLhfqxQ8mkLCzwdViXEre1z1TkhYJrh4\n" +
            "OwASdjdcfc4Fh5S3ZHY3he7Hx1LN5dD3a1yCpvsC+U57NSjV+ZiQqSim3Twb5PgI\n" +
            "fbHJJyy8meB1Y23CHhVV8yzbY72G+mzLDLtRVQcMjQuy3b2bfCya1xC2ILIjXtAa\n" +
            "Zn/1Mkw7GQY8clYPX9u/QV4CBxZ+6O5JJoRQtD9oF3QvD9QHI1QMwE9cKL4R0p63\n" +
            "5DX4FMTT1i2ShG/kOrsgAq1/TnjZHrJXJondG4f64fVh+h1mbq7KgW6gWliurNLd\n" +
            "9K/nnYqpw1cuwyRlRBHbJ6L8CqW3I/wbVwzv4On8PwpBKBoQPZvHZ7wcfAHBhMY8\n" +
            "FldqSvsPQbx6kPCY9GX6gaYcGgJi/CUCAwEAAaAAMA0GCSqGSIb3DQEBCwUAA4IC\n" +
            "AQAXATy796go+ERGGrHyohAb8EF1n0cpTGIRBzwRRg36llqpc3FI83WZ0aJ0k+x/\n" +
            "SgN4rfxw3UOE4JdWravSPZPoOjgRgTdVOxp5+HdQiDYwCdtnLlAU0+aWwTfT+Qin\n" +
            "h0s2Lon9v3d7fxuTMhlWTVJB0C8NOOlr8n7mZaICDb9Ira+ZvWdDiVPmXgGFywho\n" +
            "p+Mdb7C0rk/dZehXZXMZAn6nVHYj5llCQiNVTJQClKyzoiq7TDCQr0dLWefFcgig\n" +
            "eNilqO+5+cs8Qf5kkK9XD4rVK9xr619NtulBbxSa17Fe36BiI/WVxSwGcDEtA/AK\n" +
            "CvHVS2hulUC+Qt3bsNAEr6IdW292jTYPzeksjK18fZSiBORRHJh7SZMFpyf4Eojl\n" +
            "mixnGrF7fCOjFQ/bfr8IWrJH6BTVOChAwWEjdU42IvJQreKpSjnhauf8E9guZqsn\n" +
            "xyIQHbzMElRXdGRwfi9uVp1K8ZIcS+RaHphfveCz5aIuvTjv3yzXCO489Gz3pJWo\n" +
            "Ihu3+n1cE2ouT6s3JH2JrOjzT+hMaKuTXT5PR+6qN4GMt0ZR3ho4VBc+EBzFh0hI\n" +
            "1+WjGbkp0ToThs7nZAAccsYjz44AtKo+BSW/K0eV/UK6f5kJXnTndZRT4uFpfvJS\n" +
            "R4kIAvxQLnJH+jnJHs6NpiPknCcuWWqVrega8VwnsaoG+w==\n" +
            "-----END CERTIFICATE REQUEST-----";

    private static String reqECC = "" +
            "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIBOTCB4AIBADB+MQswCQYDVQQGEwJERTEPMA0GA1UECAwGSGVzc2VuMRQwEgYD\n" +
            "VQQHDAtUYXVudXNzdGVpbjEOMAwGA1UECgwFaXA2bGkxDTALBgNVBAMMBHRlc3Qx\n" +
            "KTAnBgkqhkiG9w0BCQEWGmhvc3RtYXN0ZXJAdGF1bnVzc3RlaW4ubmV0MFkwEwYH\n" +
            "KoZIzj0CAQYIKoZIzj0DAQcDQgAEARn+5NVFVwY70vKMFpmcpeV1kz55Rf4L1NNv\n" +
            "sq0P+1F3OxBRZb5R+fsrbZnf5irHly0jdblvw2tBdjkJdxeXjaAAMAoGCCqGSM49\n" +
            "BAMCA0gAMEUCIQCqeG7YDUMksb8IPuitQE35YZQVW+kAqXEu87sFGE+LuQIgHXj+\n" +
            "Sv5ifpv2kPSEPvl0nGYV7nPQUtC+4Xy6mzybVw4=\n" +
            "-----END CERTIFICATE REQUEST-----\n";


    @Test
    public void testGetReqDataRSA() throws Exception {
        System.out.println("Test testGetReqDataRSA");
        System.out.println("======================");
        CertificateFabric certificateFabric=new CertificateFabric();
        String subject=certificateFabric.getReqSubject(reqRSA).subject;
        System.out.println("subject (rsa): "+subject);
    }


    @Test
    public void testGetReqDataECC() throws Exception {
        System.out.println("Test testGetReqDataECC");
        System.out.println("======================");
        CertificateFabric certificateFabric=new CertificateFabric();
        String subject=certificateFabric.getReqSubject(reqECC).subject;
        System.out.println("subject (ecc): "+subject);
    }

    @Test
    public void validRequest() throws Exception {
        System.out.println("Test validRequest");
        System.out.println("=================");
        CertificateFabric certificateFabric=new CertificateFabric();
        String subject=certificateFabric.getReqSubject(reqRSA).subject;
        System.out.println("subject: "+subject);
        assertNotNull(subject);
        subject=certificateFabric.getReqSubject(reqWrong).subject;
        System.out.println("subject should be null");
        assertNull(subject);
    }


} // class
