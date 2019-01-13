package application;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWID {
	 public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {

	        String hwid = "";
	        final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
	        final byte[] bytes = main.getBytes("UTF-8");
	        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	        final byte[] md5 = messageDigest.digest(bytes);
	        int i = 0;
	        for (final byte b : md5) {
	        	hwid += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
	            if (i != md5.length - 1) {
	            	hwid += "-";
	            }
	            i++;
	        }
	        return hwid;
	    }
}
