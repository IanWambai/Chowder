package com.toe.chowder.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.toe.chowder.R;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Wednesday on 1/16/2016.
 */
public class ChowderUtils {
    public static String generateTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());

        return timestamp;
    }

    public static String bytesToHexString(byte[] bytes) {
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
            hash = ChowderUtils.bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String password = null;
        try {
            password = Base64.encodeToString(hash.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return password;
    }

    public static String generateRandomId() {
        return UUID.randomUUID().toString();
    }

    public static String generateProductId() {
        //Length of the ID has to be 13
        char[] characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        int length = 13;
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }

    public static SSLSocketFactory hostnameVerification(Context context) {
        try {
            // Get an instance of the Bouncy Castle KeyStore format
            KeyStore trusted = KeyStore.getInstance("BKS");
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream in = context.getResources().openRawResource(R.raw.safaricom_keystore);
            try {
                // Initialize the keystore with the provided trusted certificates
                // Also provide the password of the keystore
                trusted.load(in, "ch0wder1".toCharArray());
            } finally {
                in.close();
            }
            // Pass the keystore to the SSLSocketFactory. The factory is responsible
            // for the verification of the server certificate.
            SSLSocketFactory sslSocketFactory = new SSLSocketFactory(trusted);
            // Hostname verification from certificate
            // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            return sslSocketFactory;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public static void showMessage(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static String encodeBase64(String MPESA_KEY, String MPESA_SECRET) {
        try {
            String appKeySecret = MPESA_KEY + ":" + MPESA_SECRET;
            return "Basic " + Base64.encodeToString(appKeySecret.getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptInitiatorPassword(String securityCertificate, String password) {
        String encryptedPassword = null;

        try {
            Security.addProvider(new BouncyCastleProvider());
            byte[] input = password.getBytes();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            FileInputStream fin = new FileInputStream(new File(securityCertificate));
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(fin);
            PublicKey pk = certificate.getPublicKey();
            cipher.init(Cipher.ENCRYPT_MODE, pk);

            byte[] cipherText = cipher.doFinal(input);

            // Convert the resulting encrypted byte array into a string using base64 encoding
            encryptedPassword = Base64.encodeToString(cipherText, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | CertificateException | FileNotFoundException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
        }

        return encryptedPassword;
    }
}
