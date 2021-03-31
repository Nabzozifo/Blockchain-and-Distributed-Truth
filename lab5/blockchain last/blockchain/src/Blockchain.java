import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Blockchain {
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static HashMap<String,Output> UTXOs = new HashMap<String,Output>(); //list of all unspent transactions.
	public static HashMap<String,Wallet> wallets = new HashMap<String,Wallet>(); //list of all wallets. 
	public static int difficulty = 5;
	public static float minimumTransaction = 0.1f;
	public static Wallet wallet1;
	public static Wallet wallet3;
	public static Wallet wallet4;
	//public static Wallet walletC;
	public static Transaction genesisTransaction1;
	public static Transaction genesisTransaction3;
	public static Transaction genesisTransaction4;
	Date date = new Date(); // This object contains the current date value
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	SimpleDateFormat formatter1 = new SimpleDateFormat("ddMMyyyyHHmmss");
	String fileName = "Log"+formatter1.format(date)+".txt"; 
	

	public void WriteString(String fileName,String str) 
  throws IOException {
	File file = new File(fileName); 
	if(!file.exists()){
		file.createNewFile();
		FileWriter writer = new FileWriter(file); 
	// Writes the content to the file
		writer.write(str); 
		writer.flush();
		writer.close();
	  }else{
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
		writer.append("\n");
		writer.append(str);
		writer.close();
	  }
	
}

	/* public Transaction genesisTransactionWithCoinbase(Wallet coinbase, Wallet wallet, float value )
			throws NoSuchAlgorithmException {
		genesisTransaction = new Transaction(coinbase.pubKey, wallet.pubKey, value,null);
		genesisTransaction.generateSignature(coinbase.privKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new Output(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		return genesisTransaction;
	} */


	public void createGenesisBlock(String name1, String name3,String name4, float value) throws NoSuchAlgorithmException,
			IOException {
		
		wallet1 = new Wallet(name1);
		wallets.put(name1, wallet1);
		WriteString(fileName, formatter.format(date)+" Creating wallet for "+name1);

		wallet3 = new Wallet(name3);
		wallets.put(name3, wallet3);
		WriteString(fileName, formatter.format(date)+" Creating wallet for "+name3);

		wallet4 = new Wallet(name4);
		wallets.put(name4, wallet4);
		WriteString(fileName, formatter.format(date)+" Creating wallet for "+name4);

		Wallet coinbase = new Wallet();
		WriteString(fileName, formatter.format(date)+" Creating wallet Coinbase");

		//create genesis transaction, which sends 1000 bitcoin to wallets: 
		genesisTransaction1 = new Transaction(coinbase.pubKey, wallet1.pubKey, value,null);
		genesisTransaction1.generateSignature(coinbase.privKey);	 //manually sign the genesis transaction	
		genesisTransaction1.transactionId = "0"; //manually set the transaction id
		genesisTransaction1.outputs.add(new Output(genesisTransaction1.recipient, genesisTransaction1.value, genesisTransaction1.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction1.outputs.get(0).id, genesisTransaction1.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		WriteString(fileName, formatter.format(date)+" Send 1000 bitcoin to wallet of "+name1);
		
		genesisTransaction3 = new Transaction(coinbase.pubKey, wallet3.pubKey, value,null);
		genesisTransaction3.generateSignature(coinbase.privKey);	 //manually sign the genesis transaction	
		genesisTransaction3.transactionId = "0"; //manually set the transaction id
		genesisTransaction3.outputs.add(new Output(genesisTransaction3.recipient, genesisTransaction3.value, genesisTransaction3.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction3.outputs.get(0).id, genesisTransaction3.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		WriteString(fileName, formatter.format(date)+" Send 1000 bitcoin to wallet of "+name3);

		genesisTransaction4 = new Transaction(coinbase.pubKey, wallet4.pubKey, value,null);
		genesisTransaction4.generateSignature(coinbase.privKey);	 //manually sign the genesis transaction	
		genesisTransaction4.transactionId = "0"; //manually set the transaction id
		genesisTransaction4.outputs.add(new Output(genesisTransaction4.recipient, genesisTransaction4.value, genesisTransaction4.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction4.outputs.get(0).id, genesisTransaction4.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		WriteString(fileName, formatter.format(date)+" Send 1000 bitcoin to wallet of "+name4);
		
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction1);
		genesis.addTransaction(genesisTransaction3);
		genesis.addTransaction(genesisTransaction4);
		addBlock(genesis);
		WriteString(fileName, formatter.format(date)+" Creating and Mining Genesis block");

	}

	public void send(Wallet wallet1,Wallet wallet3,float value) throws NoSuchAlgorithmException, IOException {
		Block block = new Block(blockchain.get(blockchain.size()-1).hash);
		WriteString(fileName, formatter.format(date)+" " +wallet1.name+" is trying to send funds "+ value +" to "+wallet3.name);
		block.addTransaction(wallet1.sendFunds(wallet3.pubKey, value));
		if(wallet1.sendFunds(wallet3.pubKey, value)==null)
			WriteString(fileName, formatter.format(date)+" " +wallet1.name+"is trying to send more funds "+ value +" that it has.");
		else	
			WriteString(fileName, formatter.format(date)+" " +wallet1.name+" has sent "+ value +" to " +wallet3.name);
		addBlock(block);
		WriteString(fileName, formatter.format(date)+" After sending funds");
		WriteString(fileName, formatter.format(date)+" " +wallet1.name+" balance is: " + wallet1.getBalance());
		WriteString(fileName, formatter.format(date)+" " +wallet3.name+" balance is: " + wallet3.getBalance());


	}

	public void addInBlockchain(String name) throws IOException, NumberFormatException, NoSuchAlgorithmException {
		BufferedReader input = new BufferedReader(new FileReader(name));
		String line = input.readLine();
		String c1=line.split(" ")[0];
		String c2=line.split(" ")[1];
		String c3=line.split(" ")[2];
		String c4=line.split(" ")[3];
		createGenesisBlock(c1, c2, c3, Float.parseFloat(c4));
		//int i = 0;
		try {
			while (( line = input.readLine()) != null){
				String client1=line.split(" ")[1];
				String client2=line.split(" ")[2];
				String value=line.split(" ")[3];
				if (wallets.get(client1)!=null && wallets.get(client2)!=null ){
					send(wallets.get(client1), wallets.get(client2),Float.parseFloat(value));
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(input != null) input.close();                
		}

	}






	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		Blockchain bc =new Blockchain();
		bc.addInBlockchain("input.txt");
		/* bc.createGenesisBlock("Client1","Client2","Client3",1000f);
		bc.send(wallet1, wallet3, 100f);
		bc.send(wallet1, wallet3, 15f);*/
		bc.send(wallet1, wallet3, 1556f);
		//bc.send(wallet3, wallet1, 15f); 
		if(isChainValid())
			bc.WriteString(bc.fileName, bc.formatter.format(bc.date)+" Blockchain is valid");
		else
			bc.WriteString(bc.fileName, bc.formatter.format(bc.date)+" Blockchain is valid");
		//isChainValid();
		//add our blocks to the blockchain ArrayList:
		//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		
		//Create wallets:
		/* walletA = new Wallet();
		walletB = new Wallet();	
		walletC = new Wallet();		
		Wallet coinbase = new Wallet();
		
		//create genesis transaction, which sends 100 NoobCoin to walletA: 
		genesisTransaction = new Transaction(coinbase.pubKey, walletA.pubKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new Output(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		
		genesisTransactionb = new Transaction(coinbase.pubKey, walletB.pubKey, 100f, null);
		genesisTransactionb.generateSignature(coinbase.privKey);	 //manually sign the genesis transaction	
		genesisTransactionb.transactionId = "0"; //manually set the transaction id
		genesisTransactionb.outputs.add(new Output(genesisTransactionb.recipient, genesisTransactionb.value, genesisTransactionb.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransactionb.outputs.get(0).id, genesisTransactionb.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		
		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		genesis.addTransaction(genesisTransactionb);
		addBlock(genesis);
		
		//testing
		Block block1 = new Block(genesis.hash);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		System.out.println("\nWalletA is trying to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.pubKey, 40f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block2 = new Block(block1.hash);
		System.out.println("\nWalletA trying to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.pubKey, 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block3 = new Block(block2.hash);
		System.out.println("\nWalletB is trying to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.pubKey, 20));
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance()); */
		
		
		
	}
	
	public static Boolean isChainValid() throws NoSuchAlgorithmException {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String,Output> tempUTXOs = new HashMap<String,Output>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction1.outputs.get(0).id, genesisTransaction1.outputs.get(0));
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
				System.out.println("Current Hashes not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
			
			//loop thru blockchains transactions:
			Output tempOutput;
			for(int t=0; t <currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);
				
				if(!currentTransaction.verifiySignature()) {
					System.out.println("Signature on Transaction(" + t + ") is Invalid");
					return false; 
				}
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("Inputs are note equal to outputs on Transaction(" + t + ")");
					return false; 
				}
				
				for(Input input: currentTransaction.inputs) {	
					tempOutput = tempUTXOs.get(input.OutputId);
					
					if(tempOutput == null) {
						//System.out.println("Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}
					
					if(input.UTXO.value != tempOutput.value) {
						//System.out.println("Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}
					
					tempUTXOs.remove(input.OutputId);
				}
				
				for(Output output: currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}
				
				if( currentTransaction.outputs.get(0).reciepient != currentTransaction.recipient) {
					//System.out.println("Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
					System.out.println("Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}
				
			}
			
		}
		System.out.println("Blockchain is valid");
		return true;
	}
	
	public static void addBlock(Block newBlock) throws NoSuchAlgorithmException {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
}

	/* public static void main(String[] args) throws NoSuchAlgorithmException {
		//Create the new wallets
		walletA = new Wallet();
		walletB = new Wallet();
		//Test public and private keys
		System.out.println("Private and public keys:");
		System.out.println(walletA.privKey);
		System.out.println(walletA.pubKey);
		//Create a test transaction from WalletA to walletB 
		Transaction transaction = new Transaction(walletA.pubKey, walletB.pubKey, 5, null);
		transaction.generateSignature(walletA.privKey);
		//Verify the signature works and verify it from the public key
		System.out.println("Is signature verified");
		System.out.println(transaction.verifiySignature());
	
}
}
 */
