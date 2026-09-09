package util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppLogger{
	//private static final String file = "C:\\Users\\ADMIN\\Task-Management-System\\src\\main\\webapp\\logs\\app.log";
	
	public static void log(String file, String action, String message, String username, String extra) throws IOException
	{
		System.out.println(file);
		FileWriter writer = null;
		
		try {
			String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss"));
			
			String logLine = "[" + time + "] "
                    + "| USER:" + (username != null ? username : "GUEST")
                    + " | ACTION:" + action
                    + " | MESSAGE:" + message
                    + " | DATA:" + (extra != null ? extra : "")
                    + System.lineSeparator();
			
			writer = new FileWriter(file, true);
			writer.write(logLine);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			writer.close();
		}
	}
}