import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Output {
	public String id;
	public ArrayList<ArrayList<String>> reciepient; // also known as the new owner of these coins.
	public float value; // the amount of coins they own
	public String parentTransactionId; // the id of the transaction this output was created in

	// Constructor
	public Output(ArrayList<ArrayList<String>> reciepient, float value, String parentTransactionId)
			throws NoSuchAlgorithmException {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = Lamport.hashex(Integer.toString(reciepient.hashCode())+Float.toString(value)+parentTransactionId);
	}
	
	//Check if coin belongs to you
	public boolean isMine(ArrayList<ArrayList<String>> publicKey) {
		return (publicKey == reciepient);
	}

}
