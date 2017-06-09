package testrunner;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

import org.dom4j.DocumentException;

import Configuration.Constants;
import freemarker.template.TemplateException;
import testData.MailData;
import utils.ExtractMailData;
import utils.HTMLGenerator;
import utils.MailSender;
import utils.SMSSender;

public class Runner {
	
	public static void main(String[] args) throws MessagingException, IOException, DocumentException, SQLException, TemplateException, ParseException {
		ExtractMailData extractMailData = new ExtractMailData();
		
		HTMLGenerator htmlOutput = new HTMLGenerator();
		
		HashMap<String, List<MailData>> mailDataMap = extractMailData.getMailData();
		
		String html = "";
		
		MailSender mailSender = new MailSender();
		
		SMSSender smsSender = new SMSSender();
		
		for(String subject : mailDataMap.keySet()){
			List<MailData> mailData = mailDataMap.get(subject);
			String time = subject.substring(subject.lastIndexOf("at") + 3, subject.length()); 
			html = htmlOutput.generateHTMLFromTemplate(mailData, time);
			
			String mailStatus = "PASS";
			
			if(html.contains("Observation")){
				mailStatus = "FAIL";
			}
			
			mailSender.sendMail( "Analysis of " + subject + " - " + mailStatus, html, Constants.toListMasReport, Constants.ccListMasReport);
			
			String messageStatus = smsSender.sendMsg(mailData, time, Constants.mobileNo);
			if(!messageStatus.equalsIgnoreCase("Success")){
				mailSender.sendMail(Constants.errorSMSSubject, messageStatus, Constants.toListError, Constants.ccListError);
			}		
			
		}
	}
	
}
