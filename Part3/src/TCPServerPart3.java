import java.net.*; 
import java.io.*;

public class TCPServerPart3 {
	public static void main (String args[]) {
		try{		
			//1. Connect to Backup
			int backupPort = 60000; // the server port
			Socket backupSocket = new Socket(args[0], backupPort);
			DataOutputStream outBackup = new DataOutputStream(backupSocket.getOutputStream());

			
			int serverPort = 50000; // the server port
			ServerSocket listenClientSocket = new ServerSocket(serverPort); 		
			while(true) {
				System.out.println("Primary Server  is ready and waiting for requests ... ");
				Socket clientSocket = listenClientSocket.accept();
				Connection c = new Connection(clientSocket,outBackup,1,0);
			}
      	} catch(IOException e) {System.out.println("Error Listen socket:"+e.getMessage());}
	}
}

class Connection extends Thread {
	Socket clientSocket;
	DataInputStream in;
	DataOutputStream out, outBackup;
	int start,sum;
	public Connection (Socket aClientSocket,DataOutputStream outBackup,int start ,int sum ) {
    	try {
    		clientSocket = aClientSocket;
    		this.outBackup = outBackup;
    		this.start = start;
    		this.sum = sum;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
     	} catch(IOException e) {System.out.println("Error Connection:"+e.getMessage());}
	}

	public void run(){
     	try { 
         		for (int i=start; i<=100; i++) {
         			int data = in.readInt();
         			System.out.println("Received: "+data+" from:"+
         			clientSocket.getInetAddress()+":"+clientSocket.getPort()+","+sum);
         			sum += data;
         			String msg = clientSocket.getInetAddress()+":"+clientSocket.getPort()+","+sum;
         			out.writeUTF(msg.toString()); 
         			outBackup.writeUTF(msg); 

         		}
     		
     	}catch (EOFException e){System.out.println("Error EOF:"+e.getMessage());
     	} catch(IOException e) {System.out.println("Error readline:"+e.getMessage());
     	} finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
	} 
}
              