

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class Thingsrv
 */
public class ThingSrv extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ThingSrv() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("FoundPost");
		String lost_found = request.getParameter("type");
		String data = request.getParameter("data");
		System.out.println(data);
		Gson gson = new Gson();
		Thing thing = gson.fromJson(data, Thing.class);
		DatabaseWrapper dbWrapper = new DatabaseWrapper();
		ArrayList<Thing> matchThings = dbWrapper.getFromDB(!lost_found.equals("lost"));
		MatchAlgo matchAlgo = new MatchAlgo();
		int match = matchAlgo.computeMatch(matchThings, thing);
		if (match != -1) { // Match!
			EmailWrapper ew = new EmailWrapper();
			if (lost_found.equals("lost")) {
				ew.sendEmail(thing, matchThings.get(match));
			} else {
				ew.sendEmail(matchThings.get(match), thing);
			}
			matchThings.remove(match);
			dbWrapper.saveToDB(!lost_found.equals("lost"), matchThings);
		} else { // No match
			ArrayList<Thing> tempThings = dbWrapper.getFromDB(lost_found.equals("lost"));
			tempThings.add(thing);
			dbWrapper.saveToDB(lost_found.equals("lost"), tempThings);
			if (!lost_found.equals("lost")) {
				TwitterWrapper tw = new TwitterWrapper();
				tw.postTweet(thing.location);
			}
		}
		response.setContentType("text/plain");
	    response.setStatus(200);
	    PrintWriter writer = response.getWriter();
	    Gson gson2 = new Gson();
	    String output = gson2.toJson("success");
	    writer.write(output);
	    writer.close();
	}

}
