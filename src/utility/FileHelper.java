package utility;

import java.io.File;
import java.io.IOException;

public class FileHelper {
	
	public FileHelper(){
		
	}
	
	public boolean createFolderIfNotExist (String path) {
		File dir = new File(path);
		if(!dir.exists()){
			return dir.mkdir();
		}
		return false;
	}
	
	public boolean createFileIfNotExist(String fileName){
		File file = new File(fileName);
	    if (!file.exists()) {
	    	try {
				return file.createNewFile();
			} catch (IOException e) {
				// TODO Add logger :P
				e.printStackTrace();
			}
	    }
	    return false;
	}

}
