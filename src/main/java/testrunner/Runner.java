package testrunner;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

import org.dom4j.DocumentException;

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
		
		//SMSSender smsSender = new SMSSender();
		
		for(String subject : mailDataMap.keySet()){
			List<MailData> mailData = mailDataMap.get(subject);
			String time = subject.substring(subject.lastIndexOf("at") + 3, subject.length()); 
			html = htmlOutput.generateHTMLFromTemplate(mailData, time);
			
			mailSender.sendMail(subject, html);
			
//			String smsContent = smsSender.smsTextCreator(mailData);
//			if(smsContent.contains("1")){
//				smsContent = "Report till " + time + "\n" + smsContent;
//			}
//			System.out.println(smsContent);
		}
	}
	
}
