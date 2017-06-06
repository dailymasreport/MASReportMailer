package utils;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class MailSender {
	
	public GmailAPI gmailApi;
	
	public MailSender(){
		this.gmailApi = new GmailAPI();
	}
	
	public void sendMail(String subject, String htmlContent, String toList, String ccList) throws MessagingException, IOException{
		
		MimeMessage msg = gmailApi.createEmail(toList, ccList, subject, htmlContent);
		
		gmailApi.sendMessage(msg);
		
	}
}
