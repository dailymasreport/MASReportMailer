package Configuration;

public class Constants {

	public static String userID = "dailymasreport";
	public static String password = "times1234";
	
	public static String toList = "shivesh.verma@timesgroup.com;pushkar.saxena@timesgroup.com;puneet.kukreja@timesgroup.com;amit.khanna1@timesgroup.com;Pankaj.Sharma2@timesgroup.com;Tanmay.Gupta@timesgroup.com;nupur.rustgi@timesgroup.com;Ankur.Tyagi@timesgroup.com;shaurya.sahay@timesgroup.com;Roushan.Singh@timesgroup.com;Ashish.Dixit@timesgroup.com;sahil.goyal@timesgroup.com";
	public static String ccList = "Subodh.Kumar2@timesgroup.com";

	// DB connection details;
	public static String DBIP = "localhost";
	public static String DBPort = "7000";
	public static String DBDatabaseName = "masalertData";
	public static String DBUserName = "root";
	public static String DBPassword = "sahil";	
	public static String DBTableName = "masData";
	public static String mailSentCol = "mailsent";
	public static String openRateCol = "openrate";
	public static String mailerNameCol = "mailer";
	public static String timeCol = "time";
	public static String dateCol = "date";
	public static String[] mailerList = {"PRS", "PRS Instant","RENTAL", "PSM_0To6", "PSM_6To12"};

	public static double preWeeksCount = 1;
	
	public static final String USER = "me";
	public static int loglevel = 1;
	
	//public static String query = "is:unread MAS Report from:MAS ";
	public static String query = "label:inbox is:unread from:info@magicbricks.com MAS Report";
	
	public static String setDateValue_query = "Set @date = ?;";
	public static String insertMailerData_query = "insert into " + DBTableName + " values (?, ?, ?, ?, ?);";
	public static String selectBenchmarkData_query = "SELECT * FROM masalertdata.benchmarkdata where day=DAYNAME(@date) and time=?;";
	public static String average_query = "SELECT mailer, avg(MAILSENT) as " + mailSentCol +" , avg(OPENRATE) as " + openRateCol  +
									" FROM " + DBTableName +
									" WHERE time=? and date" +
									" BETWEEN DATE_SUB(STR_TO_DATE(@date, '%Y-%m-%d') - INTERVAL 7 DAY, INTERVAL 4 WEEK) AND STR_TO_DATE(@date, '%Y-%m-%d') - INTERVAL 7 DAY and dayofweek(date) = dayofweek(STR_TO_DATE(@date, '%Y-%m-%d'))" + 
									" GROUP BY Mailer;";
	
	

}
