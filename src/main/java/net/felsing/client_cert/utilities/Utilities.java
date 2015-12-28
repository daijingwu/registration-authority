package net.felsing.client_cert.utilities;


import java.security.SecureRandom;


import java.math.BigInteger;

/**
 * Created by cf on 28.12.15.
 *
 *
 */
public class Utilities {

    public static String generatePassword() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

}
