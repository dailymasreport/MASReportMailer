package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import testData.MailData;

public class SMSSender {

	public String smsTextCreator(List<MailData> list, String mailTime){
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		int issueCount = 0;
		int passCount = 0;
		
		sb.append("Mails Sent Report(" )
		.append(mailTime).append(")\n");
		
		for(MailData mailData : list){
			if(mailData.getMailSentChange().contains("-")){
				issueCount++;
				if(counter != 0){
					sb.append("\n");
				}
				sb.append(++counter)
				.append(". ")
				.append(mailData.getMailerName())
				.append(" dip by ")
				.append(mailData.getMailSentChange().replace("-", ""));
				
			}
			else if(mailData.getMailSentBenchMark() == 0 && mailData.getMailsSent() > 0){
				issueCount++;
				if(counter != 0){
					sb.append("\n");
				}
				sb.append(++counter)
				.append(". ")
				.append(mailData.getMailerName())
				.append(" running overnight");
			}
			else if(mailData.getMailSentBenchMark() < mailData.getMailsSent() && mailData.getMailSentBenchMark() != 0 && mailData.getMailsSent() != 0){
				passCount++;
				if(counter != 0){
					sb.append("\n");
				}
				sb.append(++counter)
				.append(". ")
				.append(mailData.getMailerName())
				.append(" running fine");
			}
		}
		
// 		if(!(issueCount > 0) && passCount == 5){
// 			return ("Mail Sent Report(" + mailTime + ")\nNo issues observed in the mailers.");
// 		}
		return sb.toString();
		
	}

	public String sendMsg(List<MailData> list, String mailTime, String[] mobileNo) throws IOException{
		
		String smsText = smsTextCreator(list, mailTime);
		
		String url = "";
		
		StringBuilder result = null;
		
		StringBuilder error = new StringBuilder();

		for(String mobile : mobileNo){
			url = buildHttpRequest(mobile, smsText);
			result = new StringBuilder();

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			if(con.getResponseCode() != 200 || !result.toString().contains("Request Number")){
				error.append("Text message not sent for " + mobile + "\n");
			}
			rd.close();
		}

		if(error.length() == 0){
			return "Success";
		}
		else 
			return error.toString();
	}

	public String buildHttpRequest(String mobileNo, String smsText) throws UnsupportedEncodingException{
		return "http://activeconnect.in/UrlPushSendSMS/SendSMS.aspx?auth=Gxj8mgQTxVcYnKEQMtuK3jDDIz4cXg0O&mobileNumber=91" +
				mobileNo + "&messageText=" + URLEncoder.encode(smsText,"UTF-8") + "&IsUnicode=N&tid=9NUTVIbDHBU%3D&masking=MGCBRK&clientMsgID=&dlrURL=";
	}

}
