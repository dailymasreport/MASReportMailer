package utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ModifyMessageRequest;

import Configuration.Constants;
import logging.TestLogger;

public class GmailAPI {
	
	public static Gmail service = null;
	
	public GmailAPI(){
		service = getGmailService();
	}
	
	
	/** Application name. */
	private static final String APPLICATION_NAME =
			"Gmail API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".credentials/gmail-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY =
			JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/** Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials
	 * at ~/.credentials/gmail-java-quickstart
	 */
	//    private static final List<String> SCOPES = Arrays.asList(GmailScopes.MAIL_GOOGLE_COM);
	private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_MODIFY);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.    
		InputStream in =
				new FileInputStream("src/main/resources/client_secret.json");
		GoogleClientSecrets clientSecrets =
				GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow =
				new GoogleAuthorizationCodeFlow.Builder(
						HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(DATA_STORE_FACTORY)
				.setAccessType("offline")
				.build();
		
		Credential credential = new AuthorizationCodeInstalledApp(
				flow, new LocalServerReceiver()).authorize("user");
		return credential;
	}

	/**
	 * Build and return an authorized Gmail client service.
	 * @return an authorized Gmail client service
	 * @throws IOException
	 */
	public static Gmail getGmailService() {
		Credential credential = null;
		try {
			credential = authorize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
	}
	
	void markMessageRead(String messageID) throws IOException{
		TestLogger.info("Marking mail as read..");
		ArrayList<String> list = new ArrayList<String>();
		list.add("UNREAD");
		ModifyMessageRequest mods = new ModifyMessageRequest().setRemoveLabelIds(list);
		service.users().messages().modify(Constants.USER, messageID, mods).execute();
	}
	
	public String[] getMailSubject_Date(String user, String msgID) throws IOException{
		
		Message msg = getMessage(user, msgID);
		
		String[] sub_Date = new String[2];
		
		List<MessagePartHeader> list1 = msg.getPayload().getHeaders();
		for(MessagePartHeader mh : list1){
			if(mh.getName().equalsIgnoreCase("Date")){
				String date = mh.getValue();
				//System.out.println(date);
				sub_Date[1] = date.substring(date.indexOf(",")+1, 16).trim();
			}
			
			if(mh.getName().equalsIgnoreCase("subject")){
				sub_Date[0] = mh.getValue();
				break;
			}
			
		}
		
		return sub_Date;
	}
	
	public String getMessageBodyData(String user, String msgID) throws IOException{
		TestLogger.info("Getting HTML content from mail..");
		Message msg = getMessage(user, msgID);
		
		String data = "";
		
		byte[] byt =Base64.decodeBase64(msg.getPayload().getParts().get(0).getBody().getData().trim());
		
		data = new String(byt, "UTF-8");
		try {
			data = data.substring(data.indexOf("<body>"), data.indexOf("</html>"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		data = data.replaceAll("&nbsp;", "");
		data = data.replaceAll("cellspacing", " cellspacing");		
		return data;
	}
	
	private Message getMessage(String user, String msgID) throws IOException{
		Message msg = service.users().messages().get(user, msgID).setFormat("full").execute();
		return msg;
	}
	
	List<Message> listMessagesMatchingQuery(String userId,
			String query) throws IOException {
		ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(userId).setQ(query)
						.setPageToken(pageToken).execute();
			} else {
				break;
			}
		}
		return messages;

	}
	
	/**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to email address of the receiver
     * @param from email address of the sender, the mailbox account
     * @param subject subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException
	 * @throws UnsupportedEncodingException 
     */
    public MimeMessage createEmail(String to,
                                          String cc,
                                          String subject,
                                          String bodyText)
            throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        
        Multipart multiPart = new MimeMultipart("alternative");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(bodyText, "text/html; charset=utf-8");

        
        multiPart.addBodyPart(htmlPart);
        
        List<String> tolist = new ArrayList<String>();
		List<String> CClist = new ArrayList<String>();

		StringTokenizer todetail = new StringTokenizer(to, ";");
		StringTokenizer ccdetail = new StringTokenizer(cc, ";");
		
		
		
		while (todetail.hasMoreTokens()) {
			tolist.add(todetail.nextToken());
		}
		while (ccdetail.hasMoreTokens()) {
			CClist.add(ccdetail.nextToken());
		}
        
		for (int i = 0; i < tolist.size(); i++) {
			email.addRecipients(javax.mail.Message.RecipientType.TO,
					InternetAddress.parse(tolist.get(i)));

		}
		for (int i = 0; i < CClist.size(); i++) {
			email.addRecipients(javax.mail.Message.RecipientType.CC,
					InternetAddress.parse(CClist.get(i)));
		}
		email.setFrom(new InternetAddress("dailymasreport@gmail.com","MAS Report"));
        email.setSubject(subject);
        email.setContent(multiPart);
        return email;
    }
    
    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    public Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
    
    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param emailContent Email to be sent.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
    public Message sendMessage(MimeMessage emailContent)
            throws MessagingException, IOException {
    	TestLogger.info("Sending mail..");
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(Constants.USER, message).execute();
        return message;
    }
}