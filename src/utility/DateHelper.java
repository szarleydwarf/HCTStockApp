package utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import consts.ConstStrings;

public class DateHelper {
	private ConstStrings cs;
	
	public DateHelper(ConstStrings constStrings){
		this.cs = constStrings;
	}
	
	public String getFormatedDate(){
		Calendar today = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(this.cs.DATE_FORMAT);
		
		return df.format(today.getTime());
	}

	public String getFormatedDateAndTime() {
		Calendar today = Calendar.getInstance();
		SimpleDateFormat df= new SimpleDateFormat(this.cs.DATE_TIME_FORMAT);
		
		return df.format(today.getTime());
	}

	public int getDayOfMonthNum(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int getMonthNum(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH);
	}


	public String getIndexOfMonth(String month) {
		for(int i = 0; i < cs.MONTHS_NAMES.length; i++){
			if(cs.MONTHS_NAMES[i].equals(month)){
				int monthNum = i+1;
				return "" + monthNum;
			}
		}
		return "";
	}

	public int getYearNum(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public int getYearIndex() {
		String year = ""+getYearNum();
		int index = 0;
		for(int i = 0; i < cs.YEARS_NO_STRING.length; i++){
			if(cs.YEARS_NO_STRING[i].equals(year)){
				return i;
			}
		}
		return index;
	}
	
	public boolean isLeapYear(int year){
        if(year % 4 == 0)
        {
            if( year % 100 == 0) {
                if ( year % 400 == 0)
                    return true;
                else
                	return false;
            }
            else
            	return true;
        }
        else {
        	return false;
        }
	}
	
	public boolean isLeapYear(){
		int year = this.getYearNum();
        if(year % 4 == 0)
        {
            if( year % 100 == 0) {
                if ( year % 400 == 0)
                    return true;
                else
                	return false;
            }
            else
            	return true;
        }
        else {
        	return false;
        }
	}

}
