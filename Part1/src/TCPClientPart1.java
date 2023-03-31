import java.net.*; 
import java.io.*;

public class TCPClientPart1 {
	public static void main (String args[]) { 
		// args[0] = message
		// args[1] = server IP	
		Socket s = null;
		try{
			int serverPort = 50000;
			s = new Socket(args[1], serverPort);
		    DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(args[0]); // UTF is a string encoding
			String data = in.readUTF();	// read a line of data from the stream
			System.out.println("Received: "+ data) ; 
      	}catch (UnknownHostException e) {System.out.println("Error Socket:"+e.getMessage());
      	}catch (EOFException e){System.out.println("Error EOF:"+e.getMessage());
      	}catch (IOException e){System.out.println("Error readline:"+e.getMessage());
      	}finally {if(s!=null) 
			try {s.close(); 
		  	}catch (IOException e) {System.out.println ("Error close:" + e.getMessage());}
		}
	}
}
                       