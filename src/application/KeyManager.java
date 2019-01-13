package application;

import java.util.Base64;

import javax.crypto.SecretKey;

public class KeyManager {
	private static SecretKey privateKey;
	private static String username;
	
	public static void setKey(SecretKey key) {
		privateKey = key;
	}
	
	public static SecretKey getKey() {
		return privateKey;
	}
	
	public static String getKeyString() {
		return Base64.getEncoder().encodeToString(privateKey.getEncoded());
	}
	
	public static String getUsername() {
		return username;
	}
	
	public static void setUsername(String uname) {
		username = uname;
	}
}
