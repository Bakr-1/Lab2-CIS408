import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Scanner;

public class Q2Client {
	public static void main(String args[]) throws IOException {
		// args[0] = serverIP
		Socket s = null;

		try {
			int EMSserverPort = 50000;
			s = new Socket(args[0], EMSserverPort);
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			Q2Client client = new Q2Client(in, out);

		} catch (UnknownHostException e) {
			System.out.println("Error Socket:" + e.getMessage());
		} catch (EOFException e) {
			
			System.out.println("Reconnect to Backup...");// take over
//			Socket s1 = null;

//			try {
//				int serverPort = 60000;
//				s1 = new Socket(args[0], serverPort);
//				DataInputStream in = new DataInputStream(s.getInputStream());
//				DataOutputStream out = new DataOutputStream(s.getOutputStream());
//				Q2Client client = new Q2Client(in, out);
//				
				
//				
//			} catch (IOException e1) {
//				System.out.println("Error readline:" + e1.getMessage());
//			} finally {
//				if (s1 != null)
//					try {
//						s1.close();
//					} catch (IOException e1) {
//						System.out.println("Error close:" + e1.getMessage());
//					}
//			}

		} catch (IOException e) {
			System.out.println("Error readline:" + e.getMessage());
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
					System.out.println("Error close:" + e.getMessage());
				}
		}
	}

	private static boolean checkDate(String dateStr) {

		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		sdf.setLenient(false);

		try {
			sdf.parse(dateStr);
			LocalDate currentdate = LocalDate.now();
			Month currentMonth = currentdate.getMonth();
			int currentDay = currentdate.getDayOfMonth();

			int currentYear = Year.now().getValue();
			String str[] = dateStr.split("/");
			int month = Integer.parseInt(str[0]);
			int day = Integer.parseInt(str[1]);
			int year = Integer.parseInt(str[2]);
			if (year < currentYear || month < currentMonth.getValue() || day < currentDay) {
				return false;
			}

		} catch (NumberFormatException | ParseException ex) {

			return false; // Returns false if parsing fails (in case of bad input).
		}

		return true; // Returns true for valid date Strings
	}

	public Q2Client(DataInputStream in, DataOutputStream out) throws IOException {
		while (true) {
			System.out.println("----------------------------------------------------------------------");
			System.out.println("Event Management System");
			System.out.println("----------------------------------------------------------------------");
			System.out.println("1- Schedule a new event");
			System.out.println("2- Cancel an existing one");
			System.out.println("3- List all exiting events in one location");
			System.out.println("4- Check if any event is scheduled on a particular date");
			System.out.println("0- Quit");
			System.out.println("----------------------------------------------------------------------");
			System.out.println("Your choice? __");
			Scanner sc = new Scanner(System.in);
			int choice = sc.nextInt();
			out.write(choice);
			if (choice == 1) {
				System.out.println("Enter your new event ID");
				Scanner se = new Scanner(System.in);
				int newID;
				while (!se.hasNextInt()) {
					se.next(); // next input is not an int, so consume it and move on
				}
				newID = se.nextInt();

				System.out.println("Enter your new event Title");
				Scanner ss = new Scanner(System.in);
				String newTitle = ss.next();

				System.out.println("Enter your new event Description");
				Scanner sd = new Scanner(System.in);
				String newDesc = sd.next();

				System.out.println("Enter your new event Location");
				Scanner sl = new Scanner(System.in);
				String newLoc = sl.next();


				System.out.println("Enter your new event Schedule (MM/DD/YYYY)");
				Scanner sscc = new Scanner(System.in);
				String newSchedule = sscc.next();
				while (true) {
					if (checkDate(newSchedule)) {
						break;
					} else {
						System.out.println("Invalid Date.");
						System.out.print("Please Input a valid Date:\n ");
						newSchedule = sscc.next(); // get another number before checking conditions again
					}
				}

				System.out.println("Enter your new event number of Attendees");
				Scanner sa = new Scanner(System.in);
				int newAttendees;
				while (!sa.hasNextInt()) {
					sa.next(); // next input is not an int, so consume it and move on
				}
				newAttendees = sa.nextInt();

				System.out.println("Enter your new event VIP Guest");
				Scanner sv = new Scanner(System.in);
				String newVIP = sv.next();

				System.out.println("Enter your new event Budget");
				Scanner sb = new Scanner(System.in);
				int newbudget = sb.nextInt();

				out.write(newID);
				out.writeUTF(newTitle);
				out.writeUTF(newDesc);
				out.writeUTF(newLoc);
				out.writeUTF(newSchedule);
				out.write(newAttendees);
				out.writeUTF(newVIP);
				out.writeDouble(newbudget);
			}
			if (choice == 2) {
				System.out.println("Enter the event ID you want to delete");
				int DeleteID = sc.nextInt();
				out.write(DeleteID);
				String Removed = in.readUTF();
				System.out.println(Removed);

			}
			if (choice == 3) {
				System.out.println("What location you want to see");
				String x = sc.next();

				out.writeUTF(x);
				String allevents = in.readUTF();
				System.out.println("Events in that location:\n ");
				System.out.println(allevents);

			}
			if (choice == 4) {
				System.out.println("Enter the date (MM/DD/YYYY)");
				String date = sc.next();
				out.writeUTF(date);

				String alldate = in.readUTF();
				System.out.println(alldate);
			}
			if (choice == 0) {
				System.exit(0);
			}
		}
	}

}
