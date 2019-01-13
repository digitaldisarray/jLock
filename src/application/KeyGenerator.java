package application;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyGenerator {
	public SecretKey generateKey(String username, String password, String hwid) {
		// HWID: salt
		// SHA256 of Username + Password: string input

		// Generate the salt from hwid
		byte[] salt = hwid.getBytes();

		// Get char array of the hash
		char[] pword = getSHA(username + password).toCharArray();

		SecretKey secret = null;
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(pword, salt, 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		} catch (NoSuchAlgorithmException e) {

		} catch (InvalidKeySpecException e) {

		}

		// DEBUG
		String saltDebug = new String(salt);
		String pwordDebug = new String(pword);
		String secretDebug = Base64.getEncoder().encodeToString(secret.getEncoded());
		System.out.println("Salt: " + saltDebug);
		System.out.println("Password: " + pwordDebug);
		System.out.println("Secret: " + secretDebug);           
		
		return secret;
	}

	public static String getSHA(String input) {
		try {
			// Static getInstance method is called with hashing SHA
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// digest() method called
			// to calculate message digest of an input
			// and return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}

			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			System.out.println("Exception thrown" + " for incorrect algorithm: " + e);

			return null;
		}
	}
}
