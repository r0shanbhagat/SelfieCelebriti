/*
package com.base.commonframework.baselibrary.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.base.commonframework.R;
import com.base.commonframework.application.AbstractApplication;
import com.base.commonframework.uicomponent.CommonConstant;
import com.base.commonframework.utility.NormalUtility;

public class AESSecurity {
    private static int pswdIterations = 10000;
    private static int keySize = 128;
    private static SecretKeySpec secret = getSecret();

    public static String encrypt(String plainText) throws Exception {
        //encrypt the message
        String initialVectorString = AbstractApplication.getAbstractApplication().getString(R.string.AES_VECTOR_STRING);
        String ivCipher = AbstractApplication.getAbstractApplication().getString(R.string.AES_CIPHER_KEY);
        Cipher cipher = Cipher.getInstance(ivCipher);
        final IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secret, initialVector);
        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(encryptedTextBytes));
    }

    @SuppressWarnings("static-access")
    public static String decrypt(String encryptedText) throws Exception {
        encryptedText = replaceExtraCharacter(encryptedText);
        byte[] encryptedTextBytes = Base64.decodeBase64(encryptedText.getBytes());
        // Decrypt the message
        String initialVectorString = AbstractApplication.getAbstractApplication().getString(R.string.AES_VECTOR_STRING);
        String ivCipher = AbstractApplication.getAbstractApplication().getString(R.string.AES_CIPHER_KEY);
        Cipher cipher = Cipher.getInstance(ivCipher);
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(initialVectorString.getBytes()));
        byte[] decryptedTextBytes = null;
        try {
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
            return new String(decryptedTextBytes);
        } catch (IllegalBlockSizeException e) {
            NormalUtility.showException(e);
        } catch (BadPaddingException e) {
            NormalUtility.showException(e);
        }
        return null;
    }

    // Derive the key
    private static SecretKeySpec getSecret() {
        // Derive the key
        SecretKeySpec secret = null;
        try {
            SecretKeyFactory factory;
            String aesCode = AbstractApplication.getAbstractApplication().getString(R.string.AES_SECURITY_CODE);
            String secretFactory = AbstractApplication.getAbstractApplication().getString(R.string.AES_SECRET_KEY);
            factory = SecretKeyFactory.getInstance(secretFactory);

            PBEKeySpec spec = new PBEKeySpec(
                    aesCode.toCharArray(),
                    CommonConstant.SALT_BYTES,
                    pswdIterations,
                    keySize
            );
            SecretKey secretKey = factory.generateSecret(spec);
            secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            NormalUtility.showException(e);

        }
        return secret;
    }

    */
/*public static void main(String args[])
    {
        String decText="v9ZG5nqIO4gz0LRZiHFf9aJaAxuGgXLbATogcnLYEkKel9p5cZze+BnvBBlBsIoY9sW+ppOx7+UP6SjxxJKf2WauxMsewhY45gTeqdbDZ4E4XVC4NituUJHWWoiNiL9aYOOqCxs2CVXiJzkkyCwtDFyimGkmjEh7w+Fy4dsN166X1S3X/kmnW+HqIn9yn/6tfM6J20ZFbLG/jx0P0y4y3poEIH21hw8GIqgSozymMEwKZtbzPVQuIwSpvEYkJgagahUh6EHln1p/PzMuopxml0ogkdvpSsQY3kwWgzM7RbRtb9YkydjZChwpt9HWPHqA8R4cGVlTVQLmY3tvle/QhDBlIKDyhxHD72nxNJJ9uFNPaLkduu7kyGuQiVODO4HYWaZeyyUOwB1n+FxL1XZuSso1HDjJuNHSoCRMPl/A6ie60sXhXQYxYfAqilDEv/7C6+wDTo0PxZ83T4+Rrm05AQ==";

        try {
            System.out.print(decrypt(decText));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*//*


    public static String replaceExtraCharacter(String tokenId) {
        if (null != tokenId) {
            tokenId = tokenId.replace("%20", " ").replace("%25", "%").replace("%26", "&").replace("%2B", "+")
                    .replace("%2C", ",").replace("%28", "(").replace("%29", ")")
                    .replace("%21", "!").replace("%3D", "=").replace("%3C", "<")
                    .replace("%3E", ">").replace("%23", "#").replace("%24", "$")
                    .replace("%27", "'").replace("%2A", "*").replace("%2D", "-")
                    .replace("%2E", ".").replace("%2F", "/").replace("%3A", ":")
                    .replace("%3B", ";").replace("%3F", "?").replace("%40", "@")
                    .replace("%5B", "[").replace("%5C", "\\").replace("%5D", "]")
                    .replace("%5F", "_").replace("%60", "`").replace("%7B", "{")
                    .replace("%7C", "|").replace("%7D", "}");
        }
        return tokenId;
    }

}*/
