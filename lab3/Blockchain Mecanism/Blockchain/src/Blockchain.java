import java.io.BufferedReader;
import java.io.FileReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Blockchain {
	public int difficulty;
	ArrayList<Block> blocks = new ArrayList<Block>();

	public boolean verifyingblock() throws NoSuchAlgorithmException {
		boolean success = true;
		String prefixString = new String(new char[difficulty]).replace('\0', '0');
		for (int i=0;i<blocks.size();i++ ){
			String previousHash = i==0 ? "" : blocks.get(i-1).hash;
			success = previousHash.equals(blocks.get(i).previous_hash) && 
			blocks.get(i).hash.substring(0, difficulty).equals(prefixString) && 
			blocks.get(i).hash.equals(blocks.get(i).computinghash());

			if(!success){
				success=false;
				break;
			}
		}

		return success;

	}
	
	public static void main(String args[]) throws Exception {
		Blockchain blockchain = new Blockchain();
		blockchain.difficulty=3;
		Block block1 = new Block();
        //if (args.length == 0) {
           // System.out.println("Enter the file name");
        //} else {
            String fileName = "input.txt"; //args[0];
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            String line = input.readLine();
			block1.data=line;
			block1.mineBlock(blockchain.difficulty);
			blockchain.blocks.add(block1);
			int i = 0;
            try {
                while (( line = input.readLine()) != null){
					Block block = new Block();
					block.previous_hash=blockchain.blocks.get(i).hash;
					block.data=blockchain.blocks.get(i).data+" "+line;
					block.mineBlock(blockchain.difficulty);
                    blockchain.blocks.add(block);
					i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(input != null) input.close();                
            }
			for (Block b : blockchain.blocks ){
                System.out.println(b.toString()); 
				System.out.println("\n")   ;           
            }

			if (blockchain.verifyingblock()==true)
				System.out.println("Successfully mined");
			else 
				System.out.println("Not Successfully mined");


			
        //}
    }

}
