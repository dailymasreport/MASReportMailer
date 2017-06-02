package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import Configuration.Constants;
import testData.MailData;

public class FetchDBData {

	public static HashMap<String, MailData> fetchData(String date, String time) throws SQLException{
		DatabaseUtil db = new DatabaseUtil();
		HashMap<String, MailData> map = new HashMap<>();
		MailData data = null;
		db.execute(Constants.setDateValue_query, date);
		ResultSet results = db.executeQuery(Constants.average_query, time);		
		results.beforeFirst();
		
		while(results.next()){
			data = new MailData();
			String mailer = results.getString(Constants.mailerNameCol);
			data.setMailerName(mailer);
			data.setMailsSentAvg(results.getInt(Constants.mailSentCol));
			data.setOpenRateAvg(results.getDouble(Constants.openRateCol));
			
			map.put(mailer, data);
		}
		
		results = db.executeQuery(Constants.selectBenchmarkData_query, time);
		results.beforeFirst();
		
		while(results.next()){
			String mailer = results.getString(Constants.mailerNameCol);
			map.get(mailer).setMailSentBenchMark(results.getInt(Constants.mailSentCol));
			map.get(mailer).setOpenRateBenchMark(results.getDouble(Constants.openRateCol));
		}
		
		
		
//		for(String mailer : Constants.mailerList){
//			double mainSentTotal = 0;
//			double openRateTotal = 0;
//			
//			data = new MailData();
//			
//			for(int prevWeek = 1; prevWeek<=Constants.preWeeksCount; prevWeek++){
//				data.setMailerName(mailer);
//				try {
//					result = db.executeQuery(SupportLib.getPreviousDate(prevWeek*7), time, mailer);
//					mainSentTotal += result.getDouble("MAILSENT");
//					openRateTotal += result.getDouble("OPENRATE");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					TestLogger.error("Data is not available for " + mailer + " for the previous " + prevWeek + " week");
//					System.out.println(e);
//				}
//
//			}
//			data.setMailsSentAvg(mainSentTotal/Constants.preWeeksCount);
//			data.setOpenRateAvg(openRateTotal/Constants.preWeeksCount);
//			
//			map.put(mailer, data);
//			
//		}
		
		
		//db.CloseDB();

		return map;
	}
}
