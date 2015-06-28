import java.util.ArrayList;
import java.util.List;

public class Thing {
	public String name;
	public String userName;
	public String email;
	List<String> features;
	
	public Thing(String name, String userName, String email) {
		this.name = name;
		this.userName = userName;
		this.email = email;
		this.features = new ArrayList<String>();
	}
}	
