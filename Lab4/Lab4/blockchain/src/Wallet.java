import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Wallet {
	ArrayList<ArrayList<BigInteger>> privKey;
	ArrayList<ArrayList<String>> pubKey;
	public HashMap<String,Output> UTXOs = new HashMap<String,Output>(); //only UTXOs owned by this wallet.

	public Wallet() throws NoSuchAlgorithmException{
		generateKey();

	}

	public void generateKey() throws NoSuchAlgorithmException {
		privKey=Lamport.privateKeys(Lamport.generBit(256), Lamport.generBit(256));
		pubKey=Lamport.publicKeys(Lamport.publicKey(privKey.get(0)), Lamport.publicKey(privKey.get(1)));
	}

	//returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	public float getBalance() {
		float total = 0;	
        for (Map.Entry<String, Output> item: Blockchain.UTXOs.entrySet()){
        	Output UTXO = item.getValue();
            if(UTXO.isMine(pubKey)) { //if output belongs to me ( if coins belong to me )
            	UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
            	total += UTXO.value ; 
            }
        }  
		return total;
	}
	//Generates and returns a new transaction from this wallet.
	public Transaction sendFunds(ArrayList<ArrayList<String>>  _recipient,float value ) throws NoSuchAlgorithmException{
		if(getBalance() < value) { //gather balance and check funds.
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
    //create array list of inputs
		ArrayList<Input> inputs = new ArrayList<Input>();
    
		float total = 0;
		for (Map.Entry<String, Output> item: UTXOs.entrySet()){
			Output UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new Input(UTXO.id));
			if(total > value) break;
		}
		
		Transaction newTransaction = new Transaction(pubKey, _recipient , value, inputs);
		newTransaction.generateSignature(privKey);
		
		for(Input input: inputs){
			UTXOs.remove(input.OutputId);
		}
		return newTransaction;
	}
	

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }
}