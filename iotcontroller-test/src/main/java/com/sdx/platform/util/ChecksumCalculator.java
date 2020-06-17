package com.sdx.platform.util;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;
 
// Creates MD5 and SHA-256 checksum in Java
public class ChecksumCalculator {
 
    /*public static void main(String[] args) {
        String data = "Hello World!";
        String md5Hash = getMD5Hash(data);
        String sha256hash = getSHA256Hash(data);
         
        System.out.println("data:"+data);
        System.out.println("md5:"+md5Hash);
        System.out.println("sha256:"+sha256hash);
    }*/
 
    // Java method to create SHA-25 checksum
    public static String getSHA256Hash(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
 
    // Java method to create MD5 checksum
    public static String getMD5Hash(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
     
    /**
     * Use javax.xml.bind.DatatypeConverter class in JDK to convert byte array
     * to a hexadecimal string. Note that this generates hexadecimal in lower case.
     * @param hash
     * @return 
     */
    private static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash).toLowerCase();
    }
}
