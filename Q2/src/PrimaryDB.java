import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.util.stream.Collectors;

public class PrimaryDB {
	public static void main(String args[]) throws InterruptedException {


		try {
			int EMSserverPort = 50000;// the server port
			int PrimaryPort = 60000;// the server port
			System.out.println("Connecting to EMS ...");
			ServerSocket listenEMSSocket = new ServerSocket(PrimaryPort);
			Thread.sleep(5000);
			Socket EMSSocket = new Socket(args[0] , 50000);
			System.out.println("connected to EMS ...");

			//Socket EMSSocket = new Socket(args[0], EMSserverPort);
			DataOutputStream outEMS = new DataOutputStream(EMSSocket.getOutputStream());
			DataInputStream inEMS = new DataInputStream(EMSSocket.getInputStream());

			while (true) {
				System.out.println("Server is ready and waiting for requests ... ");
				Socket PrimarySocket = listenEMSSocket.accept();
				Connection1 c = new Connection1(PrimarySocket,outEMS,inEMS); // Thread
			}
		} catch (IOException e) {
			System.out.println("Error Listen socket:" + e.getMessage());
		}
	}
}

class Connection1 extends Thread {
	HashMap<Integer, EMS> emsHashmap = new HashMap<>();

	Socket PrimarySocket;
	DataInputStream in,inEMS;
	DataOutputStream out,outEMS;

	public Connection1(Socket aPrimarySocket,DataOutputStream outEMS, DataInputStream inEMS) {
		try {
			
			PrimarySocket = aPrimarySocket;
			this.outEMS=outEMS;
			this.inEMS=inEMS;
			in = new DataInputStream(PrimarySocket.getInputStream());
			out = new DataOutputStream(PrimarySocket.getOutputStream());
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
					EMS ems = new EMS(in,out,1);
					emsHashmap.put(ems.ID, ems);
					

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
				PrimarySocket.close();
			} catch (IOException e) {
				/* close failed */}
		}
	}

}
