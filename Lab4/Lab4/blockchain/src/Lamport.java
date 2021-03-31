import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Lamport {

	public static ArrayList<BigInteger> generBit(int n) {
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

	public static ArrayList<String> publicKey(ArrayList<BigInteger> bloc) throws NoSuchAlgorithmException {
		ArrayList<String> bloc_hash = new ArrayList<String>();
		
		for(BigInteger bi : bloc){
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(bi.toString().getBytes(StandardCharsets.UTF_8));
			bloc_hash.add(bytesToHex(encodedhash));
			
		}
		return bloc_hash;
		
	}

		public static  String toBitArray(byte[] byteArray) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < byteArray.length; i++) {
				sb.append(String.format("%8s", Integer.toBinaryString(byteArray[i] & 0xFF)).replace(' ', '0'));
			}
			return sb.toString();
	}

	public static  String hash(String message) throws NoSuchAlgorithmException {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(message.toString().getBytes(StandardCharsets.UTF_8));
		return toBitArray(encodedhash) ;

	}

	public static  String hashex(String message) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(message.toString().getBytes(StandardCharsets.UTF_8));
		return bytesToHex(encodedhash) ;

	}

	public static  ArrayList<BigInteger> privateSignature(String hashmessage,ArrayList<BigInteger> bloc1,ArrayList<BigInteger> bloc2){
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
		

		public static  ArrayList<String> publicSignature(String hashmessage,ArrayList<String> bloc1,ArrayList<String> bloc2){
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

			public static ArrayList<ArrayList<BigInteger>> privateKeys (ArrayList<BigInteger> bloc1,ArrayList<BigInteger> bloc2)
			{
				ArrayList<ArrayList<BigInteger>> arr = new ArrayList<ArrayList<BigInteger>>();
				arr.add(bloc1);
				arr.add(bloc2);
				return 	arr;
				
			}	

			public static  ArrayList<ArrayList<String>> publicKeys (ArrayList<String> bloc1,ArrayList<String> bloc2)
			{
				ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();
				arr.add(bloc1);
				arr.add(bloc2);
				return 	arr;
				
			}

			public static String getDificultyString(int difficulty) {
				return new String(new char[difficulty]).replace('\0', '0');
			}

			public static  boolean verifySignature(String message,ArrayList<BigInteger> signature,ArrayList<ArrayList<String>> arrpub)
					throws NoSuchAlgorithmException {
				String messagehash=hash(message);
						// Signature verification using the message hash
				ArrayList<String> Psignatures = publicSignature(messagehash, arrpub.get(0), arrpub.get(1));
				// Hashage of the signature
				ArrayList<String> hashsignature = publicKey(signature);
				
				return Psignatures.equals(hashsignature);
				
			}
		
		//Tacks in array of transactions and returns a merkle root.
public static String getMerkleRoot(ArrayList<Transaction> transactions) throws NoSuchAlgorithmException {
	int count = transactions.size();
	ArrayList<String> previousTreeLayer = new ArrayList<String>();
	for(Transaction transaction : transactions) {
		previousTreeLayer.add(transaction.transactionId);
	}
	ArrayList<String> treeLayer = previousTreeLayer;
	while(count > 1) {
		treeLayer = new ArrayList<String>();
		for(int i=1; i < previousTreeLayer.size(); i++) {
			treeLayer.add(hashex(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
		}
		count = treeLayer.size();
		previousTreeLayer = treeLayer;
	}
	String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
	return merkleRoot;
}
		
		}

	

	/* public static void main(String[] args) throws Exception {
		Lamport lomp = new Lamport();
		//Private key bloc generation
		ArrayList<BigInteger> bloc1 = lomp.generBit(256) ;
		ArrayList<BigInteger> bloc2 = lomp.generBit(256) ;
		//ArrayList<ArrayList<BigInteger>> arr=lomp.privateKeys(bloc1, bloc2);

		//Private key bloc generation
		ArrayList<String> bloc_hash1 = lomp.publicKey(bloc1);
		ArrayList<String> bloc_hash2 = lomp.publicKey(bloc2);
		ArrayList<ArrayList<String>> arrpub=lomp.publicKeys(bloc_hash1,bloc_hash2);

		//Hash message 
		String message= "Par les soirs bleus d'été, j'irai dans les sentiers,Picoté par les blés, fouler l'herbe menue,Rêveur, j'en sentirai la fraîcheur à mes pieds.Je laisserai le vent baigner ma tête nue.";
		String messagehash=lomp.hash(message);

		//generate signature
		ArrayList<BigInteger> signatures = lomp.privateSignature(messagehash, bloc1, bloc2);

		// Signature verification using the message hash
		//ArrayList<String> Psignatures = lomp.publicSignature(messagehash, bloc_hash1, bloc_hash2);
			// Hashage of the signature
		//ArrayList<String> hashsignature = lomp.publicKey(signatures);
		

		//System.out.println(hashsignature);
		//System.out.println(Psignatures);
		System.out.println(lomp.verifySignature(message, signatures, arrpub));
		 


    }
} */
