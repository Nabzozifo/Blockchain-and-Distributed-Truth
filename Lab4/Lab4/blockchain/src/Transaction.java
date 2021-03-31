import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Transaction {

	public String transactionId; // this is also the hash of the transaction.
	public ArrayList<ArrayList<String>> sender; // senders address/public key.
	public ArrayList<ArrayList<String>> recipient; // Recipients address/public key.
	public float value;
	public ArrayList<BigInteger> signature; // this is to prevent anybody else from spending funds in our wallet.

	public ArrayList<Input> inputs = new ArrayList<Input>();
	public ArrayList<Output> outputs = new ArrayList<Output>();

	private static int sequence = 0; // a rough count of how many transactions have been generated.

	// Constructor:
	public Transaction(ArrayList<ArrayList<String>> from, ArrayList<ArrayList<String>> to, float value,
			ArrayList<Input> inputs) {
		this.sender = from;
		this.recipient = to;
		this.value = value;
		this.inputs = inputs;
	}

	// This Calculates the transaction hash (which will be used as its Id)
	private String calulateHash() throws NoSuchAlgorithmException {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return Lamport.hashex(Integer.toString(sender.hashCode())+Integer.toString(recipient .hashCode())+Integer.toString(sequence));
	}

	public void generateSignature(ArrayList<ArrayList<BigInteger>> privateKey) throws NoSuchAlgorithmException {
		String data = Integer.toString(sender.hashCode())+Integer.toString(recipient .hashCode())+Float.toString(value);
		signature = Lamport.privateSignature(Lamport.hash(data), privateKey.get(0), privateKey.get(1));		
	}
	//Verifies the data we signed hasnt been tampered with
	public boolean verifiySignature() throws NoSuchAlgorithmException {
		String data = Integer.toString(sender.hashCode())+Integer.toString(recipient .hashCode())+Float.toString(value);
		return Lamport.verifySignature(data, signature, sender);
	}
	//Returns true if new transaction could be created.	
public boolean processTransaction() throws NoSuchAlgorithmException {
		
	if(verifiySignature() == false) {
		System.out.println("#Transaction Signature failed to verify");
		return false;
	}
			
	//gather transaction inputs (Make sure they are unspent):
	for(Input i : inputs) {
		i.UTXO = Blockchain.UTXOs.get(i.OutputId);
	}

	//check if transaction is valid:
	if(getInputsValue() < Blockchain.minimumTransaction) {
		System.out.println("#Transaction Inputs to small: " + getInputsValue());
		return false;
	}
	
	//generate transaction outputs:
	float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
	transactionId = calulateHash();
	outputs.add(new Output( this.recipient, value,transactionId)); //send value to recipient
	outputs.add(new Output( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
			
	//add outputs to Unspent list
	for(Output o : outputs) {
		Blockchain.UTXOs.put(o.id , o);
	}
	
	//remove transaction inputs from UTXO lists as spent:
	for(Input i : inputs) {
		if(i.UTXO == null) continue; //if Transaction can't be found skip it 
		Blockchain.UTXOs.remove(i.UTXO.id);
	}
	
	return true;
}

//returns sum of inputs(UTXOs) values
public float getInputsValue() {
	float total = 0;
	for(Input i : inputs) {
		if(i.UTXO == null) continue; //if Transaction can't be found skip it 
		total += i.UTXO.value;
	}
	return total;
}

//returns sum of outputs:
public float getOutputsValue() {
	float total = 0;
	for(Output o : outputs) {
		total += o.value;
	}
	return total;
}
	
}
