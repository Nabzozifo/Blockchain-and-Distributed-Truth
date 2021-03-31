import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Lamport {

	public ArrayList<BigInteger> generBit(int n) {
		Random rand = new Random();
		ArrayList<BigInteger> bloc = new ArrayList<BigInteger>();
		for (int i = 0; i < 256; i++) {
			bloc.add(new BigInteger(256, rand));

		}
		return bloc;
	}

	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public ArrayList<String> publicKey(ArrayList<BigInteger> bloc) throws NoSuchAlgorithmException {
		ArrayList<String> bloc_hash = new ArrayList<String>();
		
		for(BigInteger bi : bloc){
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(bi.toString().getBytes(StandardCharsets.UTF_8));
			bloc_hash.add(bytesToHex(encodedhash));
			
		}
		return bloc_hash;
		
	}

		public String toBitArray(byte[] byteArray) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < byteArray.length; i++) {
				sb.append(String.format("%8s", Integer.toBinaryString(byteArray[i] & 0xFF)).replace(' ', '0'));
			}
			return sb.toString();
	}

	public String hash(String message) throws NoSuchAlgorithmException {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(message.toString().getBytes(StandardCharsets.UTF_8));
		return toBitArray(encodedhash) ;

	}

	public ArrayList<BigInteger> privateSignature(String hashmessage,ArrayList<BigInteger> bloc1,ArrayList<BigInteger> bloc2){
		ArrayList<BigInteger> signat = new ArrayList<BigInteger>();
		int[] messint = Arrays.stream(hashmessage.substring(1, hashmessage.length() - 1).split("")).mapToInt(Integer::parseInt).toArray();
		for (int i = 0; i < messint.length; i++) {
			if (messint[i] == 1) {
				signat.add(bloc1.get(i));

			}
			else {
				signat.add(bloc2.get(i));
			}	
		}		
		return signat;
		}
		

		public ArrayList<String> publicSignature(String hashmessage,ArrayList<String> bloc1,ArrayList<String> bloc2){
			ArrayList<String> signat = new ArrayList<String>();
			int[] messint = Arrays.stream(hashmessage.substring(1, hashmessage.length() - 1).split("")).mapToInt(Integer::parseInt).toArray();
			for (int i = 0; i < messint.length; i++) {
				if (messint[i] == 1) {
					signat.add(bloc1.get(i));
	
				}
				else {
					signat.add(bloc2.get(i));
				}	
			}		
			return signat;
			}
		
		

	

	public static void main(String[] args) throws Exception {
		Lamport lomp = new Lamport();
		//Private key bloc generation
		ArrayList<BigInteger> bloc1 = lomp.generBit(256) ;
		ArrayList<BigInteger> bloc2 = lomp.generBit(256) ;

		//Private key bloc generation
		ArrayList<String> bloc_hash1 = lomp.publicKey(bloc1);
		ArrayList<String> bloc_hash2 = lomp.publicKey(bloc2);

		//Hash message 
		String message= "Par les soirs bleus d'été, j'irai dans les sentiers,Picoté par les blés, fouler l'herbe menue,Rêveur, j'en sentirai la fraîcheur à mes pieds.Je laisserai le vent baigner ma tête nue.";
		String messagehash=lomp.hash(message);

		//generate signature
		ArrayList<BigInteger> signatures = lomp.privateSignature(messagehash, bloc1, bloc2);

		// Signature verification using the message hash
		ArrayList<String> Psignatures = lomp.publicSignature(messagehash, bloc_hash1, bloc_hash2);
			// Hashage of the signature
		ArrayList<String> hashsignature = lomp.publicKey(signatures);
		

		System.out.println(hashsignature);
		System.out.println(Psignatures);
		System.out.println(hashsignature.equals(Psignatures));
		 


    }
}
