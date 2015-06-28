import java.util.ArrayList;
import java.util.List;

public class Thing {
	public String name;
	public String user;
	public String email;
	public String location;
	public String time;			//YYYY-MM-DD
	List<String> features;
	
	public Thing(String name, String user, String email, String time, String location, ArrayList<String> features) {
		this.name = name;
		this.user = user;
		this.email = email;
		this.time = time;
		this.location = location;
		this.features = features;
	}
}	
