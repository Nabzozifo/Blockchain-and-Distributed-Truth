
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/**
 * This class implements java socket client
 * @author Naby
 *
 */
public class SocketClient {
	//static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
	private static int port = 9776;

    public static void main(String[] args) throws ClassNotFoundException {
	try {
	Socket s = new Socket("localhost", 9876);
	OutputStream os = s.getOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream (os);
	Scanner sc = new Scanner(System.in);
	System.out.print("Choissisez une operation : \n"+"1 : ADDITION (a+b) \n"+" 2: SOUSTRACTION (a-b) \n"
	+"3: DIVISION (a/b) \n"+"4: MULTIPLICATION (a*b) \n");
	int op = sc.nextInt();
	System.out.print("Type the first number a : ");
	double a = sc.nextDouble();
	System.out.print("Type the second number b : ");
	double b = sc.nextDouble();
	/*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("WW-dd-HH");  
   	LocalDateTime now = LocalDateTime.now();  */
	Calculatrice calc = new Calculatrice(a,b,op);
	oos.writeObject(calc);
	sc.close();
	

	server = new ServerSocket(port);
//keep listens indefinitely until receives 'exit' call or program terminates
	System.out.print("Waiting for the server request");
	//creating socket and waiting for client connection
	Socket socket = server.accept();
	InputStream is = socket.getInputStream ();
	ObjectInputStream ois = new ObjectInputStream (is);
	Double res= (Double) ois.readObject();
	System.out.print(res);
	//close the ServerSocket object
	s.close();
	socket.close();
	server.close();
	}catch(IOException e){;}
	}
	
}

