package Configuration;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class SupportLib {

	private static final String DATE_FORMAT = "YYYY-MM-dd";
	private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	
	public static String getTodaysDate(){
		Date date = new Date();
		String modifiedDate= dateFormat.format(date);
		return modifiedDate;
	}
	
	public static String getDate(String date) throws ParseException{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
		
		LocalDate output = LocalDate.parse(date, formatter);
		
		System.out.println(output.toString());
		
		return output.toString();
	}

	public static String getPreviousDate(int days){
		Date currentDate = new Date();

		LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		localDateTime = localDateTime.minusDays(days);

		Date previousDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		return dateFormat.format(previousDate);


	}
	
	public static String getDBTime(String mailTime){
		

		if(mailTime.contains("9 AM") || mailTime.contains("10 AM") || mailTime.contains("11 AM")){
			mailTime = "9 AM";
		}
		else if(mailTime.contains("12 PM") || mailTime.contains("1 PM") || mailTime.contains("2 PM")){
			mailTime = "12 PM";
		}
		else if(mailTime.contains("3 PM") || mailTime.contains("4 PM") || mailTime.contains("5 PM")){
			mailTime = "3 PM";
		}
		else if(mailTime.contains("6 PM") || mailTime.contains("7 PM") || mailTime.contains("8 PM")){
			mailTime = "6 PM";
		}
		else if(mailTime.contains("9 PM") || mailTime.contains("10 PM")){
			mailTime = "9 PM";
		}
		else if(mailTime.contains("11 PM") || mailTime.contains("12 AM")){
			mailTime = "11 PM";
		}
		return mailTime;

	}
	
	public static Document getDOCFromString(String str) throws DocumentException{
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new StringReader(str));
		
		return doc;
	}
	
	public static String percentDiff(double initial, double finalVal){
		
		double diff = ((finalVal - initial)/initial)*100;
		String result = String.format("%.2f", diff);
		
		return result + "%";
	}
	
	public static String smsString(String str){
		String output = str.subSequence(str.indexOf("Observation"), str.indexOf("<div align=")).toString();
		output = output.replaceAll("\\<[^>]*>","")
				.replaceAll("\\s+", " ")
//				.replaceAll(" Observation ", "\n")
//				.replaceAll("Observation ", "")
//				.replaceAll(" - ", ". ")
//				.replaceAll(" Additionally.*.", "")
//				.replaceAll("has been observed ", "")
//				.replaceAll("seems to be ", "")
//				.replaceAll("with a difference of", "i.e");
				;
		
		return output;
	}
}
