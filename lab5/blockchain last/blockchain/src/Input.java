public class Input {
	public String OutputId; //Reference to Outputs -> transactionId
	public Output UTXO; //Contains the Unspent transaction output
	
	public Input(String OutputId) {
		this.OutputId = OutputId;
	}

}
