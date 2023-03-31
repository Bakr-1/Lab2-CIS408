import java.net.*;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class BackupDB {
	public static void main(String args[]) throws UnknownHostException, IOException {


		try {
			int EMSserverPort = 51111;// the server port
			int BackupPort = 50505;// the server port
			Socket EMSSocket = new Socket(args[0] , EMSserverPort);

			
			DataOutputStream outEMS = new DataOutputStream(EMSSocket.getOutputStream());
			DataInputStream inEMS = new DataInputStream(EMSSocket.getInputStream());

			ServerSocket listenEMSSocket = new ServerSocket(BackupPort);
			while (true) {
				System.out.println("Server is ready and waiting for requests ... ");
				Socket BackupSocket = listenEMSSocket.accept();
				Connection1 c = new Connection1(BackupSocket,outEMS,inEMS); // Thread
			}
		} catch (IOException e) {
			System.out.println("Server Failed Starting Over...");
			int EMSserverPort = 50000;
			int BackupPort = 50505;// the server port

			Socket EMSSocket = new Socket(args[0] , EMSserverPort);

			
			DataOutputStream outEMS = new DataOutputStream(EMSSocket.getOutputStream());
			DataInputStream inEMS = new DataInputStream(EMSSocket.getInputStream());

			ServerSocket listenEMSSocket = new ServerSocket(BackupPort);
			while (true) {
				System.out.println("Backup Server is ready and waiting for requests ... ");
				Socket BackupSocket = listenEMSSocket.accept();
				Connection11 c = new Connection11(BackupSocket,outEMS,inEMS); // Thread
			}
		}
	}
}

class Connection11 extends Thread {
	HashMap<Integer, EMS> emsHashmap = new HashMap<>();

	Socket BackupSocket;
	DataInputStream in,inEMS;
	DataOutputStream out,outEMS;

	public Connection11(Socket aBackupSocket,DataOutputStream outEMS, DataInputStream inEMS) {
		try {
			
			BackupSocket = aBackupSocket;
			this.outEMS=outEMS;
			this.inEMS=inEMS;
			in = new DataInputStream(BackupSocket.getInputStream());
			out = new DataOutputStream(BackupSocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Error Connection:" + e.getMessage());
		}
	}


	public void run() {
		try { // an echo server
			while (true) {
				int choice = in.read();
				if (choice == 1) {
//					EMS ems = new EMS(in);
//					emsHashmap.put(ems.ID, ems);
					

				}
				if (choice == 2) {
					int DeleteID = in.read();
					if (emsHashmap.containsKey(DeleteID)) {
						EMS removed = emsHashmap.remove(DeleteID);
						outEMS.writeUTF("removed " + removed);
					} else {
						outEMS.writeUTF("wrong ID");
					}
				if (choice == 3) {
					String location = in.readUTF();
					String reqObject = EMS.getEmsByLoc(location, emsHashmap);
					outEMS.writeUTF(reqObject == null ? "There is no event on this location." : reqObject);
					
				}
				if (choice == 4) {
					String date = in.readUTF();
					String reqObject = EMS.getEmsByDate(date, emsHashmap);
					outEMS.writeUTF(reqObject == null ? "There is no event on this date." : reqObject);
				}
				}
				if (choice == 0) {
					System.exit(0);

				}
			}

		} catch (EOFException e) {
			System.out.println("Error EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error readline:" + e.getMessage());
		} finally {
			try {
				BackupSocket.close();
			} catch (IOException e) {
				/* close failed */}
		}
	}

}
