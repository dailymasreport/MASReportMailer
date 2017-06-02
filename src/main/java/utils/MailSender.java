package utils;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import Configuration.Constants;

public class MailSender {
	
	public GmailAPI gmailApi;
	
	public MailSender(){
		this.gmailApi = new GmailAPI();
	}
	
	public void sendMail(String subject, String htmlContent) throws MessagingException, IOException{		
		String mailStatus = "PASS";
		
		if(htmlContent.contains("Observation")){
			mailStatus = "FAIL";
		}
		
		MimeMessage msg = gmailApi.createEmail(Constants.toList, Constants.ccList, "Analysis of " + subject + " - " + mailStatus, htmlContent);
		
		gmailApi.sendMessage(msg);
		
	}
}
