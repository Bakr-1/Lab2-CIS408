import java.net.*; 
import java.io.*;

public class TCPServerPart1 {
	public static void main (String args[]) {
		try{
			int  serverPort = 50000; // the server port
			ServerSocket listenSocket = new ServerSocket(serverPort); 
			while(true) {
				System.out.println("Server is ready and waiting for requests ... ");
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket); 
			}
      	} catch(IOException e) {System.out.println("Error Listen socket:"+e.getMessage());}
	}
}

class Connection extends Thread {
	Socket clientSocket;
	DataInputStream in;
	DataOutputStream out;
	
	public Connection (Socket aClientSocket) {
    	try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
     	} catch(IOException e) {System.out.println("Error Connection:"+e.getMessage());}
	}

	public void run(){
     	try { // an echo server
			String data = in.readUTF();	// read a line of data from the stream
			out.writeUTF("they said " +data);
     	}catch (EOFException e){System.out.println("Error EOF:"+e.getMessage());
     	} catch(IOException e) {System.out.println("Error readline:"+e.getMessage());
     	} finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
	}
}
              