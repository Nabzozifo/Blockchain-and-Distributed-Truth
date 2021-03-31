
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

public class Block {
	public static final AtomicInteger count =new AtomicInteger(0);
	public int index;
	public int nonce;
	public String data;
	public String previous_hash = "";
	public String hash;

	public Block() throws NoSuchAlgorithmException {
		index = count.incrementAndGet();

	}

	//function for converting an array of bytes to hexadeccimal string
	public String bytesToHex(byte[] hash) {
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

	// function for encoding a string using SHA-256
	public String hash(String message) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(message.toString().getBytes(StandardCharsets.UTF_8));
		return bytesToHex(encodedhash) ;

	}

	public String computinghash() throws NoSuchAlgorithmException {
		return hash(index + previous_hash + hash(data) + nonce);

	}
	public String mineBlock(int prefix) throws NoSuchAlgorithmException {
		String prefixString = new String(new char[prefix]).replace('\0', '0');
		do {
			nonce++;
			hash = computinghash();
		} while (!hash.substring(0, prefix).equals(prefixString));
		return hash;
	}

	public String toString(){
		return "Block : "+index+"\n data : "+data+"\n hash : "+hash+"\n previous hash : "+previous_hash+"\n Nonce : "+nonce;  
	   }  

	
}