package com.toe.chowder.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Wednesday on 1/16/2016.
 */
public class Utils {
    public static String generateTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());

        return timestamp;
    }

    public static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String generatePassword(String stringToHash) {
        MessageDigest digest = null;
        String hash = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(stringToHash.getBytes());
            hash = bytesToHexString(digest.digest());

            Log.d("SOAPREQUEST", "Hash: " + hash);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Encode hash to base64
//        String password = HttpRequest.Base64.encode(hash);

        String password = "change-this";

        return password;
    }

    public static String generateRandomId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
