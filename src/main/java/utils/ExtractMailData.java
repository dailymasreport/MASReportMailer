package utils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.google.api.services.gmail.model.Message;

import Configuration.Constants;
import Configuration.SupportLib;
import freemarker.template.TemplateException;
import testData.MailData;

public class ExtractMailData {

	public static List<MailData> mailDataList = null;

	public static HashMap<String, MailData> mailObjectMap = null;

	public static String time = "";

	public HashMap<String, List<MailData>> mailDataMap = new HashMap<>();

	private List<Document> getDocFromMail() throws IOException, DocumentException{
		String subject = "";

		GmailAPI gmailApi = new GmailAPI();

		List<Message> messageList = gmailApi.listMessagesMatchingQuery(Constants.USER, Constants.query);


		if(messageList.size() == 0){
			System.err.println("NO NEW MAIL RECEIVED");
			System.exit(0);
		}

		List<Document> mails = new ArrayList<Document>();

		for(Message message : messageList){

			String[] sub_Date = gmailApi.getMailSubject_Date(Constants.USER, message.getId());

			subject = sub_Date[0];
			String date = sub_Date[1];

			String bodyData = gmailApi.getMessageBodyData(Constants.USER, message.getId());

			gmailApi.markMessageRead(message.getId());


			Document doc = SupportLib.getDOCFromString(bodyData);

			doc.getRootElement().addAttribute("subject", subject);
			doc.getRootElement().addAttribute("date", date);

			mails.add(doc);
		}
		return mails;
	}

	public HashMap<String, List<MailData>> getMailData() throws IOException, DocumentException, SQLException, TemplateException, MessagingException, ParseException{

		List<Document> docs = getDocFromMail();
		if(!(null == docs)){
			DatabaseUtil db = new DatabaseUtil();
			String mailsSent = "";
			String openRate = "";
			String date = "";
			String xpath = "";
			time = "";
			for(Document doc : docs){
				mailDataList = new LinkedList<>();
				String subject = doc.getRootElement().attributeValue("subject");
				time = subject.substring(subject.lastIndexOf("at") + 3, subject.length());
				date = SupportLib.getDate(doc.getRootElement().attributeValue("date"));

				String DBTime = SupportLib.getDBTime(time);

				mailObjectMap = FetchDBData.fetchData(date, DBTime);

				for(String mailer : Constants.mailerList){
					xpath = "//td[descendant::*[text()='" + mailer + "']]/following-sibling::*[1]/*";				
					mailsSent = doc.selectSingleNode(xpath).getText();
					xpath = "//td[descendant::*[text()='" + mailer + "']]/following-sibling::*[2]/*";
					openRate = doc.selectSingleNode(xpath).getText();
					db.executeUpdate(Constants.insertMailerData_query, date, DBTime, mailer, mailsSent, openRate);
					mailObjectMap.get(mailer).setMailsSent(Double.parseDouble(mailsSent));
					mailObjectMap.get(mailer).setOpenRate(Double.parseDouble(openRate));

					String mailSentDiff = SupportLib.percentDiff(mailObjectMap.get(mailer).getMailSentBenchMark(), Double.parseDouble(mailsSent));
					String openRateDiff = SupportLib.percentDiff(mailObjectMap.get(mailer).getOpenRateBenchMark(), Double.parseDouble(openRate));

					mailObjectMap.get(mailer).setMailSentChange(mailSentDiff);
					mailObjectMap.get(mailer).setOpenRateChange(openRateDiff);

				}

				for(String mailer : mailObjectMap.keySet()){
					mailDataList.add(mailObjectMap.get(mailer));
				}

				//String html = generateHTMLFromTemplate(mailDataList, time);
				mailDataMap.put(subject, mailDataList);

			}
			db.CloseDB();
		}

		return mailDataMap;

	}
}
