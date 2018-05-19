package com.example.android.scmu_epra;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

    public static String readStream(HttpURLConnection conn) {
        String result = null;
        StringBuffer sb = new StringBuffer();

        try (InputStream is = new BufferedInputStream(conn.getInputStream())) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
