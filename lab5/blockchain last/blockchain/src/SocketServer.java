import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * This class implements java Socket server
 * @author pankaj
 *
 */
public class SocketServer implements Runnable{
    
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;
    private static Double result;

    public static void main(String args[]) throws IOException, ClassNotFoundException  {

        // create the socket server object
        server = new ServerSocket(port);
        // keep listens indefinitely until receives 'exit' call or program terminates
        System.out.println("Waiting for the client request");
        // creating socket and waiting for client connection
        Socket socket = server.accept();
        // read from socket to ObjectInputStream object
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Calculatrice mess = (Calculatrice) ois.readObject();
     
            if (mess.op==1) {result = mess.add(mess.a, mess.b);}
            else if (mess.op==2) {result = mess.sub(mess.a, mess.b);}
            else if (mess.op==3) {result = mess.div(mess.a, mess.b);}
            else if (mess.op==4) {result = mess.mul(mess.a, mess.b);}
            Thread guruThread1 = new Thread("Guru1");
            Thread guruThread2 = new Thread("Guru2");
            guruThread1.start();
            guruThread2.start();
            System.out.println("Thread names are following:");
            System.out.println(guruThread1.getName());
            System.out.println(guruThread2.getName());
      
            socket.close();
        //close the ServerSocket object
        server.close();


        
        Socket s = new Socket("localhost", 9776);
        OutputStream os = s.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream (os);
        System.out.println("Sending result : ");
        System.out.println(result);
        oos.writeObject(result);
        s.close();
        
    }
    @Override
        public void run() {
        }
    
}

