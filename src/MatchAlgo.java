import java.util.ArrayList;


public class MatchAlgo {
	
	private double threshold = 0.5;
	
	// Find a match in the existing list 
	public int computeMatch(ArrayList<Thing> things, boolean isLost) {
		return 0;
	}

	// Compute Match Point for two record
	public int computeMatchPoint(Thing t1, Thing t2) {
		if(!t1.name.equals(t2.name)) {
			
		}
		if(t1.name.equals("identity")) {
			
		} else if(t1.name.equals("phone")) {
			
		} else {
			
		}
	}
	
	public boolean match(String str1, String str2) {
		int distance = editDistanceAlgo(str1, str2);
		return (double)distance/(double)(str1.length() + str2.length())*2 < threshold;
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
