package me.charlesrod.Connection;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DB {
	public static Connection getConnection() throws Exception {
		try {
			String propertiesPath = Thread.currentThread().getContextClassLoader().getResource("application.properties").getPath();
			FileInputStream fis = new FileInputStream(propertiesPath);
			Properties prop = new Properties();
			prop.load(fis);
			String driver = (String)prop.get("DRIVER");
			String url = (String)prop.get("URL");
			String username = (String)prop.get("USER");
			String pass = (String)prop.get("PASS");
			Class.forName(driver);
			return DriverManager.getConnection(url,username,pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
