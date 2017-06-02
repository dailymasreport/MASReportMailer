package utils;

import java.util.List;
import testData.MailData;

public class SMSSender {
	
	public String smsTextCreator(List<MailData> list){
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		for(MailData mailData : list){
			if(mailData.getMailSentChange().contains("-")){
				if(counter != 0){
					sb.append("\n");
				}
				sb.append(++counter)
				.append(". ")
				.append(mailData.getMailerName())
				.append(" dip by ")
				.append(mailData.getMailSentChange().replace("-", ""))
				.append(".");
			}
			else if(mailData.getMailSentBenchMark() == 0 && mailData.getMailsSent() > 0){
				if(counter != 0){
					sb.append("\n");
				}
				sb.append(++counter)
				.append(". ")
				.append(mailData.getMailerName())
				.append(" running overnight.");
			}
		}
		return sb.toString();
	}
	

}
