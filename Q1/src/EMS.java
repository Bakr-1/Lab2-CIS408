import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class EMS {
	int ID;
	String Title;
	String Description;
	String Location;
	String Schedule;
	int Attendees;
	String VIPGuest;
	double Budget;

	public EMS(DataInputStream in) throws IOException {
		this.ID = in.read();
		this.Title = in.readUTF();
		this.Description = in.readUTF();
		this.Location = in.readUTF();
		this.Schedule = in.readUTF();
		this.Attendees = in.read();
		this.VIPGuest = in.readUTF();
		this.Budget = in.readDouble();
	}
	public EMS(DataInputStream in,String what) throws IOException {
		this.ID = in.read();
	}


	public EMS(int ID, String Title, String Description, String Location, String Schedule, int Attendees,
			String VIPGuest, double Budget) {
		this.ID = ID;
		this.Title = Title;
		this.Description = Description;
		this.Location = Location;
		this.Schedule = Schedule;
		this.Attendees = Attendees;
		this.VIPGuest = VIPGuest;
		this.Budget = Budget;

	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getSchedule() {
		return Schedule;
	}

	public void setSchedule(String schedule) {
		Schedule = schedule;
	}

	public int getAttendees() {
		return Attendees;
	}

	public void setAttendees(int attendees) {
		Attendees = attendees;
	}

	public String getVIPGuest() {
		return VIPGuest;
	}

	public void setVIPGuest(String vIPGuest) {
		VIPGuest = vIPGuest;
	}

	public double getBudget() {
		return Budget;
	}

	public void setBudget(double budget) {
		Budget = budget;
	}

	public static String getEmsByDate(String date, Map<Integer, EMS> map) {
		String reqObject = null;

		for (Integer id : map.keySet()) {
			EMS ems = map.get(id);
			if (ems.getSchedule().equals(date)) {
				reqObject += ems + "\n\n";
			}
		}
		return reqObject;
	}

	public static String getEmsByLoc(String loc, Map<Integer, EMS> map) {
		String reqObject = null;

		for (Integer id : map.keySet()) {
			EMS ems = map.get(id);
			if (ems.getLocation().equals(loc)) {
				reqObject += ems + "\n\n";
			}
		} 
		return reqObject;
	}

	@Override
	public String toString() {
		return "EMS: ID=" + ID + ", Title=" + Title + ", Description=" + Description + ", Location=" + Location
				+ ", Schedule=" + Schedule + ", Attendees=" + Attendees + ", VIPGuest=" + VIPGuest + ", Budget="
				+ Budget;
	}

	public void EMSID(int EMSID) {
		ID = EMSID;
	}

	/* Assign the designation to the variable designation. */
	public void EMSTitle(String EMSTitle) {
		Title = EMSTitle;
	}

	public void printEvents() {
		System.out.println("EMS ID:" + ID);
		System.out.println("EMS Title:" + Title);
		System.out.println("Description:" + Description);
		System.out.println("Location:" + Location);
		System.out.println("Schedule:" + Schedule);
		System.out.println("Attendees:" + Attendees);
		System.out.println("VIP Guest:" + VIPGuest);
		System.out.println("Budget:" + Budget);
		System.out.println("-----------------------------");

	}

}
