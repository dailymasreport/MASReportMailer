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


		if(mailTime.equalsIgnoreCase("9 AM") || mailTime.equalsIgnoreCase("10 AM") || mailTime.equalsIgnoreCase("11 AM")){
			mailTime = "9 AM";
		}
		else if(mailTime.equalsIgnoreCase("12 PM") || mailTime.equalsIgnoreCase("1 PM") || mailTime.equalsIgnoreCase("2 PM")){
			mailTime = "12 PM";
		}
		else if(mailTime.equalsIgnoreCase("3 PM") || mailTime.equalsIgnoreCase("4 PM") || mailTime.equalsIgnoreCase("5 PM")){
			mailTime = "3 PM";
		}
		else if(mailTime.equalsIgnoreCase("6 PM") || mailTime.equalsIgnoreCase("7 PM") || mailTime.equalsIgnoreCase("8 PM")){
			mailTime = "6 PM";
		}
		else if(mailTime.equalsIgnoreCase("9 PM") || mailTime.equalsIgnoreCase("10 PM")){
			mailTime = "9 PM";
		}
		else if(mailTime.equalsIgnoreCase("11 PM") || mailTime.equalsIgnoreCase("12 AM")){
			mailTime = "11 PM";
		}
		//System.out.println(mailTime);
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
}
