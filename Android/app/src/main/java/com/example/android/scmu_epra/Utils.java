package com.example.android.scmu_epra;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String digest(String algorithm, String data) {
        try {
            MessageDigest digester = MessageDigest.getInstance(algorithm);
            digester.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] messageDigest = digester.digest();
            return Utils.byteArrayToHexString(messageDigest);
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byteArrayToHexString(byte[] array) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : array) {
            int intVal = b & 0xff;
            if (intVal < 0x10)
                hexString.append("0");
            hexString.append(Integer.toHexString(intVal));
        }
        return hexString.toString();
    }


}
