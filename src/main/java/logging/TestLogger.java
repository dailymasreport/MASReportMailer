package logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import Configuration.Constants;

public class TestLogger {
	static String location, URL;

	// Initialize Log4j logs
	private static Logger Log = Logger.getLogger(TestLogger.class.getName());


	// Need to create these methods, so that they can be called
	public static void info(String infoMessage) {
		try {
			Log.info(infoMessage);
		} catch (Exception e) {
			System.out
			.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void pass(String passMessage) {
		try {
			Log.info(passMessage);
		} catch (Exception e) {
			System.out
			.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void fail(String FailMessage) {
		try {
			Log.info(FailMessage);
		} catch (Exception e) {
			System.out
			.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void skip(String skipMessage) {
		try {
			Log.info(skipMessage);
		} catch (Exception e) {
			System.out
			.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void skipTestCase(String skipTestCaseMessage) {
		try {
			Log.info(skipTestCaseMessage);
		} catch (Exception e) {
			System.out
			.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void warn(String warningMessage) {
		try {
			Log.warn(warningMessage);
		} catch (Exception e) {
			System.out
			.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void error(String errorMessage) {
		try {
			Log.error("Error#: " + errorMessage);
		} catch (Exception e) {
			System.out
			.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}

	}

	public static void fatal(String fatalMessage) {
		try {
			Log.fatal(fatalMessage);
		} catch (Exception e) {
			System.out
			.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void debug(String debugMessage) {
		try {
			if (Constants.loglevel == 3) {
				System.out.println("Debug# " + debugMessage);
				Log.info(debugMessage);
			}
		}catch (Exception e) {
			System.out
			.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void LogBackup() {
		try {
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("IST"));
			File SRC_LOG_FILE = FileUtils.getFile("logfile.log");
			String LogFileName = df.format(date) + "_log" + ".txt";
			LogFileName = LogFileName.replace(':', '_');
			LogFileName = LogFileName.replace(' ', '_');
			File targetFile = new File(LogFileName);
			FileUtils.copyFile(SRC_LOG_FILE, targetFile);
		} catch (IOException e) {
			System.out.println("Error Creating backup of log file" + e.toString());
			return;
		} catch (Exception e) {
			System.out.println("Error Creating backup of log file" + e.toString());
			return;
		}
	}

	public static void LogClean() {
		try {
			PrintWriter writer = new PrintWriter("logfile.log");
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println(
					"'Logfile.txt' is not available under source folder please make sure that file is available under root location"
							+ e.toString());
			return;
		}
	}

}