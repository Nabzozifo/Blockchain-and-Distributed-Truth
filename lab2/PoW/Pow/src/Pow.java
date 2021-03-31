import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Pow {
	

	// function to generate a random string of length n 
    public String getRandomString(int n) 
    { 
        String AlphaString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"abcdefghijklmnopqrstuvxyz"+"0123456789"; 
        StringBuilder sb = new StringBuilder(n); 
        for (int i = 0; i < n; i++) { 
            int index = (int)(AlphaString.length()*Math.random()); 
            sb.append(AlphaString.charAt(index)); 
        } 
        return sb.toString(); 
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

	// Function for converting an array byte to bit string
	public String toBitArray(byte[] byteArray) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byteArray.length; i++) {
			sb.append(String.format("%8s", Integer.toBinaryString(byteArray[i] & 0xFF)).replace(' ', '0'));
		}
		return sb.toString();
}

	// function for encoding a string using SHA-256
	public byte[] hash(String message) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(message.toString().getBytes(StandardCharsets.UTF_8));
		return encodedhash ;

	}


	public int proofOfWork(String message,String threshold,int n) throws NoSuchAlgorithmException {
		String nonce=getRandomString(n);
		//String finalhash=hash(message+nonce);
		int attempt=1;
		while (!threshold.equals(bytesToHex(hash(message+nonce)).substring(0, threshold.length()))){
			//System.out.println("Attemmpt " + attempt + " Failed ! ");
			nonce=getRandomString(n);
			attempt++;

		}
		System.out.println("Attemmpt " + attempt + " SucessFull ! ");
		System.out.println("The nonce is " + nonce );
		System.out.println("The concatened hash in hexadecimal is  " + bytesToHex(hash(message+nonce)));
		System.out.println("The threshold is  " + threshold);
		//System.out.println("The concatened hash in bit is  " + toBitArray(hash(message+nonce)));
		return attempt;
	}

	
	public static void main(String[] args) throws Exception{

		ArrayList<String> Messages = new ArrayList<String>();
		ArrayList<Integer> Attemps = new ArrayList<Integer>();
		ArrayList<Long> Timeexec = new ArrayList<Long>();
		ArrayList<Integer> messize = new ArrayList<Integer>();
		Messages.add("Par");
		Messages.add("Par les soirs");
		Messages.add("Par les soirs bleus d'été");
		Messages.add("Par les soirs bleus d'été. Ouvrant la ");
		Messages.add("Par les soirs bleus d'été. Ouvrant la porte etroite");
		Messages.add("Par les soirs bleus d'été. Ouvrant la porte etroite qui chancelait");
		Messages.add("Par les soirs bleus d'été. Ouvrant la porte etroite qui chancelait jai vu");
		Messages.add("Par les soirs bleus d'été. Ouvrant la porte etroite qui chancelait jai vu une beaute");
		
		for (String message : Messages){
			messize.add(message.length());
			long startTime = System.nanoTime();
			Pow pow = new Pow();
			Attemps.add(pow.proofOfWork(message,"00000",10));
			long endTime   = System.nanoTime();
			long totalTime =  (endTime - startTime)/1000;
			Timeexec.add(totalTime);
			System.out.println("The total time of execution is  " + totalTime + " ms" );
			System.out.println("\n");
    }
	System.out.println("********************* RESULTS RESUME *********************");

	System.out.println("Messages length : "+ messize);
	System.out.println("Attemps : "+ Attemps);
	System.out.println("Execution Times : "+ Timeexec);


}
}
