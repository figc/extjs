package extjs;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		System.out.println(Cipher.getMaxAllowedKeyLength("AES"));
		System.out.println(Integer.MAX_VALUE);
		
		for (Provider p : Security.getProviders()) {
			System.out.println(p.getName());
			
			for (Provider.Service ps : p.getServices()) {
				System.out.println("\t" + ps.getAlgorithm() + "\t" + getKeyLength(ps.getAlgorithm()));
			}
		}
	}

	private static int getKeyLength(String algorithm) {
		int max;
		
		try {
			max = Cipher.getMaxAllowedKeyLength(algorithm);
		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
			max = 0;
		}
		
		return max;
	}

}
