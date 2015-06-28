import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;


public class EmailWrapper {
	
	static final String FROM = "ruandi88@gmail.com";  // Replace with your "From" address. This address must be verified.
     
    public static void main(String[] args) {
    	EmailWrapper wrapper = new EmailWrapper();
    	wrapper.sendEmail(null, null);
    }
    
	public void sendEmail(Thing thingLost, Thing thingFound) {	
		System.out.println("Email");
		String BODY_toFinder = "Hi, " + thingFound.email + "\n"+ 
				"Congratulations! We find the loser for your object!" + "\n\n" + "From Seekit";
	    String BODY_toLoser = "Hi, " + thingLost.email + "\n"+ 
	    		"Congratulations! We find your object!" + "\n\n" + "From Seekit";
	    String SUBJECT = "Good News from Seekit";
	    
		Destination toLoser = new Destination().withToAddresses(new String[]{thingLost.email});
        Content subject_toLoser = new Content().withData(SUBJECT);
        Content textBody_toLoser = new Content().withData(BODY_toLoser);
        Body body_toLoser = new Body().withText(textBody_toLoser);
        Message message_toLoser = new Message().withSubject(subject_toLoser).withBody(body_toLoser);
        SendEmailRequest request_toLoser = new SendEmailRequest().withSource(FROM).withDestination(toLoser).withMessage(message_toLoser);
        
        Destination toFinder = new Destination().withToAddresses(new String[]{thingFound.email});
        Content subject_toFinder = new Content().withData(SUBJECT);
        Content textBody_toFinder = new Content().withData(BODY_toFinder);
        Body body_toFinder = new Body().withText(textBody_toFinder);
        Message message_toFinder = new Message().withSubject(subject_toFinder).withBody(body_toFinder);
        SendEmailRequest request_toFinder = new SendEmailRequest().withSource(FROM).withDestination(toFinder).withMessage(message_toFinder);
        
        try
        {        
            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
        
            // Instantiate an Amazon SES client, which will make the service call. The service call requires your AWS credentials. 
            // Because we're not providing an argument when instantiating the client, the SDK will attempt to find your AWS credentials 
            // using the default credential provider chain. The first place the chain looks for the credentials is in environment variables 
            // AWS_ACCESS_KEY_ID and AWS_SECRET_KEY. 
            // For more information, see http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html
            PropertiesCredentials credentials = new PropertiesCredentials(
            		EmailWrapper.class.getResourceAsStream("AwsCredentials.properties"));
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
               
            // Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your sandbox 
            // status, sending limits, and Amazon SES identity-related settings are specific to a given AWS 
            // region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are using 
            // the US West (Oregon) region. Examples of other regions that Amazon SES supports are US_EAST_1 
            // and EU_WEST_1. For a complete list, see http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html 
            Region REGION = Region.getRegion(Regions.US_WEST_2);
            client.setRegion(REGION);
       
            // Send the email.
            client.sendEmail(request_toLoser);
            client.sendEmail(request_toFinder);
            System.out.println("Email sent!");
        }
        catch (Exception ex) 
        {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
	}
}
