package utility;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class Logger {
	private String defaultFolderPath;
	private DateHelper dh;
	private FileHelper fh;
	private String todayLong;
	private String todayShort;
	
	public Logger(DateHelper dateHelper, FileHelper fileHelper, String folderPath){
		this.dh = dateHelper;
		this.fh = fileHelper;
		this.defaultFolderPath = folderPath;
	}

	public void logError(String msg){
		String fileName = this.dh.getFormatedDate()+".txt";
		String filePathName = this.defaultFolderPath+ "\\"+fileName;
		
		boolean folderExist = this.fh.createFolderIfNotExist(defaultFolderPath);		
		boolean fileExist = this.fh.createFileIfNotExist(filePathName);
		
		try {
			FileWriter write = new FileWriter(filePathName, true);
			PrintWriter printLine = new PrintWriter(write);
			printLine.printf("%s"+"%n", this.getTodayLong() + msg);
			printLine.close();
			
		} catch (IOException e) {
			//TODO add JPane with error msg#
			JOptionPane.showMessageDialog(null, "Nie udalo sie zapisac logga\n"+e.getMessage());
			e.printStackTrace();
		}	
	}

	public void setShortDate(String today) {
		todayShort = today;
	}

	public String getTodayLong() {
		return todayLong;
	}

	public String getTodayShort() {
		return todayShort;
	}

	public void setLongDate(String today) {
		todayLong = today;
	}
}
