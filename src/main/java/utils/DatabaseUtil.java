package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import Configuration.Constants;
import logging.TestLogger;


public class DatabaseUtil {
	private static Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;


	public DatabaseUtil() {
		connection = getConnection();
	}

	private static Connection getConnection(){
		TestLogger.info("Setting up DB connection..");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Properties properties = new Properties();

			properties.setProperty("user", Constants.DBUserName);
			properties.setProperty("password", Constants.DBPassword);

			String url ="jdbc:mysql://" + Constants.DBIP + ":" + Constants.DBPort + "/" + Constants.DBDatabaseName + "?autoReconnect=true&useSSL=false";
			connection = DriverManager.getConnection(url, properties);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			TestLogger.error(e.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			TestLogger.error(e.toString());
		}

		return connection;
	} 

	public void CloseDB() {
		TestLogger.info("Closing DB connection..");
		try {
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			TestLogger.error(e.getStackTrace().toString());
		}
	}

	public ResultSet executeQuery(String query, String... args) {
		TestLogger.info("Executing query..");
		try {

			preparedStatement = connection.prepareStatement(query);
			for(int i = 0; i < args.length; i++){
				preparedStatement.setString(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				TestLogger.error("no data");
				resultSet.beforeFirst();
			}

		} catch (SQLException e) {
			TestLogger.error(e.toString());
		}

		return resultSet;

	}

	public boolean execute(String query, String... args) {
		TestLogger.info("Executing query..");
		boolean result = false;
		try {

			preparedStatement = connection.prepareStatement(query);
			for(int i = 0; i < args.length; i++){
				preparedStatement.setString(i + 1, args[i]);
			}

			result = preparedStatement.execute();
		} catch (SQLException e) {
			TestLogger.error(e.toString());
		}

		return result;

	}

	public int executeUpdate(String query, String... args) {
		TestLogger.info("Executing upate/insert query..");
		int result = 0;
		try {

			preparedStatement = connection.prepareStatement(query);
			for(int i = 0; i < args.length; i++){
				preparedStatement.setString(i + 1, args[i]);
			}
			result = preparedStatement.executeUpdate();

			if (result == 0) {
				TestLogger.error("Duplicate data is being inserted");
				resultSet.beforeFirst();
			}

		} catch (SQLException e) {
			TestLogger.error(e.toString());
		}

		return result;

	}

}
