import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.util.stream.Collectors;

public class EMSServer {
	public static void main(String args[]) throws IOException, Exception {
		int EMSserverPort = 50000;// the server port
		int PrimaryPort = 60000;// the server port
		int BackupPort = 50505;// the server port
		try {
			ServerSocket listenClientSocket = new ServerSocket(EMSserverPort);
			Thread.sleep(5000);

			System.out.println("Connecting to primary ...");
			Socket PrimarySocket = new Socket(args[0] , PrimaryPort);
			DataOutputStream outPrimary = new DataOutputStream(PrimarySocket.getOutputStream());
			DataInputStream inPrimary = new DataInputStream(PrimarySocket.getInputStream());
			System.out.println("Connected to primary ...");
			
//			Thread.sleep(5000);
//			//Socket PrimarySocket = new Socket(args[0], PrimaryPort);
//			Socket BackupSocket = new Socket(args[0], BackupPort);
//			DataOutputStream outPrimary = new DataOutputStream(PrimarySocket.getOutputStream());
//			//DataOutputStream outBackup = new DataOutputStream(BackupSocket.getOutputStream());
//			//DataInputStream inPrimary = new DataInputStream(PrimarySocket.getInputStream());
//			DataInputStream inBackup = new DataInputStream(BackupSocket.getInputStream());
//			
			//ServerSocket listenPrimarySocket = new ServerSocket(PrimaryPort);
			while (true) {
				System.out.println("EMS Server is ready and waiting for requests ... ");
				Socket clientSocket = listenClientSocket.accept();
				//Socket primarySocket = listenPrimarySocket.accept();

			//	Connection c = new Connection(clientSocket,null, null, null); // Thread
				Connection c1 = new Connection(clientSocket, outPrimary, inPrimary); // Thread

			}
		} catch (IOException e) {
			System.out.println("Server failed starting over");// take over
			
			Socket BackupSocket = new Socket(args[0], BackupPort);
			DataOutputStream outBackup = new DataOutputStream(BackupSocket.getOutputStream());
			DataInputStream inBackup = new DataInputStream(BackupSocket.getInputStream());
			ServerSocket listenBackupSocket = new ServerSocket(BackupPort);
			ServerSocket listenClientSocket = new ServerSocket(EMSserverPort);

			while (true) {
					Socket clientSocket = listenClientSocket.accept();
					Socket backupSocket = listenBackupSocket.accept();
					Connection c2 = new Connection(clientSocket, outBackup, inBackup); // Thread
			}
//			int EMSserverPort = 50000;// the server port
//			int BackupPort = 50505;// the server port
//			Socket BackupSocket = new Socket(args[0], BackupPort);
//			DataOutputStream outBackup = new DataOutputStream(BackupSocket.getOutputStream());
//			DataInputStream inBackup = new DataInputStream(BackupSocket.getInputStream());
//			ServerSocket listenClientSocket = new ServerSocket(EMSserverPort);
//
//			while (true) {
//				System.out.println("Established connection with the Backup server ... ");
//				Socket clientSocket = listenClientSocket.accept();
//				Connection c = new Connection(clientSocket, null, outBackup, null ); // Thread }
//
//			}

		}
		}}
	

class Connection extends Thread {
	HashMap<Integer, EMS> emsHashmap = new HashMap<>();

	Socket clientSocket;
	DataInputStream inClient,inDB;
	DataOutputStream outClient,outDB;

	public Connection(Socket aClientSocket, DataOutputStream outDB, DataInputStream inDB) {
		try {
			clientSocket = aClientSocket;
    		this.outDB = outDB;
    		this.inDB = inDB;
    		
			inClient = new DataInputStream(clientSocket.getInputStream());
			outClient = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Error Connection:" + e.getMessage());
		}
	}

//	public Connection(Socket eMSSocket, DataOutputStream outPrimary2, DataInputStream inPrimary2) { 
//		// TODO Auto-generated constructor stub
//	}

	public void run() {
		try { // an echo server
			while (true) {
				int choice = inClient.read();
				outDB.write(choice);
				if (choice == 1) {
					EMS ems = new EMS(inClient,outDB,1);
					EMS ems1 = new EMS(inClient,outDB,0);
					System.out.println("Event has been created");
				}
				if (choice == 2) {
					int DeleteID = inClient.read();
					outDB.write(DeleteID);
					String deleted = inDB.readUTF();
					outClient.writeUTF(deleted);

				}
				if (choice == 3) {
					String location = inClient.readUTF();
					outDB.writeUTF(location);
					String locationCheck = inDB.readUTF();
					outClient.writeUTF(locationCheck);
				}

				if (choice == 4) {
					String date = inClient.readUTF();
					outDB.writeUTF(date);
					String dateCheck = inDB.readUTF();
					outClient.writeUTF(dateCheck);
				}
				if (choice == 0) {
					System.exit(0);

				}
			}

		} catch (EOFException e) {
			System.out.println("Error EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error readline:" + e.getMessage());//here
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				/* close failed */}
		}
	}

}
