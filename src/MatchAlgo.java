import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MatchAlgo {
	
	private double thresholdName = 0.75;
	private double threshold = 0.5;
	private double minCount = 2;
	private double timeThreshold = 12*60*60;
	
	// Find a match in the existing list, return -1 if fail
	public int computeMatch(ArrayList<Thing> things, Thing th) {
		for (int i = 0; i < things.size(); ++i) {
			Thing thing = things.get(i);
			if (computeMatchPoint(th, thing))
				return i;
		}
		return -1;
	}

	// Compute Match Point for two record
	public boolean computeMatchPoint(Thing t1, Thing t2) {
		if (!match(t1.name, t2.name, thresholdName))
			return false;
		if (!timeMatch(t1.time, t2.time))
			return false;
		
		int m = t1.features.size();
		int n = t2.features.size();
		int count = 0;
		for(int i=0; i<m; i++) {
			for(int j=0; i<n; j++) {
				if(match(t1.features.get(i), t2.features.get(j), threshold)) {
					count++;
				}
			}
		}
		return count >= minCount;		
	}
	
	public boolean timeMatch(String t1, String t2) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = sdf.parse(t1);
			Date date2 = sdf.parse(t2);
			long diff = Math.abs(date2.getTime() - date1.getTime());
			if (diff < 1000 * 60 * 60 * 24 * timeThreshold)
				return true;
			return false;
		} catch (ParseException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean match(String str1, String str2, double thres) {
		int distance = editDistanceAlgo(str1, str2);
		return (double)distance/(double)(str1.length() + str2.length())*2 < thres;
	}
	
	private int editDistanceAlgo(String str1, String str2) {
		int len1 = str1.length();
		int len2 = str2.length();
		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];	 
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = str1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = str2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1; 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
		return dp[len1][len2];
	}
	
}
