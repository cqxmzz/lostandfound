import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class TwitterWrapper
{
	static String consumerKeyStr = "tjBU6rK8myYDImhIBdlrAk8qc";
	static String consumerSecretStr = "YBdSTk7vpLrqSy7zg2q9rWFyN3CTBJdorsnIbSTNCqoBRdmkUu";
	static String accessTokenStr = "3258182814-epBzE6rocU3nI9928wZLBjefsOkFHlF07jNrPt7";
	static String accessTokenSecretStr = "0xKCXtnXfRxMm6awKIrjisGS7FUWwPXF0Wer2M4u7cl9Q";

	public static void main(String[] args)
	{
		TwitterWrapper twitterWrapper = new TwitterWrapper();
		twitterWrapper.postTweet("");
	}
	
	public void postTweet(String string)
	{
		try
		{
			String post = "Someone%20lost%20something%20at%20" + string + "%21%21%20Come%20to%20%40seekitapp%20to%20claim%20it%21%21%20%23lostandfound";
			OAuthConsumer oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKeyStr, consumerSecretStr);
			oAuthConsumer.setTokenWithSecret(accessTokenStr, accessTokenSecretStr);
			HttpPost httpPost = new HttpPost("https://api.twitter.com/1.1/statuses/update.json?status=" + post);
			//httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			oAuthConsumer.sign(httpPost);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			System.out.println(statusCode + ':' + httpResponse.getStatusLine().getReasonPhrase());
			System.out.println(IOUtils.toString(httpResponse.getEntity().getContent()));
		} catch (Exception e)
		{
			System.out.print(e);
		}
	}
}
